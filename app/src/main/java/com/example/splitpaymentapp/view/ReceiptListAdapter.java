package com.example.splitpaymentapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.Receipt;

import java.util.ArrayList;
import java.util.List;

public class ReceiptListAdapter extends BaseAdapter {

    private Context context;
    private List<Receipt> receipts = new ArrayList<Receipt>();
    LayoutInflater inflater;

    public ReceiptListAdapter(Context context, List<Receipt> receipts) {
        this.context = context;
        this.receipts = receipts;
    }

    @Override
    public int getCount() {
        return receipts.size();
    }

    @Override
    public Object getItem(int position) {
        return receipts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ReceiptListHolder holder;
        if (convertView == null){
            holder = new ReceiptListHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.receiptlist_adapter, null);
            holder.rcName = (TextView) convertView.findViewById(R.id.rcNameTV);
            holder.rcDate = (TextView) convertView.findViewById(R.id.rcDateTV);
            holder.rcValue = (TextView) convertView.findViewById(R.id.rcValueTV);
            holder.rcName.setTag(position);
            holder.rcDate.setTag(position);
            holder.rcValue.setTag(position);

            holder.rcName.setText(receipts.get(position).getName());
            holder.rcDate.setText(receipts.get(position).getDate());
            holder.rcValue.setText(String.valueOf(receipts.get(position).getAmount()));

            convertView.setTag(holder);
        }
        else {
            holder = (ReceiptListHolder) convertView.getTag();
        }

        int tag_position = (Integer) holder.rcName.getTag();
        holder.rcName.setId(tag_position);
        holder.rcDate.setId(tag_position);
        holder.rcValue.setId(tag_position);
        return convertView;
    }
}
