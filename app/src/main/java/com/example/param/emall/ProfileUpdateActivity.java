package com.example.param.emall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProfileUpdateActivity extends AppCompatActivity {

    EditText ename, econtact;
    private Toolbar OToolbar;
    Button resetdata;
    String updateName, updateContact;
    String name,contact,email;
    DatabaseReference updateData,updateAdminUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);

        ename = (EditText) findViewById(R.id.profile_update_name);
        econtact = (EditText) findViewById(R.id.profile_update_contact);
        resetdata = (Button) findViewById(R.id.profile_update_submit);

        OToolbar = (Toolbar) findViewById(R.id.Profile_Update_toolbar);
        setSupportActionBar(OToolbar);
        getSupportActionBar().setTitle("PROFILE UPDATE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseUser Current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = Current_user.getUid();
        updateData = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
        updateAdminUserData = FirebaseDatabase.getInstance().getReference().child("All User").child(uid);
        Intent i = new Intent();
        Bundle bundle = getIntent().getExtras();
         name = bundle.getString("name");
         contact = bundle.getString("contact");
        email = bundle.getString("email");
        Log.v("Email",email);
        ename.setText(name);
        econtact.setText(contact);


        resetdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateName = ename.getText().toString();
                updateContact = econtact.getText().toString();
                HashMap<String, String> updateUser = new HashMap<>();
                updateUser.put("Name", updateName);
                updateUser.put("Contact_no", updateContact);
                updateUser.put("Email",email);
                Log.v("Userdata",updateUser.toString());
                updateData.setValue(updateUser);
                updateAdminUserData.setValue(updateUser);
            }
        });
    }
}
