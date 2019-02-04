package com.example.param.emall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Bundle recdData = getIntent().getExtras();
        String myVal = recdData.getString("value");

        TextView textView = (TextView)findViewById(R.id.textview);

        textView.setText(myVal);
    }
}
