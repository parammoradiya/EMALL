package com.example.param.emall;

import android.content.Context;
import android.icu.text.Collator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Response;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class OrderHistoryActivity extends AppCompatActivity {

   RecyclerView recyclerView;
    ArrayList<order_history_model> mlist;
    DatabaseReference order_ref,dataRef;
    order_history_adapter orderAdapter;

    private Toolbar OToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerView = (RecyclerView) findViewById(R.id.order_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mlist = new ArrayList<order_history_model>();




        OToolbar = (Toolbar) findViewById(R.id.order_history);

        //  Log.v("CODE::>",""+testCodelist.size());

        setSupportActionBar(OToolbar);
        getSupportActionBar().setTitle("ORDER HISTORY");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseUser Current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = Current_user.getUid();
        Log.v("USER", uid);
        order_ref = FirebaseDatabase.getInstance().getReference().child("EMALL Cart").child(uid).child("Order History");

        order_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String key = dataSnapshot1.getKey();
                    dataRef = order_ref.child(key);
                    Log.v("KEY >>>>>", key);
                    //Log.e("###","dataref"+dataRef);
                    //t1.setText(key);

                    //  for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                    //      String itemkey = dataSnapshot2.getKey();
                    //     String Item = dataSnapshot1.child(itemkey).getValue().toString();
                    // Log.e("###","@@@ itemkey "+itemkey+" item "+Item);
                    order_history_model fm = new order_history_model();
                    fm.setAllItem(dataSnapshot1.child("All Item").getValue().toString());
                    fm.setDate(dataSnapshot1.child("Date").getValue().toString());
                    fm.setStatus(dataSnapshot1.child("Status").getValue().toString());
                    fm.setOrderId(dataSnapshot1.child("OrderID").getValue().toString());
                    fm.setTime(dataSnapshot1.child("Time").getValue().toString());
                    fm.setAmount(dataSnapshot1.child("Amount").getValue().toString());
                    Log.v("Amount >> ", dataSnapshot1.child("Amount").getValue().toString());
                    mlist.add(fm);

                    // }
                    //FailedModel fm =dataSnapshot1.getValue(FailedModel.class);
                    // Log.e("###","key"+key+"##"+dataSnapshot1.toString());

                   Collections.reverse(mlist);
                }

                orderAdapter = new order_history_adapter(OrderHistoryActivity.this, mlist);
                recyclerView.setAdapter(orderAdapter);
                orderAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OrderHistoryActivity.this, "failed to load data!!", Toast.LENGTH_LONG).show();

            }
        });



    }
}
