package com.example.splitpaymentapp.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.User;

import java.util.ArrayList;

public class DetailedPayAdapter extends BaseAdapter
{

    private Context context;
    private LayoutInflater inflater;
    ArrayList<User> users;
    ArrayList<Float> subs;
    float amount;

    public DetailedPayAdapter(Context context, ArrayList<User> users, float amount, ArrayList<Float> subs) {
        this.context = context;
        this.users = users;
        this.amount = amount;
        this.subs = subs;
    }

    public void updateSubs(float val, int z){

        this.subs.set(z, val);
        Log.e("2subs2", "XD");
        for (Float x: subs){
            Log.e("2subs2", String.valueOf(x));
        }
        this.notifyDataSetChanged();
        Log.e("2subs2", "XD");
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ReceiptDetailsHolder holder;
        convertView=null;
        if (convertView==null){
            holder = new ReceiptDetailsHolder(); //są takie same
            inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.receiptdatails_adapter, null);
            holder.userTV = (TextView) convertView.findViewById(R.id.groupUsernameTV);
            holder.amountTV = (TextView) convertView.findViewById(R.id.groupAmountTV);
            holder.userTV.setTag(position);
            holder.amountTV.setTag(position);
            holder.userTV.setText(users.get(position).getFullName());
            holder.amountTV.setText(String.valueOf(subs.get(position)));
            convertView.setTag(holder);

        }
        else{
            holder = (ReceiptDetailsHolder) convertView.getTag();

        }
        int tagPos = (Integer) holder.userTV.getTag();
        holder.userTV.setId(tagPos);
        holder.amountTV.setId(tagPos);

        return convertView;
    }
}
