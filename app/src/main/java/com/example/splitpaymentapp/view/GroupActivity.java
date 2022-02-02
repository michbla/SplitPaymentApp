package com.example.splitpaymentapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.DbActions;
import com.example.splitpaymentapp.model.Group;
import com.example.splitpaymentapp.model.IDbActions;
import com.example.splitpaymentapp.model.IReportGenerated;
import com.example.splitpaymentapp.model.Payment;
import com.example.splitpaymentapp.model.Receipt;
import com.example.splitpaymentapp.model.ReportGenerator;
import com.example.splitpaymentapp.model.User;
import com.example.splitpaymentapp.model.UserBalance;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    ArrayList<User> users = new ArrayList<>();
    Group group;
    ListView receiptLV;
    FloatingActionButton addExpenseButton;
    Button payDayBtn;
    String userId;
    ReceiptListAdapter adapter;

    private boolean isIncluded = false;
    List<Receipt> receiptList = new ArrayList<>();
    ArrayList<ArrayList<Payment>> listOfPaymentLists = new ArrayList<>();
    String receiptName, receiptDate;
    float receiptValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        try{
            group = (Group) getIntent().getSerializableExtra("group");
            userId = (String) getIntent().getSerializableExtra("user");
            Log.e("GroupSuccess", "fetched group with name: " + group.getGroupName());
        }
        catch (Exception e){
            Log.e("GroupFatal", "failed to fetch Group");
        }
        init();
        fillUserList();

        addExpenseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent paymentIntent = new Intent(GroupActivity.this, PaymentActivity.class);
                        Bundle bundle = new Bundle();
                        paymentIntent.putParcelableArrayListExtra("u", users);
                        paymentIntent.putExtra("user", userId);
                        paymentIntent.putExtra("groupId",group.getUid());
                        startActivity(paymentIntent);

                    }
                }
        );

        payDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportGenerator rg = new ReportGenerator(receiptList, users, userId, new IReportGenerated() {
                    @Override
                    public void onGenerated(List<UserBalance> ub) {
                        for (UserBalance x:ub){
                            Log.e(x.getUser().getFullName(), String.valueOf(x.getBalance()));
                        }
                    }
                });

            }
        });
    }


    private void fillUserList(){
        List<String> userString = new ArrayList<>();
        DbActions.getUsersFromGroup(group.getUid(), new IDbActions.IBrowseUsers() {
            @Override
            public void onCompleted(List<User> userList) {
                users.addAll(userList);

                for (User u: users) {
                    userString.add(u.getFullName());
                    Log.e("groupActivity", u.getUid());
                }
                ArrayAdapter<String> ad = new ArrayAdapter<>(
                        GroupActivity.this,
                        android.R.layout.simple_list_item_1,
                        userString
                );


            }
        });

        readData(new GetReceiptsCallBack() {
            @Override
            public void onCallback(List<Receipt> list) {
                receiptList.addAll(list);
                adapter = new ReceiptListAdapter(GroupActivity.this, list);
                receiptLV.setAdapter(adapter);

                receiptLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.e("groupPosClick", String.valueOf(position));
                        Intent receiptDetailsIntent = new Intent(GroupActivity.this, ReceiptDetailsActivity.class);
                        Receipt r = list.get(position);
//                        receiptDetailsIntent.putParcelableArrayListExtra("payments", listOfPaymentLists.get(position));
                        receiptDetailsIntent.putExtra("receipt", r);
                        receiptDetailsIntent.putParcelableArrayListExtra("users", users);
                        receiptDetailsIntent.putExtra("userId", userId);
                        startActivity(receiptDetailsIntent);
                    }
                });
            }
        });
    }

    private void init(){
        receiptLV = findViewById(R.id.usersListView);
        addExpenseButton = findViewById(R.id.addExpenseFloatingButton);
        payDayBtn = (Button) findViewById(R.id.payDayBtn);
    }

    private void readData(GetReceiptsCallBack callBack){
        DbActions.browseReceipts(group.getUid(), new IDbActions.IBrowseReceipts() {
            @Override
            public void onCompleted(List<Receipt> receipts) {
//                receiptList.addAll(receipts);
                callBack.onCallback(receipts);
            }
        });
    }

}

interface GetReceiptsCallBack{
    void onCallback(List<Receipt> list);
}