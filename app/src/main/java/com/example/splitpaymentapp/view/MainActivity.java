package com.example.splitpaymentapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.Controller;
import com.example.splitpaymentapp.model.Group;
import com.example.splitpaymentapp.model.IDbActions;
import com.example.splitpaymentapp.model.User;
import com.example.splitpaymentapp.model.IDbActions;

public class MainActivity extends AppCompatActivity {

    Button login, register, createGroup;
    User u;
    TextView s;

    User user1 = new User();
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle id = getIntent().getExtras();
        try{
            userId = id.getString("id");
            Log.e("MainSuccess", "fetched id: " + userId);
        }
        catch (Exception e){
            Log.e("MainFatal", "failed to fetch Uid");
        }
        init();

        // Controller.getUserFromDb("p1MXrSYJSEgFb9s8yfzcgYoypi03");
        // u = Controller.getUserFromDb("p1MXrSYJSEgFb9s8yfzcgYoypi03");
//        if (!Objects.isNull(u))
//            Log.w("Mact",u.getFullName() + " + " + u.getEmail() );
//        else
//            Log.e("huj", "cipa");

        Controller.getUserFromDb(userId, new IDbActions.IAddUser() {
            @Override
            public void onCompleted(User user) {
                Log.e("xd", user.getFullName());
                user1 = user;
                s.setText("witaj " + user1.getFullName());
            }
        });

    }


    private void init(){
        login = findViewById(R.id.mActloginButton);
        register = findViewById(R.id.mActRegisterButton);
        s = findViewById(R.id.userNameText);
        createGroup = findViewById(R.id.createGroupButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class ));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClickCreateGroup", user1.getUid() + " " + user1.getFullName());
                Controller.createGroup(user1, "kaczuchy", new IDbActions.ICreateGroup() {
                    @Override
                    public void onCompleted(Group group) {
                        Toast.makeText(MainActivity.this, "created group with name=" + group.getGroupName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}