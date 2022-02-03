package com.example.splitpaymentapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.Group;
import com.example.splitpaymentapp.model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PaymentActivity extends AppCompatActivity {


    private ArrayList<User> users = new ArrayList<User>();
    private String userId;
    private Group groupId;
    private Button splitBtn, equalBtn;
    private EditText rcName, rcDate, rcAmount;
    private SimpleDateFormat dateFormat;
    private String date;
    private String name, sDate;
    private float amount;
    private DateFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Intent usersBundle = getIntent();
        users.addAll(usersBundle.getParcelableArrayListExtra("u"));
        userId = usersBundle.getStringExtra("user");
        groupId = (Group) usersBundle.getSerializableExtra("groupId");
        Log.e("xd", userId);
        init();

        equalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
                if (name.isEmpty()){
                    rcName.setError("pierw dodaj nazwę");
                }
                else if (amount == 0f){
                    rcAmount.setError("pierw dodaj kwotę");
                }
                else if (amount<0f){
                    rcAmount.setError("kwota nie może być ujemna");
                }
                else {
                    Intent equalPayActivity = new Intent(PaymentActivity.this, EqualPayActivity.class);
                    equalPayActivity.putParcelableArrayListExtra("u", users);
                    equalPayActivity.putExtra("date", date.toString());
                    equalPayActivity.putExtra("amount", amount);
                    equalPayActivity.putExtra("payName", name);
                    equalPayActivity.putExtra("groupId", groupId.getUid());
                    equalPayActivity.putExtra("userId", userId);
                    startActivity(equalPayActivity);
                }
            }
        });

        splitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
                if (name.isEmpty()){
                    rcName.setError("pierw dodaj nazwę");
                }
                else if (amount == 0f){
                    rcAmount.setError("pierw dodaj kwotę");
                }
                else if (amount<0f){
                    rcAmount.setError("kwota nie może być ujemna");
                }
                else {
                Intent DetailPayActivity = new Intent(PaymentActivity.this, DetailedSplitActivity.class);
                DetailPayActivity.putParcelableArrayListExtra("u", users);
                DetailPayActivity.putExtra("date", date.toString());
                DetailPayActivity.putExtra("amount", amount);
                DetailPayActivity.putExtra("payName", name);
                DetailPayActivity.putExtra("groupId", groupId.getUid());
                DetailPayActivity.putExtra("userId", userId);
                startActivity(DetailPayActivity);
            }
            }
        });

    }

    private void init(){
        splitBtn =(Button) findViewById(R.id.aloneSplitBtn);
        equalBtn =(Button) findViewById(R.id.equalSplitBtn);
        rcName = (EditText) findViewById(R.id.rcNameET);
        rcDate = (EditText) findViewById(R.id.rcDateET);
        rcDate.setFocusable(false);
        rcAmount = (EditText) findViewById(R.id.rcAmountET);
        Calendar calendar = Calendar.getInstance();
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date;
        date = formatter.format(calendar.getTime());
        rcDate.setText(date);
        rcDate.setFocusable(false);
    }

    private void loadData(){

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        date = now.format(format);
        Log.e("date", date);
        name = rcName.getText().toString().trim();
        try {
            amount = Float.parseFloat(rcAmount.getText().toString().trim());
        }
        catch (Exception e){
            amount = 0f;
        }
//        Toast.makeText(this, name + " " + date + " " + amount, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed(){
        Intent backIntent = new Intent(PaymentActivity.this, GroupActivity.class);
        backIntent.putExtra("user", userId);
        backIntent.putExtra("group", groupId);


        startActivity(backIntent);
        finish();
    }
}