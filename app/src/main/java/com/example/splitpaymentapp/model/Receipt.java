package com.example.splitpaymentapp.model;

import com.example.splitpaymentapp.R;

import java.util.ArrayList;
import java.util.List;

public class Receipt {
    private String name;
    private String date;
    private String id;
    private String groupId;
    private float amount;
    private List<String> paymentIds;

    public Receipt(){

    }

    public Receipt(String id, String groupId, float amount, List<Payment> payments, String name, String date) {
        this.id = id;
        this.groupId = groupId;
        this.amount = amount;
        this.name = name;
        this.date = date;
        paymentIds = new ArrayList<String>();
        for (int i=0;i<payments.size();i++) {
            paymentIds.add(payments.get(i).getPaymentId());
        }
    }

    public String getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    public float getAmount() {
        return amount;
    }

    public List<String> getPaymentIds() {
        return paymentIds;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
}


