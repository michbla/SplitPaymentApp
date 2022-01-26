package com.example.splitpaymentapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.Group;
import com.example.splitpaymentapp.model.User;

import java.util.ArrayList;
import java.util.Date;

public class EqualPayActivity extends AppCompatActivity {

    ArrayList<User> users = new ArrayList<>();
    ListView usersLV;
    EqualPayAdapter adapter;
    Float amount;
    String date;
    String name;
    TextView amountTV;

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

        amountTV.setText("Kwota: " + amount);

        adapter = new EqualPayAdapter(this, users, amount);
        usersLV.setAdapter(adapter);
    }

    private void init(){
        usersLV = findViewById(R.id.eUsersLV);
        amountTV = findViewById(R.id.ePayAmountTV);

    }

}

//TODO buttony