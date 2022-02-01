package com.example.splitpaymentapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DetailedSplitActivity extends AppCompatActivity {

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
                else Toast.makeText(DetailedSplitActivity.this, "there is still some amount remaining", Toast.LENGTH_SHORT).show();
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

    }

        private void init(){
            usersLV = findViewById(R.id.DUsersLV);
            amountTV = findViewById(R.id.DPayAmountTV);
            DAddPayBtn = findViewById(R.id.DAddPay);
            remainAmountTV = findViewById(R.id.DremainAmountTV);
            for (User x: users) {
                subs.add(0f);
            }
        }

}