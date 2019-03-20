package com.example.param.emall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class Payment extends AppCompatActivity {
    EditText orderid, custid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Button btn = (Button) findViewById(R.id.start_transaction);
        orderid = (EditText) findViewById(R.id.orderid);
        custid = (EditText) findViewById(R.id.custid);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Payment.this, checksum.class);
                intent.putExtra("orderid", orderid.getText().toString());
                intent.putExtra("custid", custid.getText().toString());
                startActivity(intent);
            }
        });

        if (ContextCompat.checkSelfPermission(Payment.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Payment.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }
    }
}
