package com.example.splitpaymentapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class User implements Parcelable {
    private String Uid;
    private String fullName;
    private String email;
    private String password;


    public User() {
        Uid="";
        email="";
        fullName="";
        password="";
    }

    public User(String uid, String fullName, String email ) {
        Uid = uid;
        this.email = email;
        this.fullName = fullName;

    }

    protected User(Parcel in) {
        Uid = in.readString();
        fullName = in.readString();
        email = in.readString();
        password = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

     public String getUid() {
         return Uid;
     }

     public void setFullName(String _fullname){
        fullName = _fullname;
     }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Uid);
        dest.writeString(fullName);
        dest.writeString(email);
        dest.writeString(password);
    }
}


