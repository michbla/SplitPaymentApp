package com.example.splitpaymentapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.DbActions;
import com.example.splitpaymentapp.model.DetailProduct;
import com.example.splitpaymentapp.model.IDbActions;
import com.example.splitpaymentapp.model.Payment;
import com.example.splitpaymentapp.model.Receipt;
import com.example.splitpaymentapp.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ReceiptDetailsActivity extends AppCompatActivity {

    ArrayList<User> users = new ArrayList<User>();
    String userId;
    Receipt receipt;
    TextView receiptDetailsName, receiptDetailsDate, receiptDetailsOpt;
    ListView receiptDetailsLV;
    ImageView photoView;
    List<Payment> paymentsList;
    List<DetailProduct> detailProductList;
    String owedId;
    ReceiptDetailsAdapter adapter;
    boolean zalegaOg = false;
    boolean zalegaDet = false;
    boolean nZalega = false;
    boolean nDot = false;
    Payment payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_details);
        Intent rcIntent = getIntent();
        users.addAll(rcIntent.getParcelableArrayListExtra("users"));
        receipt = (Receipt) rcIntent.getSerializableExtra("receipt");
        userId = rcIntent.getStringExtra("userId");
        init();
        loadPaymentsFromReceipt();
        receiptDetailsName.setText(receipt.getName());
        receiptDetailsDate.setText(receipt.getDate());


        StorageReference reference = DbActions.storageReference.child("images/"+receipt.getId());
        reference.getBytes(20000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap btm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                photoView.setImageBitmap(btm);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                photoView.setVisibility(View.INVISIBLE);
                Log.e("loadPhoto", "no photo");
            }
        });

    }

    private void loadPaymentsFromReceipt(){

        DbActions.getPaymentsFromDb(receipt.getId(), new IDbActions.IBrowsePayments() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCompleted(List<Payment> payments) {
                paymentsList.addAll(payments);
                for (Payment x: paymentsList) {
                    if(receipt.getOwnerId().equals(userId)){
                        nZalega = true;
                    }
                    else if(x.getPaymentTo().equals(userId) && x.getDetails().size()==0){
                        zalegaOg = true;
                        payment = x;
                    }
                    else if (x.getPaymentTo().equals(userId) && x.getDetails().size()>0){
                        zalegaDet = true;
                        detailProductList.addAll(x.getDetails());
                        owedId = x.getPaymentFrom();
//                        Log.e("useDetailed", x.getDetails().get(0).getProduct().toString());
//                        receiptDetailsOpt.setVisibility(TextView.VISIBLE);
//                        String text = "zalegasz " + x.getAmount() + " za";
//                        receiptDetailsOpt.setText(text);
//                        adapter = new ReceiptDetailsAdapter(ReceiptDetailsActivity.this, x.getDetails());
                    }
                    else
                        nDot = true;
                }
                String x = new StringBuilder(nZalega + " " + zalegaOg + " " + zalegaDet + " " + nDot).toString();
                Log.e("owes: ", x);

                if (zalegaOg && nDot){
                    nDot = false;
                }
                if (zalegaDet && nDot){
                    nDot = false;
                }
                receiptDetailsOpt.setVisibility(TextView.VISIBLE);
                if (nZalega || nDot){
                    if (nZalega){
                        users.remove(getUserById(userId));
                        for(Payment pay : payments) {
                            if (isSelfPayment(pay))
                                paymentsList.remove(pay);
                        }
                     }
                    receiptDetailsOpt.setText("zapłaciłeś " + getLentValue() + " za");
                    adapter = new ReceiptDetailsAdapter(ReceiptDetailsActivity.this, paymentsList, users);
                    if (nDot){

                        receiptDetailsOpt.setText("nie ma cie na liście");
                    }

                }
                else if (zalegaOg){
                    receiptDetailsOpt.setText("zalegasz: ");
                    adapter = new ReceiptDetailsAdapter(ReceiptDetailsActivity.this, payment, users);
                }
                else if (zalegaDet){
                    receiptDetailsOpt.setText("zalegasz " + getOwedValue() + " dla " + getUserNameById(owedId, users) + " za:");
                    adapter = new ReceiptDetailsAdapter(ReceiptDetailsActivity.this, detailProductList, users, true);
                }

                receiptDetailsLV.setAdapter(adapter);

            }
        });
    }

    public static String getUserNameById(String id, List<User> users){
        for(User x: users){
            if (x.getUid().equals(id))
                return x.getFullName();
        }
        return "not found";
    }

    private User getUserById(String id){
        for(User x: users){
            if (x.getUid().equals(id))
                return x;
        }
        return null;
    }

    private Payment getPaymentByDestId(String id){
        for(Payment x : paymentsList){
            if (x.getPaymentTo().equals(id))
                return x;

        }
        return null;
    }

    private boolean isSelfPayment(Payment x){
        if (x.getPaymentTo().equals(x.getPaymentFrom()))
            return true;
        return false;
    }

    private int remove2SelfPayments(){
        Payment buf = null;
        for(Payment x : paymentsList){
            if (x.getPaymentTo().equals(x.getPaymentFrom())){
                buf = x;
            }
        }
        if (buf != null)
            return paymentsList.indexOf(buf);
        else return -1;
    }

    private void init(){
        paymentsList = new ArrayList<>();
        detailProductList = new ArrayList<>();
        receiptDetailsName = (TextView) findViewById(R.id.receiptDetailName);
        receiptDetailsDate = (TextView) findViewById(R.id.receiptDetailDate);
        receiptDetailsOpt = (TextView) findViewById(R.id.receiptDetailOptTV);
        receiptDetailsOpt.setVisibility(TextView.INVISIBLE);
        receiptDetailsLV = (ListView) findViewById(R.id.receiptDetailLV);
        photoView = (ImageView) findViewById(R.id.receiptPhotoIV);
    }

    private float getLentValue(){
        float am = 0;
        for (Payment x :paymentsList) {
            if (x.getPaymentFrom().equals(userId))
            am += x.getAmount();
        }
        return am;
    }

    private float getOwedValue(){
        float am = 0;
        for (DetailProduct x :detailProductList) {
                am += x.getValue();
        }
        return am;
    }
}