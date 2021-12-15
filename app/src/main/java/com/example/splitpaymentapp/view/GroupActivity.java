package com.example.splitpaymentapp.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.Group;

import java.io.Serializable;

public class GroupActivity extends AppCompatActivity {

    Group group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        /*try{
            group = (Group) getIntent().getSerializableExtra("group");
            Log.e("GroupSuccess", "fetched group with name: " + group.getGroupName());
        }
        catch (Exception e){
            Log.e("GroupFatal", "failed to fetch Group");
        }*/

        if (getIntent().getExtras()!=null){
            group = (Group) getIntent().getSerializableExtra("group");
        }
        else Log.e("GroupFatal", "failed to fetch Group");
        Log.e("GroupSuccess", "fetched group with name: " + group.getGroupName());
    }
}