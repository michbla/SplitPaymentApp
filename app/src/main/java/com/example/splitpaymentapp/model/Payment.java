package com.example.splitpaymentapp.model;

public class Payment {
    private String paymentFrom;
    private String paymentTo;
    private float amount;

    public Payment(String paymentFrom, String paymentTo, float amount) {
        this.paymentFrom = paymentFrom;
        this.paymentTo = paymentTo;
        this.amount = amount;

    }
        public float getAmount(){
            return amount;
        }

}
