package com.example.param.emall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar OToolbar;
    TextView tpname,tpcontact,tpemail,tpnameedit,tpcontactedit;
    Button tpresetpass;
    String name,contcat,email;

    private DatabaseReference mUserdatabase;
    private FirebaseUser mCurrentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tpname = (TextView)findViewById(R.id.profilename);
        tpcontact = (TextView)findViewById(R.id.profilecontact);
        tpemail = (TextView)findViewById(R.id.profileemail);

        tpnameedit = (TextView)findViewById(R.id.nameedit);
        tpcontactedit = (TextView)findViewById(R.id.contactedit);
        tpresetpass = (Button)findViewById(R.id.profile_reset_password);

        OToolbar = (Toolbar) findViewById(R.id.Profile_toolbar);
        setSupportActionBar(OToolbar);
        getSupportActionBar().setTitle("PROFILE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentuser.getUid();
        mUserdatabase = FirebaseDatabase.getInstance().getReference().child("user").child(current_uid);

        mUserdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("Name").getValue().toString();
                contcat = dataSnapshot.child("Contact_no").getValue().toString();
                email = dataSnapshot.child("Email").getValue().toString();

                tpname.setText(name);
                tpcontact.setText(contcat);
                tpemail.setText(email);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //for handle error while retriving data
            }
        });

        tpcontactedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this,ProfileUpdateActivity.class);
                i.putExtra("name",name);
                i.putExtra("contact",contcat);
                i.putExtra("email",email);
                startActivity(i);
            }
        });

        tpnameedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this,ProfileUpdateActivity.class);
                i.putExtra("name",name);
                i.putExtra("contact",contcat);
                i.putExtra("email",email);
                startActivity(i);
                //startActivity(new Intent(ProfileActivity.this,ProfileUpdateActivity.class));
            }
        });

        tpresetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ProfileActivity.this,ForgotPasswordActivity.class);
                i.putExtra("Header","RESET PASSWORD");
                startActivity(i);
                //startActivity(new Intent(ProfileActivity.this,ForgotPasswordActivity.class));
            }
        });
    }
}
