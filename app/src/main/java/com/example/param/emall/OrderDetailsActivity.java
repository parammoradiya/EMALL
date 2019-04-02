package com.example.param.emall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderDetailsActivity extends AppCompatActivity {

    TextView headertext,orderstatus,orderid,date,totalamount,allitem;
    String Totalamount,Status,Date,Orderid,Time,AllItem;
    private Toolbar OToolbar;
    ImageView statusimage1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        headertext = (TextView)findViewById(R.id.headtext);
        orderstatus = (TextView)findViewById(R.id.orderstatus);
        orderid = (TextView)findViewById(R.id.orderiddetail);
        date = (TextView)findViewById(R.id.date);
        totalamount = (TextView)findViewById(R.id.totalamount);
        allitem = (TextView)findViewById(R.id.allitem);
        statusimage1 = (ImageView)findViewById(R.id.Status_Image1);

        OToolbar = (Toolbar) findViewById(R.id.Order_Detail_toolbar);

        Intent intent = new Intent();
        Bundle extras = getIntent().getExtras();

        /*if(getIntent().hasExtra("TotalAmount") && getIntent().hasExtra("Status") &&
                getIntent().hasExtra("Date") && getIntent().hasExtra("Time") &&
                getIntent().hasExtra("allItem") && getIntent().hasExtra("OrderID")){*/

        if (extras!=null){
            Totalamount = extras.getString("TotalAmount");
            Status = extras.getString("Status");
            Date = extras.getString("Date");
            Time = extras.getString("Time");
            AllItem = extras.getString("allItem");
            Orderid = extras.getString("Orderid");
            Log.v("hiiii",String.valueOf(Orderid));
        }

        setSupportActionBar(OToolbar);
        getSupportActionBar().setTitle("OID:" + Orderid);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

     if(Status.equalsIgnoreCase("Success")) {
         headertext.setText("Payment of RS. " + Totalamount + " is done !");
         orderstatus.setText(Status + "\n\n");
         orderid.setText(Orderid + "\n");
         date.setText(Date + "\n" + Time + "\n\n");
         totalamount.setText("Rs. " + Totalamount);
         allitem.setText("\n\n" + AllItem);
     }
     else if(Status.equalsIgnoreCase("Failed")){
         headertext.setText("Your Transaction Failed !");
         orderstatus.setText(Status + "\n\n");
         orderid.setText(Orderid + "\n");
         date.setText(Date + "\n" + Time + "\n\n");
         totalamount.setText("Rs. " + Totalamount);
         allitem.setText("\n\n" + AllItem);
     }
    }


}
