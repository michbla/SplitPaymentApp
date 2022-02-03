package com.example.splitpaymentapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.DbActions;
import com.example.splitpaymentapp.model.Group;
import com.example.splitpaymentapp.model.IDbActions;
import com.example.splitpaymentapp.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button login, register, createGroup;
    User u;
    TextView s;
    ListView groupListView;
    ArrayList<Group> groupArrayList = new ArrayList<>();
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

        // DbActions.getUserFromDb("p1MXrSYJSEgFb9s8yfzcgYoypi03");
        // u = DbActions.getUserFromDb("p1MXrSYJSEgFb9s8yfzcgYoypi03");
//        if (!Objects.isNull(u))
//            Log.w("Mact",u.getFullName() + " + " + u.getEmail() );
//        else
//            Log.e("huj", "cipa");
        fillGroupList();
        DbActions.getUserFromDb(userId, new IDbActions.IAddUser() {
            @Override
            public void onCompleted(User user) {
                Log.e("xd", user.getFullName());
                user1 = user;
                s.setText("witaj " + user1.getFullName());
            }
        });

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent groupViewIntent = new Intent(MainActivity.this, GroupActivity.class);
                Group g = groupArrayList.get(position);
                groupViewIntent.putExtra("group", g);
                groupViewIntent.putExtra("user", userId);
                startActivity(groupViewIntent);
                finish();
            }
        });

    }


    private void init(){
        login = findViewById(R.id.mActloginButton);
        register = findViewById(R.id.mActRegisterButton);
        s = findViewById(R.id.userNameText);
        createGroup = findViewById(R.id.createGroupButton);
        groupListView = findViewById(R.id.groupListView);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class ));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClickCreateGroup", user1.getUid() + " " + user1.getFullName());
                Intent intent = new Intent(MainActivity.this, CreateGroupActivity.class);
                intent.putExtra("user", (Serializable) user1);
                startActivity(intent);
                finish();
//                DbActions.createGroup(user1, "kaczuchy", new IDbActions.ICreateGroup() {
//                    @Override
//                    public void onCompleted(Group group) {
//                        Toast.makeText(MainActivity.this, "created group with name=" + group.getGroupName(), Toast.LENGTH_SHORT).show();
//                        fillGroupList();
//                    }
//                });
            }
        });
    }

    private void fillGroupList(){
        DbActions.getUserGroups(userId, new IDbActions.IBrowseGroup() {
            @Override
            public void onCompleted(List<Group> groupList) {
                ArrayList<String> groups = new ArrayList<String>();
                for (Group group: groupList){
                groups.add(new StringBuilder(group.getGroupName()).toString());
                groupArrayList.addAll(groupList);
                }
                    ArrayAdapter<String> ad = new ArrayAdapter<String>(
                            MainActivity.this,
                            android.R.layout.simple_list_item_1,
                            groups
                    );
                groupListView.setAdapter(ad);
            }
        });
    }
}