package com.example.splitpaymentapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private List list;
    private ListView lv;
    private ArrayList<User> users = new ArrayList<User>();
    private PaymentListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Intent usersBundle = getIntent();
        users.addAll(usersBundle.getParcelableArrayListExtra("u"));



        list = new ArrayList<User>();
        lv = findViewById(R.id.PaymentLV);
        lv.setItemsCanFocus(true);
        for (int i=0;i<users.size();i++){
            list.add(i);
            Log.e("PaymentTest", users.get(i).getFullName());
        }


        adapter = new PaymentListAdapter(this, users);
        lv.setAdapter(adapter);
    }
}