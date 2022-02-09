package com.example.splitpaymentapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class CreateGroupActivity extends AppCompatActivity {

    private User user;
    private EditText nameET;
    private Button addBtn;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        user = getIntent().getParcelableExtra("user");
        init();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameET.getText().toString();
                if(!name.isEmpty()) {
                    DbActions.createGroup(user, name, new IDbActions.ICreateGroup() {
                        @Override
                        public void onCompleted(Group group) {
                            Toast.makeText(CreateGroupActivity.this, "grupa " + name + " założona", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(CreateGroupActivity.this,AddUsersActivity.class);
                            intent.putExtra("user", user.getUid());
                            intent.putExtra("group", group);
                            startActivity(intent);
                        }
                    });
                }
                else{
                    nameET.setError("pierw podaj nazwę");
                }
            }
        });
    }

    private void init(){
        nameET = (EditText) findViewById(R.id.GroupNameET);
        addBtn = (Button) findViewById(R.id.CreateGroupBtn);
    }
}