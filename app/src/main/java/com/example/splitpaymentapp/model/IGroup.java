package com.example.splitpaymentapp.model;

import com.example.splitpaymentapp.model.Payment;
import com.example.splitpaymentapp.model.User;

public interface IGroup {

    public void AddUser(User user);
    public void AddPayment(Payment payment);


}
