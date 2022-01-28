package com.example.splitpaymentapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.DbActions;
import com.example.splitpaymentapp.model.Group;
import com.example.splitpaymentapp.model.Payment;
import com.example.splitpaymentapp.model.Receipt;
import com.example.splitpaymentapp.model.User;

import java.util.ArrayList;
import java.util.Date;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equal_pay);
        init();
        Intent usersBundle = getIntent();
        users.addAll(usersBundle.getParcelableArrayListExtra("u"));
        amount = usersBundle.getFloatExtra("amount", 0);
        date = usersBundle.getStringExtra("date");
        name = usersBundle.getStringExtra("name");
        groupId = usersBundle.getStringExtra("groupId");
        userId = usersBundle.getStringExtra("userId");

        amountTV.setText("Kwota: " + amount);

        adapter = new EqualPayAdapter(this, users, amount);
        usersLV.setAdapter(adapter);

        eqAddPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ADDPAY CLICK" ,"clicked");
                List<Payment> payments = new ArrayList<Payment>();
                String receiptId = UUID.randomUUID().toString();
                for (int i=0;i<adapter.users.size();i++) {
                    if (adapter.isEnabled.get(i)){
                        String paymentId = UUID.randomUUID().toString();
                        payments.add(new Payment(receiptId, paymentId, userId, users.get(i).getUid(), adapter.subAmounts.get(i)));
                    }
                }
                Receipt rc = new Receipt(receiptId, groupId, adapter.amount, payments);
                DbActions.addReceipt(rc);
                Log.e("ADDPAY CLICK" ,"finished");
            }
        });

    }

    private void init(){
        usersLV = findViewById(R.id.eUsersLV);
        amountTV = findViewById(R.id.ePayAmountTV);
        eqAddPayBtn = findViewById(R.id.eqAddPay);
    }

}

//TODO dodać nazwe paragonu do klasy 