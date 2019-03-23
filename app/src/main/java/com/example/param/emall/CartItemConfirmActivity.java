package com.example.param.emall;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartItemConfirmActivity extends AppCompatActivity {

    Button backbtn,paybtn;
    ArrayList<cartactivitymodel> mlist;
    private DatabaseReference mUserdatabase, mref, mreference;
    RecyclerView mrecyclerview;
    CartAdapter madapter;
    private Toolbar OToolbar;
    private FirebaseUser mCurrentuser;
    TextView txt_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_item_confirm);

        backbtn = (Button)findViewById(R.id.cartbackbtn);
        paybtn = (Button)findViewById(R.id.cartpaybtn);
        txt_amount = (TextView)findViewById(R.id.txt_amount);

        OToolbar = (Toolbar) findViewById(R.id.Cart_Confirm_toolbar);

        setSupportActionBar(OToolbar);
        getSupportActionBar().setTitle("CONFIRM ORDER");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mrecyclerview = (RecyclerView) findViewById(R.id.myRecyclerview);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mlist = new ArrayList<cartactivitymodel>();

        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        final String current_uid = mCurrentuser.getUid();
        mreference = FirebaseDatabase.getInstance().getReference().child("user").child(current_uid).child("product_list");
        mreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    cartactivitymodel ca = dataSnapshot1.getValue(cartactivitymodel.class);
                    mlist.add(ca);
                }
                CartAdapter.amount = 0;
                madapter = new CartAdapter(CartItemConfirmActivity.this, mlist);
                mrecyclerview.setAdapter(madapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CartItemConfirmActivity.this, "failed to load cart data!!", Toast.LENGTH_LONG).show();
            }
        });

       txt_amount.setText("Total Amount : " +  CartAdapter.amount);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartAdapter.amount = 0;
                Log.v("Back >>>",String.valueOf(CartAdapter.amount));
                startActivity(new Intent(CartItemConfirmActivity.this,CartActivity.class));

            }
        });

        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartItemConfirmActivity.this,checksum.class));
            }
        });

    }
}
