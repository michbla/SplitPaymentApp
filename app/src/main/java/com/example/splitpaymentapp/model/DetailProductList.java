package com.example.splitpaymentapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class DetailProductList implements Parcelable {

    public ArrayList<DetailProduct> list;

    public DetailProductList(ArrayList<DetailProduct> list) {
        list = new ArrayList<DetailProduct>();
        this.list = list;
    }

    public DetailProductList() {
        list = new ArrayList<DetailProduct>();
    }


    protected DetailProductList(Parcel in) {
        list = in.createTypedArrayList(DetailProduct.CREATOR);
    }

    public static final Creator<DetailProductList> CREATOR = new Creator<DetailProductList>() {
        @Override
        public DetailProductList createFromParcel(Parcel in) {
            return new DetailProductList(in);
        }

        @Override
        public DetailProductList[] newArray(int size) {
            return new DetailProductList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(list);
    }
}
