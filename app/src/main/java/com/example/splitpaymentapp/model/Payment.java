package com.example.splitpaymentapp.model;

class Payment {
    private User paymentFrom;
    private User paymentTo;
    private float amount;

    public Payment(User paymentFrom, User paymentTo, float amount) {
        this.paymentFrom = paymentFrom;
        this.paymentTo = paymentTo;
        this.amount = amount;

    }
        public float getAmount(){
            return amount;
        }

}
