package com.example.splitpaymentapp.model;

public class Payment {
    private String groupId;
    private String paymentFrom;
    private String paymentTo;
    private float amount;

    public Payment() {
    }

    public Payment(String groupId, String paymentFrom, String paymentTo, float amount) {
        this.groupId = groupId;
        this.paymentFrom = paymentFrom;
        this.paymentTo = paymentTo;
        this.amount = amount;

    }

    public float getAmount(){
        return amount;
    }

    public String getPaymentFrom() {
        return paymentFrom;
    }

    public String getPaymentTo() {
        return paymentTo;
    }

    public String getGroupId() {
        return groupId;
    }
}
