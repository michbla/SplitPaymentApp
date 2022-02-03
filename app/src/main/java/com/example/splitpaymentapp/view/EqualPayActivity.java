package com.example.splitpaymentapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.DbActions;
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

public class EqualPayActivity extends AppCompatActivity {

    ArrayList<User> users = new ArrayList<>();
    ListView usersLV;
    EqualPayAdapter adapter;
    Float amount;
    String date;
    String name, groupId, userId;
    TextView amountTV;
    Button addPhotoBtn, eqAddPayBtn;
    Bitmap btm;
    File photo;
    static final int IMG_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equal_pay);
        init();
        Intent usersBundle = getIntent();
        users.addAll(usersBundle.getParcelableArrayListExtra("u"));
        amount = usersBundle.getFloatExtra("amount", 0);
        date = usersBundle.getStringExtra("date");
        name = usersBundle.getStringExtra("payName");
        groupId = usersBundle.getStringExtra("groupId");
        userId = usersBundle.getStringExtra("userId");

        amountTV.setText("Kwota: " + amount);

        adapter = new EqualPayAdapter(this, users, amount);
        usersLV.setAdapter(adapter);
        final int IMG_REQUEST = 1;
        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try{
                    startActivityForResult(cameraIntent, IMG_REQUEST);
                }
                catch(ActivityNotFoundException exception){
                    Toast.makeText(EqualPayActivity.this, "nie udało się otworzyć kamery", Toast.LENGTH_LONG).show();
                }
            }
        });


        eqAddPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ADDPAY CLICK" ,"clicked");
                List<Payment> payments = new ArrayList<Payment>();
                List<String> paymentsString = new ArrayList<String>();
                String receiptId = UUID.randomUUID().toString();
                for (int i=0;i<adapter.users.size();i++) {
                    if (adapter.isEnabled.get(i)){
                        String paymentId = UUID.randomUUID().toString();
                        if (!userId.equals(users.get(i).getUid())) {
                            adapter.subAmounts.set(i, Float.valueOf(String.format("%.2f",adapter.subAmounts.get(i))));
                            payments.add(new Payment(receiptId, paymentId, userId, users.get(i).getUid(), adapter.subAmounts.get(i)));
                            paymentsString.add(paymentId.toString());
                        }
                    }
                }
                Receipt rc = new Receipt(paymentsString, receiptId, groupId, adapter.amount,  name, userId, date);
                DbActions.addReceipt(rc);
                for (Payment x: payments) {
                    DbActions.addPayment(x);
                }

                if(btm!=null){
                    try {
                        photo =  File.createTempFile(receiptId, ".jpg");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        btm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] photodata = baos.toByteArray();
                        StorageReference photoRef = DbActions.storageReference.child("images/"+receiptId);
                        UploadTask ut = (UploadTask) photoRef.putBytes(photodata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(EqualPayActivity.this, "dodano zdjęcie", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EqualPayActivity.this, "nie udało się dodać zdjęcia", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (IOException e) {
                        Toast.makeText(EqualPayActivity.this, "nie udało się utworzyć pliku", Toast.LENGTH_SHORT).show();
                    }

                }

                Log.e("ADDPAY CLICK" ,"finished");
                finish();
            }
        });

    }

    private void init(){
        usersLV = findViewById(R.id.eUsersLV);
        amountTV = findViewById(R.id.ePayAmountTV);
        eqAddPayBtn = findViewById(R.id.eqAddPay);
        addPhotoBtn = findViewById(R.id.eqAddPhoto);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            btm = (Bitmap) bundle.get("data");
            Log.e("aparat", "koniec");
        }
    }
}

//TODO dodać nazwe paragonu do klasy