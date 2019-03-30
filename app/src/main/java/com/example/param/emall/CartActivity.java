package com.example.param.emall;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    static int finalTotal = 0;
    static int amount, TotalPrice;
    //   int[] total;
    int pos = 0;
    private FirebaseUser mCurrentuser;

    Button mcartdelete,btn_pay;

    static ArrayList<cartactivitymodel> mlist;
    private DatabaseReference mUserdatabase, mref, mreference;
    RecyclerView mrecyclerview;
    CartAdapter madapter;
    private String orderid;
    //ArrayList<String> testCodelist;

    TextView totalqty,totalamount,txt_name,txt_price;
    ImageView close;
    static int finalqty=0;
    static String ProductData[];
    static int ProductQty[];
    static int ProductPrice[];

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
        btn_payment.setVisibility(View.INVISIBLE);
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
                if(mlist.isEmpty()) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(CartActivity.this);
                    mBuilder.setMessage("No item in Cart");
                    mBuilder.setCancelable(false);
                    mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(CartActivity.this,HomeActivity.class));
                        }
                    });
                    mBuilder.show();
                    btn_payment.setVisibility(View.INVISIBLE);
                }
                else{
                    btn_payment.setVisibility(View.VISIBLE);
                    madapter = new CartAdapter(CartActivity.this, mlist);
                    mrecyclerview.setAdapter(madapter);}
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(CartActivity.this);
                mBuilder.setCancelable(false);
                //View mView = View.inflate(context,R.layout.update_data,null);
                View mView = View.inflate(CartActivity.this,R.layout.cart_confirm_dialog,null);


                txt_name = (TextView)mView.findViewById(R.id.txt_Name);
                txt_price = (TextView)mView.findViewById(R.id.txt_Price);
                totalqty = (TextView) mView.findViewById(R.id.edt_total_qty);
                totalamount = (TextView) mView.findViewById(R.id.edt_total_amount);
                btn_pay = (Button)mView.findViewById(R.id.btn_update);

                close = (ImageView)mView.findViewById(R.id.cancelDialog);

                CartAdapter.amount = 0;
                CartAdapter.tqty = 0 ;

                ProductData = new String[CartAdapter.pri.length];
                ProductQty = new int[CartAdapter.pri.length];
                ProductPrice = new int[CartAdapter.pri.length];

                for(int j=0;j<CartAdapter.qty.length;j++){
                    ProductData[j] = CartAdapter.product[j];
                    ProductPrice[j] = CartAdapter.pri[j];
                    ProductQty[j] = CartAdapter.qty[j];
                    CartAdapter.amount = CartAdapter.amount + (CartAdapter.qty[j]*CartAdapter.pri[j]);
                    CartAdapter.tqty = CartAdapter.tqty + CartAdapter.qty[j];
                    //CartAdapter.productname = CartAdapter.productname + CartAdapter.product[i] + "\n";
                }

                finalTotal = CartAdapter.amount ;
                finalqty = CartAdapter.tqty;
                totalqty.setText(String.valueOf(finalqty));
                totalamount.setText(String.valueOf(finalTotal));

                Log.v("finaltotal", String.valueOf(CartAdapter.amount));
                Log.v("total qty", String.valueOf(CartAdapter.tqty));
                //mBuilder.(finalTotal);
//                mBuilder.setMessage(CartAdapter.tqty);
                //mBuilder.setView(dialogView);

                mBuilder.setView(mView);

                final AlertDialog dialog = mBuilder.create();
                //Log.v("Code Position >>>>",String.valueOf(pos));
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btn_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(CartActivity.this,checksum.class));
                  }
                });
                dialog.show();
                //btn_payment.setVisibility(View.INVISIBLE);

                //Intent i = new Intent(CartActivity.this, CartItemConfirmActivity.class);
                //startActivity(i);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CartActivity.this,HomeActivity.class));
        finish();

    }

    @Override
    public boolean onNavigateUp() {
        return super.onNavigateUp();
    }
}
