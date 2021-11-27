package com.example.splitpaymentapp.model;

import java.util.List;

class Group implements IGroup {
    private String groupName;
    private List<User> userList;
    private List<Payment> paymentList;

    public Group(String groupName, User owner) {
        this.groupName = groupName;
        this.userList.add(owner);
    }

    public void AddUser(User user){
        userList.add(user);
    }

    public void  AddPayment(Payment payment){
        paymentList.add(payment);
    }


}
