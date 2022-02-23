package com.example.splitpaymentapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    ArrayList<User> users = new ArrayList<>();
    Group group;
    ListView receiptLV;
    TextView groupNameTV, noReceiptAlertTV;
    Button addExpenseButton;
    Button payDayBtn;
    Button addUserBtn;
    Button getOwedBtn, getOwedBtn2, getOwedBtn3;
    String userId;
    ReceiptListAdapter adapter;
    ReportViewAdapter reportAdapter;
    private boolean isIncluded = false;
    List<Receipt> receiptList = new ArrayList<>();
    List<Payment> payments = new ArrayList<>();
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
        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this, AddUsersActivity.class);
                intent.putExtra("group", group);
                intent.putExtra("user", userId);
                startActivity(intent);
                finish();
            }
        });
        if(group.getIsFinished() == 1){
//            addExpenseButton.setVisibility(View.INVISIBLE);
            addUserBtn.setVisibility(View.INVISIBLE);
            addExpenseButton.setText("cofnij do menu");
            payDayBtn.setVisibility(View.VISIBLE);
            payDayBtn.setText("pokaż raport");
            addExpenseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        else {
            addExpenseButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent paymentIntent = new Intent(GroupActivity.this, PaymentActivity.class);
                            Bundle bundle = new Bundle();
                            paymentIntent.putParcelableArrayListExtra("u", users);
                            paymentIntent.putExtra("user", userId);
                            paymentIntent.putExtra("groupId", group);
                            startActivity(paymentIntent);

                        }
                    }
            );
        }


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
                getOwedBtn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            if (list.size() > 0) {
                                noReceiptAlertTV.setText("");
                                receiptLV.setVisibility(View.VISIBLE);
                                adapter = new ReceiptListAdapter(GroupActivity.this, list);
                                receiptLV.setAdapter(adapter);
                                receiptLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Log.e("groupPosClick", String.valueOf(position));
                                        Intent receiptDetailsIntent = new Intent(GroupActivity.this, ReceiptDetailsActivity.class);
                                        Receipt r = list.get(position);
//                        receiptDetailsIntent.putParcelableArrayListExtra("payments", listOfPaymentLists.get(position));
                                        receiptDetailsIntent.putExtra("receipt", (Serializable) r);
                                        receiptDetailsIntent.putParcelableArrayListExtra("users", users);
                                        receiptDetailsIntent.putExtra("userId", userId);
                                        startActivity(receiptDetailsIntent);
                                    }
                                });
                            } else {
                                receiptLV.setVisibility(View.INVISIBLE);
                                noReceiptAlertTV.setText("brak paragonów, dodaj pierwszy");

                            }

                    }
                });
            }
        });
    }

    private void init(){
        receiptLV = findViewById(R.id.usersListView);
        groupNameTV = findViewById(R.id.groupNameTextView);
        groupNameTV.setText(group.getGroupName());
        addUserBtn = findViewById(R.id.gAddUserBtn);
        noReceiptAlertTV = findViewById(R.id.noReceiptAlertTV);
        noReceiptAlertTV.setText("");
        addExpenseButton = findViewById(R.id.addExpenseButton);
        getOwedBtn = findViewById(R.id.getOwedButton);
        getOwedBtn2 = findViewById(R.id.getOwedButton2);
        getOwedBtn3 = findViewById(R.id.getOwedButton3);
        if (!group.getGroupOwner().equals(userId)){
            payDayBtn = (Button) findViewById(R.id.payDayBtn);
            addUserBtn.setVisibility(View.INVISIBLE);
        }
        if (group.getGroupOwner().equals(userId) || (!group.getGroupOwner().equals(userId) && group.getIsFinished() == 1 ) ) {
            payDayBtn = (Button) findViewById(R.id.payDayBtn);
            payDayBtn.setText("Rozlicz grupę");
            payDayBtn.setVisibility(Button.VISIBLE);
            payDayBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addUserBtn.setVisibility(View.INVISIBLE);
                    ReportGenerator rg = new ReportGenerator(receiptList, users, userId, new IReportGenerated() {
                        @Override
                        public void onGenerated(List<UserBalance> ub, List<Payment> p) {
                            group.setFinished();
                            noReceiptAlertTV.setText("grupa jest rozliczona.");
//                            payDayBtn.setVisibility(View.INVISIBLE);
//                            addExpenseButton.setVisibility(View.INVISIBLE);
                            receiptLV.setVisibility(View.VISIBLE);
                            reportAdapter = new ReportViewAdapter(GroupActivity.this, ub);
                            receiptLV.setAdapter(reportAdapter);
                            receiptLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                }
                            });
                            addExpenseButton.setText("cofnij do menu");
                            addExpenseButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onBackPressed();
                                }
                            });

                        }
                    });

                }
            });

        }
        getOwedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportGenerator rg = new ReportGenerator(receiptList, users, userId, new IReportGenerated() {
                    @Override
                    public void onGenerated(List<UserBalance> ub, List<Payment> p) {
                        List<UserBalance> owedList = new ArrayList<>();
                        for (UserBalance x : ub){
                            if (x.getBalance()<0)
                                owedList.add(x);
                        }
                        if (owedList.size()>0) {
                            receiptLV.setVisibility(View.VISIBLE);
                            noReceiptAlertTV.setText("");
                            reportAdapter = new ReportViewAdapter(GroupActivity.this, owedList);
                            receiptLV.setAdapter(reportAdapter);
                            receiptLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(GroupActivity.this, ReceiptRelationPop.class);
                                    intent.putExtra("username", owedList.get(position).getUser().getFullName());
                                    intent.putExtra("userId", userId);
                                    intent.putParcelableArrayListExtra("users", users);
                                    List<Receipt> x = getReceiptsFiltered(false, owedList.get(position).getUser().getUid(), p);
                                    intent.putParcelableArrayListExtra("receipts", (ArrayList<? extends Parcelable>) x);
                                    startActivity(intent);
                                    //todo ściągnąć paymenty z raportu i je przefiltrować zamiast ściągać 2 raz


                                }
                            });
                        }
                        else{
                            receiptLV.setVisibility(View.INVISIBLE);
                            noReceiptAlertTV.setText("jesteś na czysto");
                        }


                    }
                });
            }
        });



        getOwedBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportGenerator rg = new ReportGenerator(receiptList, users, userId, new IReportGenerated() {
                    @Override
                    public void onGenerated(List<UserBalance> ub, List<Payment> p) {
                        List<UserBalance> owedList = new ArrayList<>();
                        for (UserBalance x : ub){
                            if (x.getBalance()>0)
                                owedList.add(x);
                        }
                        if (owedList.size()>0) {
                            receiptLV.setVisibility(View.VISIBLE);
                            noReceiptAlertTV.setText("");
                            reportAdapter = new ReportViewAdapter(GroupActivity.this, owedList);
                            receiptLV.setAdapter(reportAdapter);
                            receiptLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(GroupActivity.this, ReceiptRelationPop.class);
                                    intent.putExtra("username", owedList.get(position).getUser().getFullName());
                                    intent.putExtra("userId", userId);
                                    intent.putParcelableArrayListExtra("users", users);

                                    List<Receipt> x = getReceiptsFiltered(true, owedList.get(position).getUser().getUid(), p);
                                    intent.putParcelableArrayListExtra("receipts", (ArrayList<? extends Parcelable>) x);
                                    startActivity(intent);
                                }

                            });
                        }
                        else{
                            receiptLV.setVisibility(View.INVISIBLE);
                            noReceiptAlertTV.setText("nikt ci nie zalega");
                        }

                    }
                });
            }
        });
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

    @Override
    public void onBackPressed(){
        Intent backIntent = new Intent(GroupActivity.this, MainActivity.class);
        backIntent.putExtra("id", userId);
        startActivity(backIntent);
        finish();
    }


    private List<Receipt> getReceiptsFiltered(boolean isOwed, String uid, List<Payment> payments){ //true = we paid
        List<Receipt> bufList = new ArrayList<Receipt>();

        for(Receipt x : receiptList){
            List<Payment> bufPayList = new ArrayList<Payment>();
            for (Payment y: payments){
                if (y.getReceiptId().equals(x.getId())){
                    bufPayList.add(y);
                }
            }
            x.addPayments(bufPayList);
        }

        if (isOwed){
            for (Receipt x : receiptList){
                if (x.getOwnerId().equals(userId)){
                    for(Payment y : x.getPayments()){
                        if (y.getPaymentTo().equals(uid)){
                            bufList.add(x);
                        }
                    }
                }
                x.dropPayments();
            }
        }
        else{
            for (Receipt x : receiptList){
                if (x.getOwnerId().equals(uid)){
                    for (Payment y : x.getPayments()){
                        if (y.getPaymentTo().equals(userId)){
                            bufList.add(x);
                        }
                    }
                }
                x.dropPayments();
            }
        }
    return bufList;
    }
}

interface GetReceiptsCallBack{
    void onCallback(List<Receipt> list);
}