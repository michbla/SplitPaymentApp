package com.example.splitpaymentapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.DbActions;
import com.example.splitpaymentapp.model.Group;
import com.example.splitpaymentapp.model.IDbActions;
import com.example.splitpaymentapp.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    ArrayList<User> users = new ArrayList<>();
    Group group;
    ListView usersLV;
    FloatingActionButton addExpenseButton;
    String userId;
    GroupListAdapter adapter;

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
                PaymentCalculator calc = new PaymentCalculator(users, group.getUid(), userId);
                calc.MapPayments(new IPaymentCalculator.IMapPayments() {
                    @Override
                    public void onCompleted(List<Pair<User, Float>> map) {
                        adapter = new GroupListAdapter(GroupActivity.this, users, map);
                        usersLV.setAdapter(adapter);
                    }
                });

            }
        });


    }

    private void init(){
        usersLV = findViewById(R.id.usersListView);
        addExpenseButton = findViewById(R.id.addExpenseFloatingButton);
    }


}