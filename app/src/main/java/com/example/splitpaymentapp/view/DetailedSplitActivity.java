package com.example.splitpaymentapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.DbActions;
import com.example.splitpaymentapp.model.DetailProduct;
import com.example.splitpaymentapp.model.DetailProductList;
import com.example.splitpaymentapp.model.Payment;
import com.example.splitpaymentapp.model.Receipt;
import com.example.splitpaymentapp.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DetailedSplitActivity extends AppCompatActivity {

    private static final int IMG_REQUEST = 12;
    Bitmap btm;
    ArrayList<User> users = new ArrayList<>();
    ArrayList<Float> subs = new ArrayList<>();
    ListView usersLV;
    DetailedPayAdapter adapter;
    Float amount, remainingAmount;
    String date;
    String name, groupId, userId;
    TextView amountTV, remainAmountTV;
    Button addPhotoBtn, DAddPayBtn;
    String receiptId;
    ArrayList<DetailProduct> products = new ArrayList<>();
    ArrayList<Payment> payments = new ArrayList<>();
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_split);

        Intent usersBundle = getIntent();
        users.addAll(usersBundle.getParcelableArrayListExtra("u"));
        amount = remainingAmount = usersBundle.getFloatExtra("amount", 0);
        date = usersBundle.getStringExtra("date");
        name = usersBundle.getStringExtra("payName");
        groupId = usersBundle.getStringExtra("groupId");
        userId = usersBundle.getStringExtra("userId");
        init();
        amountTV.setText("Kwota: " + amount);
        remainAmountTV.setText("Pozostało: " + remainingAmount);
        receiptId = UUID.randomUUID().toString();
        adapter = new DetailedPayAdapter(this, users, amount, subs);
        usersLV.setAdapter(adapter);

        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try{
                    startActivityForResult(cameraIntent, IMG_REQUEST);
                }
                catch(ActivityNotFoundException exception){
                    Toast.makeText(DetailedSplitActivity.this, "nie udało się otworzyć kamery", Toast.LENGTH_LONG).show();
                }
            }
        });

        usersLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailsIntent = new Intent(DetailedSplitActivity.this, PayDetailsPop.class);
                detailsIntent.putExtra("name", users.get(position).getFullName());
                detailsIntent.putExtra("amount", remainingAmount);
                detailsIntent.putExtra("pos", position);
                startActivityForResult(detailsIntent, 2);
            }
        });
        DAddPayBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                if (Math.round(remainingAmount) == 0f) {
                    List<String> paymentStrings = new ArrayList<>();
                    for (Payment x : payments) {
                        paymentStrings.add(x.getPaymentId());
                        DbActions.addPayment(x);
                    }
                    Receipt r = new Receipt(paymentStrings, receiptId, groupId, amount, name, userId, date);
                    DbActions.addReceipt(r);

                }

                if(btm!=null){
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        btm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] photodata = baos.toByteArray();
                        StorageReference photoRef = DbActions.storageReference.child("images/"+receiptId);
                        UploadTask ut = (UploadTask) photoRef.putBytes(photodata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(DetailedSplitActivity.this, "dodano zdjęcie", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DetailedSplitActivity.this, "nie udało się dodać zdjęcia", Toast.LENGTH_SHORT).show();
                            }
                        });


                }

                else Toast.makeText(DetailedSplitActivity.this, "there is still some amount remaining", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("XDDDD", "XDDDD");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == 1) {
                Bundle bundle = data.getBundleExtra("bundle");
                products.clear();
                products.addAll(bundle.getParcelableArrayList("list"));
                int pos = data.getIntExtra("pos", -1);
                float sub = data.getFloatExtra("subAm", 0f);
                for (DetailProduct x : products) {
                    Log.e(x.getProduct(), x.getValue().toString());
                }
                subs.set(pos, sub);
                for (Float x: subs){
                    Log.e("subs", String.valueOf(x));
                }
                remainingAmount -= subs.get(pos);
                remainAmountTV.setText("Pozostało " + remainingAmount);
                adapter.updateSubs(subs.get(pos), pos);
                String paymentId = UUID.randomUUID().toString();
                Payment payment = new Payment(receiptId, paymentId, userId, users.get(pos).getUid(), sub, products);
                payments.add(payment);

            }

        }

        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            btm = (Bitmap) bundle.get("data");
            Log.e("aparat", "koniec");
        }

    }

        private void init(){
            usersLV = findViewById(R.id.DUsersLV);
            amountTV = findViewById(R.id.DPayAmountTV);
            DAddPayBtn = findViewById(R.id.DAddPay);
            addPhotoBtn = findViewById(R.id.DaddPhoto);
            remainAmountTV = findViewById(R.id.DremainAmountTV);
            for (User x: users) {
                subs.add(0f);
            }
        }


}