package com.example.param.emall;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
    private DatabaseReference mreference;
    RecyclerView mrecyclerview;
    CartAdapter madapter;
    private Toolbar OToolbar;
    private FirebaseUser mCurrentuser;
    TextView txt_amount;
    static int finalTotal = 0;

    static String ProductData[];
    static int ProductQty[];
    static int ProductPrice[];
    //ArrayList<String> testCodelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_item_confirm);

        backbtn = (Button)findViewById(R.id.cartbackbtn);
        paybtn = (Button)findViewById(R.id.cartpaybtn);
        txt_amount = (TextView)findViewById(R.id.txt_amount);

        OToolbar = (Toolbar) findViewById(R.id.Cart_Confirm_toolbar);
        //testCodelist = new ArrayList<>();
        //testCodelist = getIntent().getStringArrayListExtra("testCodelist");
        ProductData = new String[CartAdapter.pri.length];
        ProductQty = new int[CartAdapter.pri.length];
        ProductPrice = new int[CartAdapter.pri.length];

        setSupportActionBar(OToolbar);
        getSupportActionBar().setTitle("CONFIRM ORDER");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mrecyclerview = (RecyclerView) findViewById(R.id.myRecyclerview);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mlist = new ArrayList<cartactivitymodel>();

        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        final String current_uid = mCurrentuser.getUid();
        mreference = FirebaseDatabase.getInstance().getReference().child("EMALL Cart").child(current_uid).child("Cart Added");
        mreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    cartactivitymodel ca = new cartactivitymodel();
                    ca.setName(dataSnapshot1.child("Name").getValue().toString());
                    ca.setPrice(dataSnapshot1.child("Price").getValue().toString());
                    ca.setCode(dataSnapshot1.child("Code").getValue().toString());
                    ca.setQty(dataSnapshot1.child("Qty").getValue().toString());
                    //Log.v("CODE","@@@"+dataSnapshot1.child("Code").getValue()+" $$$ "+dataSnapshot1.child("Name").getValue().toString()+" %%% "+ dataSnapshot1.child("Price").getValue().toString() + "!!!!" + dataSnapshot1.child("Qty").getValue().toString());
                    mlist.add(ca);
                }
                CartAdapter.amount = 0;
                CartAdapter.tqty = 0;
                CartAdapter.productname = "";
                madapter = new CartAdapter(CartItemConfirmActivity.this, mlist);
                mrecyclerview.setAdapter(madapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CartItemConfirmActivity.this, "failed to load cart data!!", Toast.LENGTH_LONG).show();
            }
        });

        for(int i=0;i<CartAdapter.qty.length;i++){
            ProductData[i] = CartAdapter.product[i];
            ProductPrice[i] = CartAdapter.pri[i];
            ProductQty[i] = CartAdapter.qty[i];
            CartAdapter.amount = CartAdapter.amount + (CartAdapter.qty[i]*CartAdapter.pri[i]);
            CartAdapter.tqty = CartAdapter.tqty + CartAdapter.qty[i];
            CartAdapter.productname = CartAdapter.productname + CartAdapter.product[i] + "\n";
        }

        finalTotal = CartAdapter.amount;
        txt_amount.setText("Total Amount : " +  finalTotal +
                "\nTotal Quantity : " + CartAdapter.tqty);



        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartAdapter.amount = 0;
                CartAdapter.tqty = 0;
                CartAdapter.productname = "";
                Log.v("Back >>>",String.valueOf(CartAdapter.amount));
                startActivity(new Intent(CartItemConfirmActivity.this,CartActivity.class));
                finish();
            }
        });

        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckNetwork.isInternetAvailable(CartItemConfirmActivity.this)) {
                    startActivity(new Intent(CartItemConfirmActivity.this, checksum.class));
                    finish();
                }else {
                    Snackbar snackbar = Snackbar.make(paybtn, "No Internet Connection", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

    }
}
