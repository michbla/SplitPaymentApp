package com.example.splitpaymentapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.DbActions;
import com.example.splitpaymentapp.model.Payment;
import com.example.splitpaymentapp.model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {


    private ArrayList<User> users = new ArrayList<User>();
    private String userId;
    private String groupId;
    private Button splitBtn, equalBtn;
    private EditText rcName, rcDate, rcAmount;
    private SimpleDateFormat dateFormat;
    private Date date;
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
        groupId = usersBundle.getStringExtra("groupId");
        Log.e("xd", userId);
        init();

        equalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
                Intent equalPayActivity = new Intent(PaymentActivity.this, EqualPayActivity.class);
                equalPayActivity.putParcelableArrayListExtra("u", users);
                equalPayActivity.putExtra("date", date.toString());
                equalPayActivity.putExtra("amount", amount);
                equalPayActivity.putExtra("payName", name);
                equalPayActivity.putExtra("groupId", groupId);
                equalPayActivity.putExtra("userId", userId);
                startActivity(equalPayActivity);
            }
        });

        splitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();

            }
        });

    }

    private void init(){
        splitBtn =(Button) findViewById(R.id.aloneSplitBtn);
        equalBtn =(Button) findViewById(R.id.equalSplitBtn);
        rcName = (EditText) findViewById(R.id.rcNameET);
        rcDate = (EditText) findViewById(R.id.rcDateET);
        rcAmount = (EditText) findViewById(R.id.rcAmountET);
        Calendar calendar = Calendar.getInstance();
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date;
        date = formatter.format(calendar.getTime());
        rcDate.setText(date);
    }

    private void loadData(){

        String toDateVar = rcDate.getText().toString();
//        toDateVar = new SimpleDateFormat("dd/MM/yyyy").format(toDateVar);
        name = rcName.getText().toString().trim();

        try {
            date = formatter.parse(toDateVar);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        amount = Float.parseFloat(rcAmount.getText().toString().trim());
        Toast.makeText(this, name + " " + date + " " + amount, Toast.LENGTH_LONG).show();
    }
}