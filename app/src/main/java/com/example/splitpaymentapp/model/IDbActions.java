package com.example.splitpaymentapp.model;

import java.util.List;

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

    interface IBrowseGroup{
         void onCompleted(List<Group> groupList);
    }

    interface IBrowseUsers{
         void onCompleted(List<User> userList);
    }
}


