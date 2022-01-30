package com.example.splitpaymentapp.view;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.Payment;
import com.example.splitpaymentapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class ReceiptDetailsAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater inflater;
    List<Payment> payments = new ArrayList<Payment>();
    List<User> users = new ArrayList<User>();

    public ReceiptDetailsAdapter(Context context, List<Payment> payments, List<User> users) {
        this.context = context;
        this.payments = payments;
        this.users = users;

    }

    private String getUserNameById(String id){
        for(User x: users){
            if (x.getUid().equals(id))
                return x.getFullName();
        }
        return "not found";
    }

    @Override
    public int getCount() {
        return payments.size();
    }

    @Override
    public Object getItem(int position) {
        return payments.get(position);
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
            holder.userTV.setText(getUserNameById(payments.get(position).getPaymentTo()));
            holder.amountTV.setText(String.valueOf(payments.get(position).getAmount()));
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
