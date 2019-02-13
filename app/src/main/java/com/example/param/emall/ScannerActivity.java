package com.example.param.emall;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.example.param.emall.HomeActivity.m_product_list;
import static com.example.param.emall.HomeActivity.m_ref;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView scannerView;
    DatabaseHelper dh;
    ItemData itemData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        dh = new DatabaseHelper(ScannerActivity.this);
        dh.getWritableDatabase();
    }

    @Override
    public void handleResult(Result result) {

        m_ref.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                m_product_list.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    //String  code = ds.child("P_code").getValue().toString();
                    String name = ds.child("P_name").getValue().toString();
                    String price = ds.child("p_price").getValue().toString();
                    Log.v("name",""+name);
                   // dh.getWritableDatabase();
                   //itemData  = new ItemData("name","price","1");
                   //long i = dh.insertData(name,price,"1");
                    //Log.v("@@@", ""+i);
                    //Toast.makeText(ScannerActivity.this,"Saved!!!",Toast.LENGTH_LONG).show();
                    HomeActivity.name = name;
                    HomeActivity.price = price;
                    //HomeActivity.m_product_list.add("Product Code :"+code);
                    for(int i=0;i<1;i++){
                    HomeActivity.m_product_list.add("Product Name: " + name);
                    HomeActivity.m_product_list.add("Product Price: " + price);
                    HomeActivity.arrayAdapter.notifyDataSetChanged();}

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "error in retrive product info", Toast.LENGTH_SHORT).show();
            }
        });
        onBackPressed();
    }


    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}

