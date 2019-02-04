package com.example.param.emall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    private ElegantNumberButton mnumberbutten;

    int quantity = 1;
    Button btn_scanner,addtocartbtn;
    ListView m_listView;
    public static Firebase m_ref;
    public static  TextView result_text;
    DatabaseReference databaseReference;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    public static ArrayList<String> m_product_list = new ArrayList<>();
    public static ArrayAdapter<String> arrayAdapter;
    Button btn_cart;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mDrawerlayout = (DrawerLayout) findViewById(R.id.activity_home_drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initNavigationDrawer();

        btn_scanner=(Button)findViewById(R.id.btn_scanner);
        addtocartbtn = (Button)findViewById(R.id.addtocartbtn);
        result_text=(TextView)findViewById(R.id.product_code);


        Firebase.setAndroidContext(this);
        m_ref=new Firebase("https://emall-57850.firebaseio.com//productDetails");
        m_listView=(ListView)findViewById(R.id.list_item);
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,m_product_list);
        m_listView.setAdapter(arrayAdapter);

        /*addtocartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartintent = new Intent(HomeActivity.this,CartActivity.class);
                Object obj = m_listView.getAdapter();
                String value= obj.toString();
                co.putExtra("value", value);

                startActivity(cartintent);
            }
        });*/

        /*btn_cart=(Button)findViewById(R.id.btn_cart);*/
        if (checkSelfPermission(android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }

        btn_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ScannerActivity.class));
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==MY_CAMERA_REQUEST_CODE)
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
                }
                return true;
            }
        });
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increament(View view) {
        if (quantity == 10) {
            Toast.makeText(this, "can not order more than 10 cups!", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity + 1;
        display(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decreament(View view) {
        if (quantity <= 1) {
            Toast.makeText(this, "can not order less than 1 cup!", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        display(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }
}
