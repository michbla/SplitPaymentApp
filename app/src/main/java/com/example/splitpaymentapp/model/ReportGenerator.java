package com.example.splitpaymentapp.model;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {
    private List<Receipt> receipts;
    private List<Payment> paymentsList;
    private List<UserBalance> userBalances;
    private String userId;

    public ReportGenerator(List<Receipt> receipts, List<User> users, String userId, IReportGenerated IReportGenerated) {
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
        this.userId = userId;
        DbActions.browsePaymentsWithinRange(rIds ,new IDbActions.IBrowsePaymentsWithinRange() {
            @Override
            public void onCompleted(List<Payment> payments) {
                paymentsList.addAll(payments);
//                Log.e("xd", String.valueOf(paymentsList.size()));
                addPaymentsToUsers();
                IReportGenerated.onGenerated(userBalances);
            }
        });

    }

    private void addPaymentsToUsers(){
        for (Receipt r : receipts){
            List<String> ids = r.getPaymentIds();
            List<Payment> bufPays = new ArrayList<>();
            for(String s:ids){
                bufPays.add(getPaymentById(s));
            }
            if (r.getOwnerId().equals(userId)) { //dodajemy na konta innych
                for (Payment p : bufPays) {
                    for(UserBalance u: userBalances){
                        try {
                            if (p.getPaymentTo().equals(u.getUser().getUid())){
                                u.addBalance(p.getAmount());
                            }
                        }
                        catch (Exception e){
                            Log.e("payment not found", "xd");
                        }

                    }
                }
            }

            else { //odejmujemy z kont innych
                for (Payment p : bufPays) {
                    for(UserBalance u: userBalances){
                        try {
                            if (p.getPaymentTo().equals(userId) && p.getPaymentFrom().equals(u.getUser().getUid())){
                                u.subBalance(p.getAmount());
                            }
                        }
                        catch (Exception e){
                            Log.e("payment not found", "xd");
                        }
                    }
                }
            }
            bufPays.clear();
        }
    }
//todo debug that
    public void writeReport(){
        for(UserBalance u: userBalances){
            Log.e(u.getUser().getFullName(), String.valueOf(u.getBalance()));
        }
    }


    private Payment getPaymentById(String id){
        for (Payment x : paymentsList){
            if (x.getPaymentId().equals(id))
                return x;
        }
        return null;
    }
}


