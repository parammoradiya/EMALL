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

    DatabaseReference Transaction;
    FirebaseUser Current_user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = Current_user.getUid();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(checksum.status.equalsIgnoreCase("Success")){
            Transaction = FirebaseDatabase.getInstance().getReference().child("All Transaction").child("Success").child(HomeActivity.dateString).child(uid).child(checksum.orderId);
            CartAdapter.allItem.put("Status",checksum.status);
            Log.v("Cart Item ",CartAdapter.allItem.toString());
            Transaction.setValue(CartAdapter.allItem);
            startActivity(new Intent(VerifyData.this,FinalBillActivity.class));
        }
        else if(checksum.status.equalsIgnoreCase("Pending")){
            Transaction = FirebaseDatabase.getInstance().getReference().child("All Transaction").child("Pending").child(HomeActivity.dateString).child(uid).child(checksum.orderId);
            CartAdapter.allItem.put("Status",checksum.status);
            Transaction.setValue(CartAdapter.allItem);
            startActivity(new Intent(VerifyData.this,Payment.class));
        }
        else if(checksum.status.equalsIgnoreCase("Failed")){
            Transaction = FirebaseDatabase.getInstance().getReference().child("All Transaction").child("Failed").child(HomeActivity.dateString).child(uid).child(checksum.orderId);
            CartAdapter.allItem.put("Status",checksum.status);
            Transaction.setValue(CartAdapter.allItem);
            startActivity(new Intent(VerifyData.this,CartItemConfirmActivity.class));
        }
    }
}

