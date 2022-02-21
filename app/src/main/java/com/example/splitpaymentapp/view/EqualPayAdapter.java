package com.example.splitpaymentapp.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.User;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;

public class EqualPayAdapter extends BaseAdapter {

    private Context context;
    ArrayList<User> users;
    LayoutInflater inflater;
    Float amount;
    int usersInList;
    int usersInListChanged;
    ArrayList<Float> subAmounts = new ArrayList<>();
    ArrayList<Boolean> isEnabled = new ArrayList<>();
    ArrayList<Boolean> isChanged = new ArrayList<>();
    Float changedAmount;
    private boolean firstLoad = true;
    public EqualPayAdapter(Context context, ArrayList<User> users, Float amount) {
        this.users = users;
        this.context = context;
        this.amount = amount;
        usersInList = users.size();
        usersInListChanged = users.size();
        changedAmount = amount;
        for (int i=0;i<usersInList;i++) {
            isEnabled.add(true);
            isChanged.add(false);
            BigDecimal a = new BigDecimal(amount/usersInList);
            BigDecimal b = a.setScale(2, BigDecimal.ROUND_DOWN);
            subAmounts.add(b.floatValue());
            Log.e("subAm", b.toString());
        }
        float sumBuf = 0;
        for (int i=0;i<usersInList;i++){
            sumBuf += subAmounts.get(i);
        }
        if (sumBuf!=amount){
            subAmounts.set(0, (float) (subAmounts.get(0)+(amount-sumBuf)));
        }
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
        final EqualPayHolder holder;
        convertView = null;
        if (convertView == null){
            holder = new EqualPayHolder();
            inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.equalpay_adapter, null);
            holder.amountTV = (EditText) convertView.findViewById(R.id.ePayETL);
            holder.userTV = (TextView) convertView.findViewById(R.id.ePayTVL);
            holder.isPaying = (CheckBox) convertView.findViewById(R.id.isPayingCB);
            holder.amountTV.setTag(position);
            holder.isPaying.setTag(position);
            holder.userTV.setTag(position);
            if(isEnabled.get(position)){
                holder.isPaying.setChecked(true);
            }else
                holder.amountTV.setEnabled(false);
            holder.userTV.setText(users.get(position).getFullName());
            String amountbuf = String.format("%.2f", subAmounts.get(position));
            holder.amountTV.setText(amountbuf);



            holder.amountTV.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event != null &&
                                    event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        if (event == null || !event.isShiftPressed()) {
                            // the user is done typing.
                            usersInListChanged--;
                            String am = holder.amountTV.getText().toString();
                            calculateOnModifiedField(position, am);
                            return true; // consume.
                        }

                    }return false;
                }
            });
        }
        else
        {
            holder = (EqualPayHolder) convertView.getTag();
        }
        int tagPos = (Integer) holder.userTV.getTag();
        holder.userTV.setId(tagPos);
        holder.amountTV.setId(tagPos);
        holder.isPaying.setId(tagPos);

        holder.isPaying.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    usersInList--;
                    usersInListChanged--;
                    isChanged.set(position, true);
                    holder.amountTV.setText(String.valueOf(0.00));
                    holder.amountTV.setFocusable(false);
                    isEnabled.set(position, false);
                    updateAdapter(position);
                }
                if(isChecked){
                    usersInList++;
                    usersInListChanged++;
                    isChanged.set(position, false);
                    holder.amountTV.setText(String.valueOf(amount/usersInList));
                    holder.amountTV.setFocusableInTouchMode(true);
                    isEnabled.set(position, true);
                    updateAdapter(-1);
                }

            }
        });

        return convertView;
    }

    private void calculateOnModifiedField(int position, String amount) {
        float buf = 0;
        try{
             buf = Float.parseFloat(amount);
            Log.e("DX", "xd");
        }catch(NumberFormatException e)
        {
            Log.e("xd", "xd");
        }
        isChanged.set(position, true);
        changedAmount -=buf;

        subAmounts.set(position, buf);
        for(int i=0;i<subAmounts.size();i++){
            if (i==position || isChanged.get(i))
                continue;
            else
                subAmounts.set(i, changedAmount/usersInListChanged);
        }
        float checkSubs =0;
        for (Float x: subAmounts) {
            checkSubs +=x;
        }
        if (checkSubs!=this.amount){
            for (int i=0;i<subAmounts.size();i++) {
                subAmounts.set(i, this.amount/subAmounts.size());
            }
            String errTXT = "sum of values exceeds overall value";
            Toast.makeText(this.context, errTXT, Toast.LENGTH_LONG).show();
        }
        notifyDataSetChanged();
    }

    private void updateAdapter(int pos){
        loadSubsList(pos);
        notifyDataSetChanged();
    }

    private void loadSubsList(int pos){
        int firstActive = getFirstActive();
        BigDecimal a,b;

        if(firstActive!=-1) {
            a = new BigDecimal(amount / usersInList);
            b = a.setScale(2, BigDecimal.ROUND_DOWN);
        }
        else b = new BigDecimal(0);

        float sumBuf = 0;
        for (int i=0;i<subAmounts.size();i++){
            if(i==pos)
                continue;
            else
                if(isEnabled.get(i)) {
                    subAmounts.set(i, b.floatValue());
                    sumBuf += b.floatValue();
                }
        }
        if (pos!=-1)
            subAmounts.set(pos, 0f);

        if (amount!=sumBuf && firstActive!=-1)
            subAmounts.set(firstActive, (float) (subAmounts.get(firstActive)+(amount-sumBuf)));

    }

    private int getFirstActive(){
        for (int i=0;i<isEnabled.size();i++){
            if (!isEnabled.get(i))
                continue;
            else return i;
        }
        return -1;
    }
}
