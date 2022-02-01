package com.example.splitpaymentapp.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.DetailProduct;
import com.example.splitpaymentapp.model.DetailProductList;

import java.util.ArrayList;

public class PayDetailsPop extends Activity {

    TextView userTV;
    Button nextItemBtn, finishBtn;
    EditText pdProductET, pdValueET;
    String userName;
    Float amount, subAmounts;
    int position;
    ArrayList<DetailProduct> products = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent detBundle = getIntent();
        setContentView(R.layout.paydetailspop_layout);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout(dm.widthPixels, dm.heightPixels);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        userName = detBundle.getStringExtra("name");
        amount = detBundle.getFloatExtra("amount", 0f);
        position = detBundle.getIntExtra("pos",-1);
        init();
        subAmounts = 0f;
        nextItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemName = pdProductET.getText().toString();
                Float itemVal = 0f;
                try{
                    itemVal = Float.parseFloat(pdValueET.getText().toString());}
                catch (NumberFormatException e){
                    pdValueET.setText("");
                    pdValueET.setHint("0.00");
                    pdValueET.setError("invalid value");
                }
                if (itemVal<0){
                    pdValueET.setText("");
                    pdValueET.setHint("0.00");
                    pdValueET.setError("value must be positive");
                }

                if (itemVal>amount || itemVal-subAmounts<0){
                    subAmounts-=itemVal;
                    pdValueET.setText("");
                    pdValueET.setHint("0.00");
                    pdValueET.setError("this exceeds amount of " + amount);
                }
                else {
                    DetailProduct product = new DetailProduct(itemName, itemVal);
                    products.add(product);
                    pdProductET.setText("");
                    pdProductET.setHint("next product");
                    pdValueET.setText("");
                    pdValueET.setHint("0.00");
                    subAmounts +=itemVal;
                }
            }
        });
//todo fix business logic
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnWithData = new Intent();
                Bundle listBundle = new Bundle();
                listBundle.putParcelableArrayList("list", products);
                returnWithData.putExtra("bundle", listBundle);
                returnWithData.putExtra("pos", position);
                returnWithData.putExtra("subAm", subAmounts);
                setResult(1, returnWithData);
                finish();
            }
        });
    }

    private void init(){
        userTV = (TextView) findViewById(R.id.pdUserTV);
        pdProductET = (EditText) findViewById(R.id.pdproductET);
        pdValueET = (EditText) findViewById(R.id.pdvalueET);
        nextItemBtn = (Button) findViewById(R.id.pdnextitemBtn);
        finishBtn = (Button) findViewById(R.id.pdfinishBtn);
        userTV.setText(userName);
        pdProductET.setHint("enter product");
        pdValueET.setHint("0.00");
    }
}
