package com.example.splitpaymentapp.model;

public class Payment {
    private String receiptId;
    private String paymentId;
    private String paymentFrom;
    private String paymentTo;
    private float amount;

    public Payment() {
    }

    public Payment(String receiptId, String paymentId, String paymentFrom, String paymentTo, float amount) {
        this.receiptId = receiptId;
        this.paymentId = paymentId;
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

    public String getReceiptId() { return receiptId; }

    public String getPaymentId() {
        return paymentId;
    }
}
