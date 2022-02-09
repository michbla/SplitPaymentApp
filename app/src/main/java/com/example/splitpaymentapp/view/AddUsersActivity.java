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
import com.example.splitpaymentapp.model.DbActions;
import com.example.splitpaymentapp.model.Group;
import com.example.splitpaymentapp.model.IDbActions;
import com.example.splitpaymentapp.model.User;

import java.io.Serializable;

public class AddUsersActivity extends AppCompatActivity {

    Group group;
    String mail;
    Button addUserBtn, goToGroupBtn;
    EditText setMail;
    String  user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users);
        group =(Group) getIntent().getSerializableExtra("group");
        user = (String) getIntent().getSerializableExtra("user");
        init();

        goToGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddUsersActivity.this, GroupActivity.class);
                intent.putExtra("group", group);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("xD", "XDD");
                mail = setMail.getText().toString().trim();
                if (mail.isEmpty()){
                    setMail.setError("pierw podaj mail członka");
                }
                else{
                    DbActions.addUserToGroup(mail, group.getUid(), new IDbActions.IAddUserToGroup() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(AddUsersActivity.this, "dodano do grupy", Toast.LENGTH_LONG).show();
                            setMail.setText("");
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(AddUsersActivity.this, "podany użytkownik nie istnieje", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAlreadyInGroup() {
                            Toast.makeText(AddUsersActivity.this, "członek już jest w grupie", Toast.LENGTH_LONG).show();
                            setMail.setText("");
                        }
                    });
                }

            }
        });
    }


    private void init(){
        addUserBtn = (Button) findViewById(R.id.addUserBtn);
        goToGroupBtn = (Button) findViewById(R.id.goToGroupBtn);
        setMail = (EditText) findViewById(R.id.AddEmailET);
    }
}