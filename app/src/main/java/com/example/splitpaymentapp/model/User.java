package com.example.splitpaymentapp.model;

public class User {
     private String Uid;
    private String fullName;
    private String email;
    private String password;


    public User() {
        Uid="";
        email="";
        fullName="";
    }

    public User(String uid, String fullName, String email ) {
        Uid = uid;
        this.email = email;
        this.fullName = fullName;

    }

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
 }


