package com.example.splitpaymentapp.view;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.DetailProduct;
import com.example.splitpaymentapp.model.Payment;
import com.example.splitpaymentapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class ReceiptDetailsAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater inflater;
    List<Payment> payments = new ArrayList<Payment>();
    List<DetailProduct> paymentDets = new ArrayList<DetailProduct>();
    List<User> users = new ArrayList<User>();
    Payment singlePay;
    int mode = 0;

    public ReceiptDetailsAdapter(Context context, List<Payment> payments, List<User> users) {
        this.context = context;
        this.payments = payments;
        this.users = users;
        mode = 0;
//ndot - owe
    }
    public ReceiptDetailsAdapter(Context context, List<DetailProduct> payments, List<User> users, boolean x) {
        this.context = context;
        this.paymentDets = payments;
        this.users = users;
        this.mode = 1;
//det
    }

    public ReceiptDetailsAdapter(Context context, Payment payment, List<User> users) {
        this.context = context;
        this.singlePay = payment;
        this.users = users;
        this.mode = 2;
//og
    }



    @Override
    public int getCount() {
        if (mode == 0)
            return payments.size();
        else if (mode == 1)
            return paymentDets.size();
        else return 1;
    }


    @Override
    public Object getItem(int position) {
        if (mode == 0)
            return payments.get(position);
        else if (mode == 1)
            return paymentDets.get(position);
        else return singlePay;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ReceiptDetailsHolder holder;
        convertView = null;
        if (convertView == null){
            holder = new ReceiptDetailsHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.receiptdatails_adapter, null);
            holder.userTV = (TextView) convertView.findViewById(R.id.groupUsernameTV);
            holder.amountTV = (TextView) convertView.findViewById(R.id.groupAmountTV);
            holder.userTV.setTag(position);
            holder.amountTV.setTag(position);
            if(mode == 0) {
                holder.userTV.setText(ReceiptDetailsActivity.getUserNameById(payments.get(position).getPaymentTo(), users));
                holder.amountTV.setText(String.valueOf(payments.get(position).getAmount()));
            }
            if (mode == 1){
                holder.userTV.setText(paymentDets.get(position).getProduct());
                holder.amountTV.setText(String.valueOf(paymentDets.get(position).getValue()));
            }
            if (mode == 2){
                holder.userTV.setText(ReceiptDetailsActivity.getUserNameById(singlePay.getPaymentFrom(), users));
                holder.amountTV.setText(String.valueOf(singlePay.getAmount()));
            }
        }
        else {
            holder = (ReceiptDetailsHolder) convertView.getTag();
        }
        int tag_position = (Integer) holder.userTV.getTag();
        holder.userTV.setId(tag_position);
        holder.amountTV.setId(tag_position);
        return convertView;
    }
}
