package com.example.splitpaymentapp.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.DbActions;
import com.example.splitpaymentapp.model.Group;
import com.example.splitpaymentapp.model.IDbActions;
import com.example.splitpaymentapp.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    ArrayList<User> users = new ArrayList<>();
    Group group;
    ListView usersLV;
    FloatingActionButton addExpenseButton;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        init();
        try{
            group = (Group) getIntent().getSerializableExtra("group");
            Log.e("GroupSuccess", "fetched group with name: " + group.getGroupName());
        }
        catch (Exception e){
            Log.e("GroupFatal", "failed to fetch Group");
        }
        fillUserList();

        addExpenseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent paymentIntent = new Intent(GroupActivity.this, PaymentActivity.class);
                        Bundle bundle = new Bundle();
                        paymentIntent.putParcelableArrayListExtra("u", users);
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
                    Log.e("groupActivity", u.getFullName());
                }
                ArrayAdapter<String> ad = new ArrayAdapter<>(
                        GroupActivity.this,
                        android.R.layout.simple_list_item_1,
                        userString
                );
                usersLV.setAdapter(ad);
            }
        });

    }

    private void init(){
        usersLV = findViewById(R.id.usersListView);
        addExpenseButton = findViewById(R.id.addExpenseFloatingButton);
    }


}