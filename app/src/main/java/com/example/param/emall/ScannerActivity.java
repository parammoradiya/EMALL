package com.example.param.emall;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
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


                    //HomeActivity.m_product_list.add("Product Code :"+code);
                    HomeActivity.m_product_list.add("Product Name: " + name);
                    HomeActivity.m_product_list.add("Product Price: " + price);
                    HomeActivity.arrayAdapter.notifyDataSetChanged();
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

