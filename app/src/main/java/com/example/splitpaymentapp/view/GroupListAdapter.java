package com.example.splitpaymentapp.view;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class GroupListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<User> users;
    LayoutInflater inflater;
    protected ArrayList<Float> amounts = new ArrayList<>();
    protected List<Pair<User, Float>> map;

    public GroupListAdapter(Context context, ArrayList<User> users, List<Pair<User, Float>> map) {
        this.context = context;
        this.users = users;
        for(int i=0;i<users.size();i++){
            amounts.add(0f);
        }
        this.map = map;
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
        final GroupViewHolder holder;
        convertView = null;
        if (convertView == null){
            holder = new GroupViewHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grouplist_adapter, null);
            holder.userTV = (TextView) convertView.findViewById(R.id.groupUsernameTV);
            holder.amountTV = (TextView) convertView.findViewById(R.id.groupAmountTV);
            holder.userTV.setTag(position);
            holder.amountTV.setTag(position);
            holder.userTV.setText(users.get(position).getFullName());
            holder.amountTV.setText(map.get(position).second.toString());
        }
        else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        int tag_position = (Integer) holder.userTV.getTag();
        holder.userTV.setId(tag_position);
        holder.amountTV.setId(tag_position);
        return convertView;
    }
}
