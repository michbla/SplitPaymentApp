package com.example.splitpaymentapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.UserBalance;

import java.util.List;

public class ReportViewAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<UserBalance> balances;

    public ReportViewAdapter(Context context, List<UserBalance> balances) {
        this.context = context;
        this.balances = balances;
    }

    @Override
    public int getCount() {
        return balances.size();
    }

    @Override
    public Object getItem(int position) {
        return balances.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount(){
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ReportViewHolder holder;
        if (convertView == null){
            holder = new ReportViewHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.report_view_adapter, null);
            holder.ReportUserTV = (TextView) convertView.findViewById(R.id.ReportUserTV);
            holder.ReportStateTV = (TextView) convertView.findViewById(R.id.ReportStateTV);
            holder.ReportAmountTV = (TextView) convertView.findViewById(R.id.ReportAmountTV);
            holder.ReportUserTV.setTag(position);
            holder.ReportStateTV.setTag(position);
            holder.ReportAmountTV.setTag(position);

            holder.ReportUserTV.setText(balances.get(position).getUser().getFullName());
            if(balances.get(position).getBalance()<0f){
                holder.ReportStateTV.setText("czeka na");
                holder.ReportAmountTV.setText(String.format("%.2f", Math.abs(balances.get(position).getBalance())));
            }
            else if (balances.get(position).getBalance()==0f){
                holder.ReportStateTV.setText("jest rozliczony");
                holder.ReportAmountTV.setVisibility(View.INVISIBLE);
//                holder.ReportAmountTV.setText("");
            }
            else {
                holder.ReportStateTV.setText("zalega");
                holder.ReportAmountTV.setText(String.format("%.2f",balances.get(position).getBalance()));
            }

        }
        else{
            holder = (ReportViewHolder) convertView.getTag();

        }
        int tagPos = (Integer) holder.ReportUserTV.getTag();
        holder.ReportUserTV.setId(tagPos);
        holder.ReportStateTV.setId(tagPos);
        holder.ReportAmountTV.setId(tagPos);
        return convertView;
    }




}
