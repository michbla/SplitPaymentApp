package com.example.splitpaymentapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Receipt implements Serializable {
    private String name;
    private String ownerId;
    private String date;
    private String id;
    private String groupId;
    private float amount;
    private List<String> paymentIds;
    private List<Payment> payments;

    public Receipt(){
        payments = new ArrayList<Payment>();
    }

    public Receipt( List<String> payments,String id, String groupId, float amount, String name, String ownerId, String date) {
        this.id = id;
        this.groupId = groupId;
        this.amount = amount;
        this.name = name;
        this.ownerId = ownerId;
        this.date = date;
        this.payments = new ArrayList<Payment>();
        paymentIds = new ArrayList<String>();
        for (int i=0;i<payments.size();i++) {
            paymentIds.add(payments.get(i));
        }
    }

    public Receipt(String id, String groupId, float amount, List<Payment> payments, String name, String ownerId, String date) {
        this.id = id;
        this.groupId = groupId;
        this.amount = amount;
        this.name = name;
        this.ownerId = ownerId;
        this.date = date;
        paymentIds = new ArrayList<String>();
        this.payments = new ArrayList<Payment>();
        for (int i=0;i<payments.size();i++) {
            paymentIds.add(payments.get(i).getPaymentId());
            payments.add(payments.get(i));
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

    public String getOwnerId() {
        return ownerId;
    }

    public List<Payment> getPayments(){
        return payments;
    }

    public void addPayments(List<Payment> payments){
        this.payments.addAll(payments);
    }
}


