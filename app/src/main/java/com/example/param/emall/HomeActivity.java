package com.example.param.emall;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    DatabaseReference reference, myRef,lastSeen;
    FirebaseAuth firebaseAuth;
    private int REQUEST = 100;
    private Date time;
    static int Total;
    HashMap<String,String> cart;
    boolean flag = false;
    public static String name, price, code,error;
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    private ElegantNumberButton mnumberbutten;
    static TextView quantityTextView,txtData;
    int quantity = 1;
    static String dateString,timeString;
    String codeSacn;
    public int totalQty;
    String qty="1";
    Button btn_scanner, addtocartbtn;
    ListView m_listView;
    public static Firebase m_ref;
    public static TextView result_text;
    DatabaseReference databaseReference;
    private static final int MY_CAMERA_REQUEST_CODE = 100,MY_STORAGE_REQUEST_CODE = 200;
    public static ArrayList<String> m_product_list = new ArrayList<>();
    public static ArrayAdapter<String> arrayAdapter;
    static Button btn_cart,Dec,Inc;
    public static TextView infoqty;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        infoqty = (TextView)findViewById(R.id.infoqty);

        //txtData = (TextView)findViewById(R.id.product_code);
        mDrawerlayout = (DrawerLayout) findViewById(R.id.activity_home_drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        cart = new HashMap<>();
        time = new Date();
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeonly = new SimpleDateFormat("hh:mm:ss");
        dateString = sdf.format(date);
        timeString = timeonly.format(date);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initNavigationDrawer();

        btn_scanner = (Button) findViewById(R.id.btn_scanner);
        addtocartbtn = (Button) findViewById(R.id.addtocartbtn);
        //result_text = (TextView) findViewById(R.id.product_code);
        FirebaseUser Current_user = FirebaseAuth.getInstance().getCurrentUser();
        // DatabaseReference myref = firebaseDatabase.getReference(firebaseAuth.getUid());
        quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("1");
        String uid = Current_user.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("user").child(uid).child("product_list");
        lastSeen = FirebaseDatabase.getInstance().getReference().child("All User").child(uid).child("Last Seen");

        HashMap<String,String> lastLogin = new HashMap<>();
        lastLogin.put("Last seen Date",dateString);
        lastLogin.put("Last Seen Time",timeString);

        Firebase.setAndroidContext(this);
        m_ref = new Firebase("https://emall-57850.firebaseio.com//productDetails");
        m_listView = (ListView) findViewById(R.id.list_item);
        Inc = (Button)findViewById(R.id.Inc);
        Dec = (Button) findViewById(R.id.Dec);
        Inc.setVisibility(View.INVISIBLE);
        Dec.setVisibility(View.INVISIBLE);
        quantityTextView.setVisibility(View.INVISIBLE);
        infoqty.setVisibility(View.INVISIBLE);
        //HomeActivity.arrayAdapter.clear();

        lastSeen.setValue(lastLogin);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, m_product_list);
        m_listView.setAdapter(arrayAdapter);



        addtocartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                Intent cartintent = new Intent(HomeActivity.this, CartActivity.class);
                Log.v("Code.....",code);
                Log.v("@@@", "" + name);
                Log.v("@@", "" + price);
                     quantity =1;
                    if(!code.equals("1")) {
                        if (!name.isEmpty() && !price.isEmpty() && !qty.isEmpty()) {
                            myRef = reference.child(code);
                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("Name", name);
                            userMap.put("Price", price);
                            userMap.put("qty", qty);
                            myRef.setValue(userMap);
                            cart.put("Name", name);
                            cart.put("Price", price);
                            cart.put("qty", qty);
                            Total = Integer.parseInt(qty)*Integer.parseInt(price);
                /*HashMap<String,Integer> userMap1 = new HashMap<>();
                userMap1.put("Quantity",totalQty);
                myRef.setValue(userMap1);*/
                            //userMap.put("Contact_no", contact_no);
                            //userMap.put("text", contact_no);
                            // long i = db.insertData(name,price,"1");
                            //Log.v("data",""+i);

               /* Object obj = m_listView.getAdapter();
                String value= obj.toString();
                cartintent.putExtra("value", value);
                //Log.e("value", value);
               */
                            startActivity(cartintent);
                            Log.v("CART ::",cart.toString());
                            arrayAdapter.clear();
                            quantityTextView.setText("1");
                            code="";
                            name="";
                            price="";
                        } else {
                            quantityTextView.setText("1");
                            Inc.setVisibility(View.INVISIBLE);
                            Dec.setVisibility(View.INVISIBLE);
                            quantityTextView.setVisibility(View.INVISIBLE);
                            infoqty.setVisibility(View.INVISIBLE);
                            Toast.makeText(HomeActivity.this, "Product is not Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Inc.setVisibility(View.INVISIBLE);
                        Dec.setVisibility(View.INVISIBLE);
                        quantityTextView.setVisibility(View.INVISIBLE);
                        infoqty.setVisibility(View.INVISIBLE);
                        Toast.makeText(HomeActivity.this, "Product is not Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Inc.setVisibility(View.INVISIBLE);
                    Dec.setVisibility(View.INVISIBLE);
                    quantityTextView.setVisibility(View.INVISIBLE);
                    infoqty.setVisibility(View.INVISIBLE);
                    Toast.makeText(HomeActivity.this, "Value is not selected", Toast.LENGTH_SHORT).show();
                }

            }
        });

        /*btn_cart=(Button)findViewById(R.id.btn_cart);*/
        if (checkSelfPermission(android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }
        /*if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                   MY_STORAGE_REQUEST_CODE);
        }
*/
        btn_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = "";
                price ="";
                startActivityForResult(new Intent(getApplicationContext(),ScannerActivity.class),REQUEST);
                Dec.setVisibility(View.VISIBLE);
                Inc.setVisibility(View.VISIBLE);
                quantityTextView.setVisibility(View.VISIBLE);
                infoqty.setVisibility(View.VISIBLE);
            }
        });

        /*if(code.equals("")){
            Toast.makeText(HomeActivity.this,"Product is not Found",Toast.LENGTH_SHORT).show();
        }
        else{
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, m_product_list);
            m_listView.setAdapter(arrayAdapter);
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST){
            if(resultCode == RESULT_OK) {
                code = data.getStringExtra("CODE");
                error = data.getStringExtra("ERROR");

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(HomeActivity.this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(HomeActivity.this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logged_in_user, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item1) {
        int id = item1.getItemId();
        *//*if (id == R.id.action_open) {
            showPictureDialog();
            Log.v("HII", "action open");
            return true;
        }*//*
        if(id == R.id.action_open)
        {
            return true;
        }
        return super.onOptionsItemSelected(item1);
    }*/

    public void initNavigationDrawer() {
        NavigationView mnavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mnavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                firebaseAuth = FirebaseAuth.getInstance();
                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.logout: {
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        break;
                    }
                    case R.id.about: {
                        startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
                        break;
                    }
                    case R.id.feedback: {
                        startActivity(new Intent(HomeActivity.this, FeedbackActivity.class));
                        break;
                    }
                    case R.id.carttab: {
                        startActivity(new Intent(HomeActivity.this, CartActivity.class));
                        break;
                    }
                    case R.id.profiletab: {
                        startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                        break;
                    }
                    /*case  R.id.action_open_cart:{
                        startActivity(new Intent(HomeActivity.this, CartActivity.class));
                        break;
                    }*/
                }
                return true;
            }
        });
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increament(View view) {
        if (quantity >= 10) {
            Toast.makeText(this, "can not order more than 10 cups!", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity + 1;
        display(quantity);
    }

    // This method is called when the minus button is clicked.
    public void decreament(View view) {
        if (quantity <= 1) {
            Toast.makeText(this, "can not order less than 1 cup!", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        display(quantity);
    }

    // This method displays the given quantity value on the screen.
    private void display(int number) {
        totalQty = number;
        qty = String.valueOf(totalQty);
        //quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText(qty);
    }
}
