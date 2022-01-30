package com.example.splitpaymentapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Payment implements Parcelable {
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

    protected Payment(Parcel in) {
        receiptId = in.readString();
        paymentId = in.readString();
        paymentFrom = in.readString();
        paymentTo = in.readString();
        amount = in.readFloat();
    }

    public static final Creator<Payment> CREATOR = new Creator<Payment>() {
        @Override
        public Payment createFromParcel(Parcel in) {
            return new Payment(in);
        }

        @Override
        public Payment[] newArray(int size) {
            return new Payment[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(paymentFrom);
        dest.writeString(paymentTo);
        dest.writeString(paymentId);
        dest.writeFloat(amount);
    }
}
