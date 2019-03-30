package com.example.param.emall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VerifyData extends AppCompatActivity {

    DatabaseReference Transaction,Orderhistory,productlistremove,mUserdatabase;
    FirebaseUser Current_user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = Current_user.getUid();
    static String name,contact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("STATTAT>>",checksum.status);
        if(checksum.status.equalsIgnoreCase("Success")){
            Transaction = FirebaseDatabase.getInstance().getReference().child("All Transaction").child("Success").child(checksum.orderId);
                    /*.child("Date").child(HomeActivity.dateString).child(uid)*/
            Orderhistory = FirebaseDatabase.getInstance().getReference().child("EMALL Cart").child(uid).child("Order History").child(checksum.orderId);
            productlistremove = FirebaseDatabase.getInstance().getReference().child("EMALL Cart").child(uid).child("Cart Added");
            CartAdapter.allItem.put("Status",checksum.status);
            CartAdapter.allItem.put("Date",HomeActivity.dateString);
            CartAdapter.allItem.put("Time",HomeActivity.timeString);
            CartAdapter.allItem.put("OrderID",checksum.orderId);
            CartAdapter.allItem.put("Amount",String.valueOf(CartActivity.finalTotal));
            Log.v("Cart Item ",CartAdapter.allItem.toString());

            Transaction.setValue(CartAdapter.allItem);
            //CartActivity.mlist.clear();
            Orderhistory.setValue(CartAdapter.allItem);
            //for temporery
            productlistremove.removeValue();


            startActivity(new Intent(VerifyData.this,FinalBillActivity.class));
            finish();
        }
        else if(checksum.status.equalsIgnoreCase("Pending")){
            Transaction = FirebaseDatabase.getInstance().getReference().child("All Transaction").child("Pending").child(checksum.orderId);
            Orderhistory = FirebaseDatabase.getInstance().getReference().child("EMALL Cart").child(uid).child("Order History").child(checksum.orderId);
                    /*.child("Date").child(HomeActivity.dateString).child(uid);*/
            CartAdapter.allItem.put("Status",checksum.status);
            CartAdapter.allItem.put("Date",HomeActivity.dateString);
           // CartAdapter.allItem.put("Time",HomeActivity.timeString);
            CartAdapter.allItem.put("UserID",uid);
            CartAdapter.allItem.put("Amount","0");
            Transaction.setValue(CartAdapter.allItem);
            Orderhistory.setValue(CartAdapter.allItem);
            startActivity(new Intent(VerifyData.this,CartActivity.class));
            finish();

        }
        else if(checksum.status.equalsIgnoreCase("Failed")){
            Transaction = FirebaseDatabase.getInstance().getReference().child("All Transaction").child("Failed").child(checksum.orderId);
            Orderhistory = FirebaseDatabase.getInstance().getReference().child("EMALL Cart").child(uid).child("Order History").child(checksum.orderId);
            /*child("Date").child(HomeActivity.dateString).child(uid).*/
            CartAdapter.allItem.put("Status",checksum.status);
            CartAdapter.allItem.put("Date",HomeActivity.dateString);
            CartAdapter.allItem.put("Time",HomeActivity.timeString);
            CartAdapter.allItem.put("OrderID",checksum.orderId);
            CartAdapter.allItem.put("Amount","0");
            Transaction.setValue(CartAdapter.allItem);
            Orderhistory.setValue(CartAdapter.allItem);
            startActivity(new Intent(VerifyData.this,CartActivity.class));
            finish();
        }

    }
}

