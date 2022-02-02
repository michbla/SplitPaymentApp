package com.example.splitpaymentapp.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {
    private List<Receipt> receipts;
    private List<Payment> paymentsList;
    private List<UserBalance> userBalances;


    public ReportGenerator(List<Receipt> receipts, List<User> users, String userId) {
        this.receipts = receipts;
        paymentsList = new ArrayList<>();
        userBalances = new ArrayList<>();
        for(User u : users){
            if (!u.getUid().equals(userId))
            userBalances.add(new UserBalance(u));
        }
        List<String> rIds = new ArrayList<>();
        for (Receipt r: this.receipts){
            rIds.add(r.getId());
        }
        DbActions.browsePaymentsWithinRange(rIds ,new IDbActions.IBrowsePaymentsWithinRange() {
            @Override
            public void onCompleted(List<Payment> payments) {
                paymentsList.addAll(payments);
//                Log.e("xd", String.valueOf(paymentsList.size()));
                addPaymentsToUsers();
            }
        });

    }

    private void addPaymentsToUsers(){
        for(Payment x:paymentsList){
            for(UserBalance z: userBalances){
                String Uid = z.getUser().getUid();
                String payee = x.getPaymentTo();
                String payer = x.getPaymentFrom();
                if(Uid.equals(payee)){
                    z.addBalance(x.getAmount());
                }
                else if(Uid.equals(payer)){
                    z.subBalance(x.getAmount());
                }
            }
        }
        for(UserBalance u: userBalances){
            Log.e(u.getUser().getFullName(), String.valueOf(u.getBalance()));
        }
    }

}
