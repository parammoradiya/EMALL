package com.example.param.emall;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
    //DatabaseHelper dh;
    String name,price,code="";
    ItemData itemData;
    boolean flag = false;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        // dh = new DatabaseHelper(ScannerActivity.this);
        // dh.getWritableDatabase();
    }

    @Override
    public void handleResult(Result result) {
        final String codeScan = result.getText();
        i = new Intent();
        m_ref.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                m_product_list.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    code = ds.child("P_code").getValue().toString();
                    if (codeScan.equals(code)) {
                        flag = true;
                        // code = ds.child("P_code").getValue().toString();
                         name = ds.child("P_name").getValue().toString();
                         price = ds.child("p_price").getValue().toString();
                         break;
                    }
                }
                if (flag) {
                    //Log.v("Code :",code);
                    Log.v("name", "" + name);

                    /*i.putExtra("CODE",code);
                    i.putExtra("ERROR","");
                    setResult(RESULT_OK,i);*/
                    // dh.getWritableDatabase();
                    //itemData  = new ItemData("name","price","1");
                    //long i = dh.insertData(name,price,"1");
                    //Log.v("@@@", ""+i);
                    //Toast.makeText(ScannerActivity.this,"Saved!!!",Toast.LENGTH_LONG).show();
                    HomeActivity.code = codeScan;
                    HomeActivity.name = name;
                    HomeActivity.price = price;
                    //HomeActivity.m_product_list.add("Product Code :"+code);
                    //for (int i = 0; i < 1; i++) {
                    HomeActivity.m_product_list.add("Product Name: " + name);
                    HomeActivity.m_product_list.add("Product Price: " + price);
                    HomeActivity.arrayAdapter.notifyDataSetChanged();
                    //}
                }
                else{
                    Log.v("code:",codeScan);
                    /*i.putExtra("CODE",code);
                    i.putExtra("ERROR","Product is not Found");
                    setResult(RESULT_OK,i);*/
                    //HomeActivity.m_listView.setAdapter(null);
                    HomeActivity.code = "1";
                    HomeActivity.arrayAdapter.clear();
                    //HomeActivity.m_product_list.add("Product Name: " + "Not Found");
                    //HomeActivity.m_product_list.add("Product Price: " + "Not found");
                    Toast.makeText(ScannerActivity.this,"Product is not found!!!",Toast.LENGTH_LONG).show();
                    HomeActivity.Inc.setVisibility(View.INVISIBLE);
                    HomeActivity.Dec.setVisibility(View.INVISIBLE);
                    HomeActivity.quantityTextView.setVisibility(View.INVISIBLE);
                    HomeActivity.infoqty.setVisibility(View.INVISIBLE);
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

