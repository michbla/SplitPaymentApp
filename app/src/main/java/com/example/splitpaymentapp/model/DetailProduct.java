package com.example.splitpaymentapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class DetailProduct implements Parcelable {

    String product;
    Float value;

    public DetailProduct(String product, Float value) {
        this.product = product;
        this.value = value;
    }

    public DetailProduct() {
    }

    protected DetailProduct(Parcel in) {
        product = in.readString();
        if (in.readByte() == 0) {
            value = null;
        } else {
            value = in.readFloat();
        }
    }

    public static final Creator<DetailProduct> CREATOR = new Creator<DetailProduct>() {
        @Override
        public DetailProduct createFromParcel(Parcel in) {
            return new DetailProduct(in);
        }

        @Override
        public DetailProduct[] newArray(int size) {
            return new DetailProduct[size];
        }
    };

    public String getProduct() {
        return product;
    }

    public Float getValue() {
        return value;
    }

    public Pair<String, Float> toPair(){
        return new Pair<String,Float>(getProduct(), getValue());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(product);
        if (value == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(value);
        }
    }
}
