package com.example.splitpaymentapp.view;



import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.Payment;
import com.example.splitpaymentapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class PaymentListAdapter extends BaseAdapter {


    private Context context;
    private ArrayList<User> list;
    LayoutInflater inflater;
    protected ArrayList<Float> amounts = new ArrayList<>();

    public PaymentListAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
        for(int i=0;i<list.size();i++){
            amounts.add(0f);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PaymentViewHolder holder;
        convertView = null;
        if (convertView == null){
            holder = new PaymentViewHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.paymentlist_adapter, null);
            holder.editText = (EditText) convertView.findViewById(R.id.amountET);
            holder.textView = (TextView) convertView.findViewById(R.id.nameTV);
            holder.editText.setTag(position);
            holder.textView.setTag(position);
            holder.textView.setText(list.get(position).getFullName());

            convertView.setTag(holder);
        }
        else{
            holder = (PaymentViewHolder) convertView.getTag();
        }

        int tag_position = (Integer) holder.editText.getTag();
        holder.editText.setId(tag_position);
        holder.textView.setId(tag_position);

        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final int position2 = holder.editText.getId();
                final EditText text = holder.editText;
                if (text.getText().toString().length()>0){
                    Log.e("position2", String.valueOf(position2));
                    amounts.set(position2, Float.parseFloat(text.getText().toString()));
                    //TODO pobieranie płatności
                    //TODO sumowanie płatności
                    //TODO wypisywanie należności w groupActivity
                    //TODO dodawanie użytkowników
                    //TODO ??równy podział płatności??
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Toast.makeText(context,holder.editText.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });
        return convertView;
    }

}


