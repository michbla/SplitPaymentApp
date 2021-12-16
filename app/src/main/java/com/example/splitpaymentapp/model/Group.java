package com.example.splitpaymentapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable {
    private String Uid;
    private String groupName;
    private List<User> userList;
    private List<Payment> paymentList;

    public Group(String groupName, User owner) {
        this.groupName = groupName;
        userList = new ArrayList<>();
        this.userList.add(owner);
    }

    public Group(){
        userList = new ArrayList<>();
    };

    public void AddUser(User user){
        userList.add(user);
    }

    public void  AddPayment(Payment payment){
        paymentList.add(payment);
    }

    public String getGroupName() {
        return groupName;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUid(String Uid){
        this.Uid=Uid;
    }

    public String getUid() {
        return Uid;
    }
}
