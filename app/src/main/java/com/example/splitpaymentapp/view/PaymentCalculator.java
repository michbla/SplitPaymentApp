package com.example.splitpaymentapp.view;

import android.util.Log;
import android.util.Pair;

import com.example.splitpaymentapp.model.DbActions;
import com.example.splitpaymentapp.model.IDbActions;
import com.example.splitpaymentapp.model.Payment;
import com.example.splitpaymentapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PaymentCalculator {

    private List<Payment> paymentList = new ArrayList<Payment>();
    private List<User> users;
    private String groupId;
    private String userId;
    Map<User, Float> map;
    List<Pair<User, Float>> s = new ArrayList<>();

    public PaymentCalculator(List<User> users, String groupId, String userId) {
        this.users = users;
        this.groupId = groupId;
        this.userId = userId;
//        for (int i=0;i< users.size();i++){
//            map.put(users.get(i), 0f);
//        }
        MapPayments(new IPaymentCalculator.IMapPayments() {
            @Override
            public void onCompleted(List<Pair<User, Float>> map) {
                Log.e("xddd", String.valueOf(( map.size())));
                for (int i=0;i< s.size();i++){
                    Log.e("xdd", map.get(i).first.getFullName() + " " + map.get(i).second.toString());
                }
            }
        });

    }


    public void MapPayments(IPaymentCalculator.IMapPayments Listener){
        List<Pair<User, Float>> ss = new ArrayList<>();
        DbActions.getPaymentsFromDb(groupId, new IDbActions.IBrowsePayments() {

            @Override
            public void onCompleted(List<Payment> payments) {
                float amountBuf = 0;
                paymentList.addAll(payments);
                for (int i=0;i<users.size();i++){
                    amountBuf = 0;
                    String currUserId = users.get(i).getUid();
                    if (currUserId.equals(userId)) {
                        users.remove(i);
                        i--;
                    }
                    for (int j=0;j<payments.size();j++){
                        Payment buf = payments.get(j);
                         if (currUserId.equals(buf.getPaymentFrom()) || currUserId.equals(buf.getPaymentTo())){
                             if (userId.equals(buf.getPaymentTo())){
                                 amountBuf +=buf.getAmount();
                             }
                             else if (userId.equals(buf.getPaymentFrom())){
                                 amountBuf -=buf.getAmount();
                             }
                         }
                    }
                    Pair<User, Float> f = new Pair<User, Float>(users.get(i), amountBuf);
                    s.add(f);
                    Log.e("xdd", s.get(i).first.getFullName() + " " + s.get(i).second.toString());


                }
                Listener.onCompleted(s);
            }
        });
    }


}

interface IPaymentCalculator{
    interface IMapPayments{
        void onCompleted(List<Pair<User, Float>> map);
    }
}
