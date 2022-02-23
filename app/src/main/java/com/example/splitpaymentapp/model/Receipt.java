package com.example.splitpaymentapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Receipt implements Serializable, Parcelable {
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

    protected Receipt(Parcel in) {
        name = in.readString();
        ownerId = in.readString();
        date = in.readString();
        id = in.readString();
        groupId = in.readString();
        amount = in.readFloat();
        paymentIds = in.createStringArrayList();
        payments = in.createTypedArrayList(Payment.CREATOR);
    }

    public static final Creator<Receipt> CREATOR = new Creator<Receipt>() {
        @Override
        public Receipt createFromParcel(Parcel in) {
            return new Receipt(in);
        }

        @Override
        public Receipt[] newArray(int size) {
            return new Receipt[size];
        }
    };

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

    public void dropPayments(){
        this.payments.clear();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(ownerId);
        dest.writeString(date);
        dest.writeString(id);
        dest.writeString(groupId);
        dest.writeFloat(amount);
        dest.writeStringList(paymentIds);
        dest.writeTypedList(payments);
    }
}


