package com.example.splitpaymentapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Payment implements Parcelable {
    private String receiptId;
    private String paymentId;
    private String paymentFrom;
    private String paymentTo;
    private float amount;
    private List<DetailProduct> details;

    public Payment() {
    }

    public List<DetailProduct> getDetails() {
        return details;
    }

    public Payment(String receiptId, String paymentId, String paymentFrom, String paymentTo, float amount) {
        this.receiptId = receiptId;
        this.paymentId = paymentId;
        this.paymentFrom = paymentFrom;
        this.paymentTo = paymentTo;
        this.amount = amount;
        this.details = new ArrayList<>();

    }
    public Payment(String receiptId, String paymentId, String paymentFrom, String paymentTo, float amount, List<DetailProduct> details) {
        this.receiptId = receiptId;
        this.paymentId = paymentId;
        this.paymentFrom = paymentFrom;
        this.paymentTo = paymentTo;
        this.amount = amount;
        this.details = new ArrayList<>();
        this.details.addAll(details);

    }

    protected Payment(Parcel in) {
        receiptId = in.readString();
        paymentId = in.readString();
        paymentFrom = in.readString();
        paymentTo = in.readString();
        amount = in.readFloat();
        in.readTypedList(details, DetailProduct.CREATOR);
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
        dest.writeTypedList(details);
    }
}
