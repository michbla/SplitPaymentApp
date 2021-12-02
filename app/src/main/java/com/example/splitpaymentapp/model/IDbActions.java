package com.example.splitpaymentapp.model;

public interface IDbActions{

     interface IAddUser {
        void onCompleted(User user);
    }

     interface IRegisterUser{
        void onCompleted(User user);
    }

    interface ILoginUser{
         void onCompleted(User user);
    }

    interface ICreateGroup{
         void onCompleted(Group group);
    }
}


