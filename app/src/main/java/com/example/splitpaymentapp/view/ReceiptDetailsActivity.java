package com.example.splitpaymentapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ListView;
import android.widget.TextView;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.DbActions;
import com.example.splitpaymentapp.model.IDbActions;
import com.example.splitpaymentapp.model.Payment;
import com.example.splitpaymentapp.model.Receipt;
import com.example.splitpaymentapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class ReceiptDetailsActivity extends AppCompatActivity {

    ArrayList<User> users = new ArrayList<User>();
    Receipt receipt;
    TextView receiptDetailsName, receiptDetailsDate;
    ListView receiptDetailsLV;
    List<Payment> paymentsList;
    ReceiptDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_details);
        Intent rcIntent = getIntent();
        users.addAll(rcIntent.getParcelableArrayListExtra("users"));
        receipt = (Receipt) rcIntent.getSerializableExtra("receipt");
        init();
        loadPaymentsFromReceipt();
        receiptDetailsName.setText(receipt.getName());
        receiptDetailsDate.setText(receipt.getDate());
    }

    private void loadPaymentsFromReceipt(){

        DbActions.getPaymentsFromDb(receipt.getId(), new IDbActions.IBrowsePayments() {
            @Override
            public void onCompleted(List<Payment> payments) {
                paymentsList.addAll(payments);



                adapter = new ReceiptDetailsAdapter(ReceiptDetailsActivity.this, paymentsList, users);
                receiptDetailsLV.setAdapter(adapter);
            }
        });
    }

    private void init(){
        paymentsList = new ArrayList<>();
        receiptDetailsName = (TextView) findViewById(R.id.receiptDetailName);
        receiptDetailsDate = (TextView) findViewById(R.id.receiptDetailDate);
        receiptDetailsLV = (ListView) findViewById(R.id.receiptDetailLV);
    }
}