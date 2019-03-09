package com.example.param.emall;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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

public class CartActivity extends AppCompatActivity {

    private Toolbar OToolbar;
    private ListView mlistview;
    TextView pname,pprice,pqua;
    FirebaseListAdapter adapter;
    private FirebaseUser mCurrentuser;
    private DatabaseReference mUserdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mlistview = (ListView) findViewById(R.id.cart_list);

        OToolbar = (Toolbar) findViewById(R.id.Cart_toolbar);
        setSupportActionBar(OToolbar);
        getSupportActionBar().setTitle("CART");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentuser.getUid();
        mUserdatabase = FirebaseDatabase.getInstance().getReference().child("user").child(current_uid);

        Query query = FirebaseDatabase.getInstance().getReference().child("user").child(current_uid).child("product_list");
        FirebaseListOptions<cartactivitymodel> options = new FirebaseListOptions.Builder<cartactivitymodel>().setLayout(R.layout.cart_layout).setQuery(query, cartactivitymodel.class).build();

        adapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(View v, Object model, int position) {

                Log.v("hii2", "aaaa");
                pname = v.findViewById(R.id.cpname);
                pprice = v.findViewById(R.id.cpprice);
                pqua = v.findViewById(R.id.cpquantity);

                cartactivitymodel rep = (cartactivitymodel) model;

                pname.setText(rep.getName().toString());
                pprice.setText(rep.getPrice().toString());
                pqua.setText(rep.getQty().toString());
            }
        };

        mlistview.setAdapter(adapter);

    }
    @Override
    protected void onStart() {
        super.onStart();

        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        adapter.stopListening();
    }
}

