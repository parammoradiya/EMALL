package com.example.param.emall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VerifyData extends AppCompatActivity {

    DatabaseReference Transaction,Orderhistory,productlistremove;
    FirebaseUser Current_user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = Current_user.getUid();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(checksum.status.equalsIgnoreCase("Success")){
            Transaction = FirebaseDatabase.getInstance().getReference().child("All Transaction").child("Success").child(checksum.orderId);
                    /*.child("Date").child(HomeActivity.dateString).child(uid)*/
            Orderhistory = FirebaseDatabase.getInstance().getReference().child("user").child(uid).child("Order_History").child(checksum.orderId);
            productlistremove = FirebaseDatabase.getInstance().getReference().child("user").child(uid).child("product_list");

            CartAdapter.allItem.put("Status",checksum.status);
            CartAdapter.allItem.put("Date",HomeActivity.dateString);
            CartAdapter.allItem.put("UserID",uid);
            Log.v("Cart Item ",CartAdapter.allItem.toString());

            Transaction.setValue(CartAdapter.allItem);
            Orderhistory.setValue(CartAdapter.allItem);
           // productlistremove.remove();
            startActivity(new Intent(VerifyData.this,FinalBillActivity.class));
            finish();
        }
        else if(checksum.status.equalsIgnoreCase("Pending")){
            Transaction = FirebaseDatabase.getInstance().getReference().child("All Transaction").child("Pending").child(checksum.orderId);
                    /*.child("Date").child(HomeActivity.dateString).child(uid);*/
            CartAdapter.allItem.put("Status",checksum.status);
            CartAdapter.allItem.put("Date",HomeActivity.dateString);
            CartAdapter.allItem.put("UserID",uid);
            Transaction.setValue(CartAdapter.allItem);
            startActivity(new Intent(VerifyData.this,Payment.class));
        }
        else if(checksum.status.equalsIgnoreCase("Failed")){
            Transaction = FirebaseDatabase.getInstance().getReference().child("All Transaction").child("Failed").child(checksum.orderId);
            /*child("Date").child(HomeActivity.dateString).child(uid).*/
            CartAdapter.allItem.put("Status",checksum.status);
            CartAdapter.allItem.put("Date",HomeActivity.dateString);
            CartAdapter.allItem.put("UserID",uid);
            Transaction.setValue(CartAdapter.allItem);
            startActivity(new Intent(VerifyData.this,CartItemConfirmActivity.class));
        }
    }
}

