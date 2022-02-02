package com.example.splitpaymentapp.model;

public class UserBalance {
    private User user;
    private float balance;

    public UserBalance(User user) {
        this.user = user;
        this.balance = 0f;
    }

    public User getUser() {
        return user;
    }

    public float getBalance() {
        return balance;
    }

    public void addBalance(float amount){
        this.balance+=amount;
    }

    public void subBalance(float amount){
        this.balance-=amount;
    }
}
