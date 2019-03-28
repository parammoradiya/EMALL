package com.example.param.emall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {

    private static final int MY_STORAGE_REQUEST_CODE = 200;
    Button btn_payment;
    private Toolbar OToolbar;
    private ListView mlistview;
    public ArrayList list;
    TextView pname, pprice, pqua, txt_amount;
    FirebaseListAdapter adapter;
    String code;
    static int count=1000;
    static int amount, TotalPrice;
 //   int[] total;
    int pos = 0;
    private FirebaseUser mCurrentuser;

    Button mcartdelete;

    static ArrayList<cartactivitymodel> mlist;
    private DatabaseReference mUserdatabase, mref, mreference;
    RecyclerView mrecyclerview;
    CartAdapter madapter;
    private String orderid;
   //ArrayList<String> testCodelist;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        txt_amount = (TextView) findViewById(R.id.txt_amount);
        btn_payment = (Button) findViewById(R.id.payment);
        //mlistview = (ListView) findViewById(R.id.cart_list);
        //mcartdelete = (Button)findViewById(R.id.cartdelete);
        list = new ArrayList();
        OToolbar = (Toolbar) findViewById(R.id.Cart_toolbar);

      //  Log.v("CODE::>",""+testCodelist.size());

        setSupportActionBar(OToolbar);
        getSupportActionBar().setTitle("CART");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mrecyclerview = (RecyclerView) findViewById(R.id.myRecyclerview);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mlist = new ArrayList<cartactivitymodel>();

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_STORAGE_REQUEST_CODE);
        }

        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        final String current_uid = mCurrentuser.getUid();
        mreference = FirebaseDatabase.getInstance().getReference().child("user").child(current_uid).child("product_list");
        mreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    cartactivitymodel ca = dataSnapshot1.getValue(cartactivitymodel.class);
                    ca.setCode(dataSnapshot1.child("Code").getValue().toString());
                    Log.v("CODE","@@@"+dataSnapshot1.child("Code").getValue()+" $$$ "+ca.getName()+" %%% "+ ca.getPrice());
                    mlist.add(ca);
                }
                madapter = new CartAdapter(CartActivity.this, mlist);
                mrecyclerview.setAdapter(madapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CartActivity.this, "failed to load cart data!!", Toast.LENGTH_LONG).show();
            }
        });

        //mUserdatabase = FirebaseDatabase.getInstance().getReference().child("user").child(current_uid);
        //Query query = FirebaseDatabase.getInstance().getReference().child("user").child(current_uid).child("product_list");
        //FirebaseListOptions<cartactivitymodel> options = new FirebaseListOptions.Builder<cartactivitymodel>().setLayout(R.layout.cart_layout).setQuery(query, cartactivitymodel.class).build();
        //total = new int[100];
        //final ArrayList<String> keyList = new ArrayList<>();
        //final ArrayList<String> itemname = new ArrayList<>();
        //final ArrayList<String> itemsqty = new ArrayList<>();

        /*Date currentTime = Calendar.getInstance().getTime();
        orderid = "0000" + current_uid ;*/

        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CartActivity.this, CartItemConfirmActivity.class);
               // i.putExtra("testCodelist",testCodelist);
                startActivity(i);

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_STORAGE_REQUEST_CODE)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(CartActivity.this, "storage permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(CartActivity.this, "storage permission denied", Toast.LENGTH_LONG).show();
            }
    }
        /*adapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(View v, Object model, int position) {
                amount = 0;
               // Log.v("hii2", "aaaa");
                pname = v.findViewById(R.id.cpname);
                pprice = v.findViewById(R.id.cpprice);
                pqua = v.findViewById(R.id.cpquantity);

                cartactivitymodel rep = (cartactivitymodel) model;

                pname.setText(rep.getName().toString());
                pprice.setText(rep.getPrice().toString());
                pqua.setText(rep.getQty().toString());
                String price = rep.getPrice().toString();
                Log.v("Price >>>",price);
                String qty = rep.getQty().toString();
                Log.v("QTY >>>",qty);
               amount = amount + (Integer.parseInt(price)*Integer.parseInt(qty));
               list.add(amount);

               *//*for(int i=0;i<adapter.getCount();i++){
               total[i] = amount;
               TotalPrice = TotalPrice + amount;
               Log.v("Price >>>>",String.valueOf(TotalPrice));
               Log.v("total jo >>>",String.valueOf(total[i]));
                   amount = 0;}*//*
               Log.v("Amount >>",String.valueOf(amount));
                Log.v("ADAPTER SIze >>>>",String.valueOf(adapter.getCount()));
               // txt_amount.setText("Total amount = "+amount);
                count = adapter.getCount();
            }
        };

        mlistview.setAdapter(adapter);*/
      /*  String price = rep.getPrice().toString();
        Log.v("Price >>>",price);
        String qty = rep.getQty().toString();
        Log.v("QTY >>>",qty);*/
        //amount = amount + (Integer.parseInt(price)*Integer.parseInt(qty));
        //list.add(amount);

       /* for(int i=0;i<madapter.getItemCount();i++)
        {
            total[i] = amount;
            TotalPrice = TotalPrice + amount;
            Log.v("Price >>>>",String.valueOf(TotalPrice));
            Log.v("total jo >>>",String.valueOf(total[i]));
            amount = 0;
        }*/
           /* Log.v("Amount >>",String.valueOf(amount));
        Log.v("ADAPTER SIze >>>>",String.valueOf(madapter.getItemCount()));*/
        // txt_amount.setText("Total amount = "+amount);
        /*count = madapter.getItemCount();

        for(int i=0;i<mlist.size();i++){

            TotalPrice = TotalPrice + mlist.indexOf(i);
            Log.v("km cho ???",String.valueOf(mlist.indexOf(i)));
            Log.v("LALA >>>",String.valueOf(TotalPrice));

        }*/
        /*Log.v("COunteriya >>>>",String.valueOf(count));
        Log.v("LIST >>>>",mlistview.toString());*/

      /* mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     pos = position;
                     code = (String) parent.getItemAtPosition(position);
               Toast.makeText(CartActivity.this,"Name " +code ,Toast.LENGTH_SHORT).show();
           }
       });*/

       /*mcartdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = ref.child("user").child(current_uid).child("prduct_list");

                final String str = cartactivitymodel.getName().substring(0,6);
                if(str==""){
                    Toast.makeText(Showdata.this,"Please Select Items",Toast.LENGTH_LONG).show();
                }else {
                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("cancel", "onCancelled", databaseError.toException());
                        }
                    });
                }
            }
        });*/



    /*@Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }*/

    /*@Override
    public boolean onNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();
    }*/

    /*@Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }*/

}
