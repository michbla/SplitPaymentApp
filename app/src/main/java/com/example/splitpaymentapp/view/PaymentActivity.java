package com.example.splitpaymentapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.DbActions;
import com.example.splitpaymentapp.model.Payment;
import com.example.splitpaymentapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private List list;
    private ListView lv;
    private ArrayList<User> users = new ArrayList<User>();
    private ArrayList<Payment> payments = new ArrayList<Payment>();
    private PaymentListAdapter adapter;
    private Button makePaymentButton;
    private String userId;
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Intent usersBundle = getIntent();
        users.addAll(usersBundle.getParcelableArrayListExtra("u"));
        userId = usersBundle.getStringExtra("user");
        groupId = usersBundle.getStringExtra("groupId");
        Log.e("xd", userId);
        init();


        list = new ArrayList<User>();
        lv = findViewById(R.id.PaymentLV);
        lv.setItemsCanFocus(true);
        for (int i=0;i<users.size();i++){
            list.add(i);
            Log.e("PaymentTest", users.get(i).getFullName());
        }

        adapter = new PaymentListAdapter(this, users);
        lv.setAdapter(adapter);
        makePaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<list.size();i++){
                    User x = (User) users.get(i);
                    payments.add(new Payment(groupId, x.getUid(), userId, adapter.amounts.get(i)));
                    if (adapter.amounts.get(i)>0f) {
//                      Log.e("xddd", new StringBuilder(payments.get(i).getPaymentFrom() + " " + payments.get(i).getAmount()).toString());
                        DbActions.addPayment(payments.get(i));
                    }
                }

            }
        });
    }

    private void init(){
        makePaymentButton = findViewById(R.id.addPaymentButton);

    }
}