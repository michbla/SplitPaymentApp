package com.example.splitpaymentapp.model;

import java.util.List;

public interface IDbActions{

     interface IAddUser {
        void onCompleted(User user);
    }

    interface IAddUserToGroup{
         void onSuccess();
         void onFail();
         void onAlreadyInGroup();
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

    interface IBrowsePayments{
         void onCompleted(List<Payment> payments);
    }

    interface IBrowsePaymentsWithinRange{
         void onCompleted(List<Payment> payments);
    }

    interface IBrowseReceipts{
         void onCompleted(List<Receipt> receipts);
    }
}


