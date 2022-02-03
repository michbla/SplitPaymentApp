package com.example.splitpaymentapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable {
    private String Uid;
    private String groupName;
    private String groupOwner;
    private int isFinished;
    private List<User> userList;
    private List<Payment> paymentList;


    public Group(String groupName, String groupOwner, int isFinished, User owner) {
        this.groupName = groupName;
        this.groupOwner = groupOwner;
        this.isFinished = isFinished;
        userList = new ArrayList<>();
        this.userList.add(owner);

    }

    public Group(String Uid, String groupName, String groupOwner, int isFinished, User owner) {
        this.Uid = Uid;
        this.groupName = groupName;
        this.groupOwner = groupOwner;
        this.isFinished = isFinished;
        userList = new ArrayList<>();
//        this.userList.add(owner);

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

    public String getGroupOwner() {
        return groupOwner;
    }

    public int getIsFinished() {
        return isFinished;
    }

    public void setFinished() {
        isFinished = 1;
        DbActions.setGroupFinished(getUid());
    }
}
