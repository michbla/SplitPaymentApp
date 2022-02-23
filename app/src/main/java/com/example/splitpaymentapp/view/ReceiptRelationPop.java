package com.example.splitpaymentapp.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.Receipt;
import com.example.splitpaymentapp.model.User;

import java.io.Serializable;
import java.util.ArrayList;

public class ReceiptRelationPop extends Activity {

    TextView userTV;
    ListView receiptLV;
    ArrayList<Receipt> receipts = new ArrayList<Receipt>();
    String owedUsername;
    ReceiptListAdapter adapter;
    String userId;
    ArrayList<User> users = new ArrayList<User>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        setContentView(R.layout.receiptrelationpop_layout);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout(dm.widthPixels, dm.heightPixels);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        owedUsername = intent.getStringExtra("username");
        receipts.addAll(intent.getParcelableArrayListExtra("receipts"));
        userId =  intent.getStringExtra("userId");
        users.addAll(intent.getParcelableArrayListExtra("users"));
        Log.e("kaka", String.valueOf(receipts.size()));
        init();
        adapter = new ReceiptListAdapter(ReceiptRelationPop.this, receipts);
        receiptLV.setAdapter(adapter);
        receiptLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailsIntent = new Intent(ReceiptRelationPop.this, ReceiptDetailsActivity.class);
                detailsIntent.putExtra("userId", userId);
                detailsIntent.putExtra("receipt", (Serializable) receipts.get(position));
                detailsIntent.putParcelableArrayListExtra("users", users);
                startActivity(detailsIntent);
            }
        });
        userTV.setText(owedUsername);
    }


    private void init(){
        receiptLV = findViewById(R.id.relationLV);
        userTV = findViewById(R.id.relationUsername);
    }
}
