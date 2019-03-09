package com.example.param.emall;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
   ListView listView ;
   RecyclerView recyclerView;
   ArrayList<ItemData> list;
   MyAdapter adapter;
   DatabaseReference refDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        /*recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        list = new ArrayList<ItemData>();
        FirebaseUser Current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = Current_user.getUid();
        refDb = FirebaseDatabase.getInstance().getReference().child("user").child(uid).child("product_list");*/
       // listView = (ListView)findViewById(R.id.list_Data);

        /*Bundle recdData = getIntent().getExtras();
        String myVal = recdData.getString("value");
        TextView textView = (TextView)findViewById(R.id.textview);
        textView.setText(myVal);*/
        //DatabaseHelper db = new DatabaseHelper(CartActivity.this);
       /* ItemData itemData = new ItemData();
        itemData = db.DisplayData();
        list.add(itemData);
        Log.v("ddd","list"+list.size());

        adapter = new MyAdapter(CartActivity.this,list);
        recyclerView.setAdapter(adapter);*/

       refDb.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });



    }
}
