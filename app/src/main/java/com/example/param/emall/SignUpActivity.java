package com.example.param.emall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.system.Os;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private DatabaseReference ODatabase,AdminData;
    FirebaseAuth firebaseAuth;
    EditText OUser_Name,OUser_Email,OUser_Contact,OUser_Password;
    Button OSignUpbtn;
    Toolbar OToolbar;
    private ProgressDialog ORegprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //to hide keyboard when activity start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        OUser_Name = (EditText)findViewById(R.id.user);
        OUser_Email = (EditText) findViewById(R.id.email);
        OUser_Contact = (EditText) findViewById(R.id.contact);
        OUser_Password = (EditText) findViewById(R.id.password);
        OSignUpbtn = (Button) findViewById(R.id.submitbtn);

        OToolbar = (Toolbar) findViewById(R.id.Register_toolbar);
        ORegprogress = new ProgressDialog(this);

        setSupportActionBar(OToolbar);
        getSupportActionBar().setTitle("Sign Up");


        OSignUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckNetwork.isInternetAvailable(SignUpActivity.this)){
                    if (!validate_email(OUser_Email) | !validate_password() | !validate_phone(OUser_Contact) | !validation_name(OUser_Name)) {
                        return;
                    } else {
                        ORegprogress.setMessage("we register your account");
                        ORegprogress.setCanceledOnTouchOutside(false);
                        ORegprogress.show();
                        ORegprogress.setCanceledOnTouchOutside(false);

                        firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.createUserWithEmailAndPassword(OUser_Email.getText().toString(),
                                OUser_Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    mailVerified();
                                } else {
                                    ORegprogress.dismiss();
                                    Snackbar snackbar = Snackbar.make(OSignUpbtn, "Registration Failed!!!", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            }
                        });

                    }
                }
                else {
                    Snackbar snackbar = Snackbar.make(OSignUpbtn, "No Internet Connection", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    private void mailVerified() {
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        sendUserData();
                        Toast.makeText(SignUpActivity.this, "Successfully Registered,Please verified your mail", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    } else {
                        ORegprogress.dismiss();
                       Toast.makeText(SignUpActivity.this, "Mail isn't sent...", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }
    }

    public boolean validate_phone(View v) {
        String Contact = OUser_Contact.getText().toString();
        if (Contact.isEmpty()) {
            ORegprogress.dismiss();
            OUser_Contact.setError("Phone number required");
            return false;
        } else if (Contact.length() > 10 | Contact.length() < 10) {
            ORegprogress.dismiss();
            OUser_Contact.setError("Phone number is not valid");
            return false;
        } else {
            OUser_Contact.setError(null);
            return true;
        }
    }

    public boolean validate_email(View view) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String Email = OUser_Email.getText().toString();
        if (!Email.matches(emailPattern)) {
            ORegprogress.dismiss();
            OUser_Email.setError("Email is not valid");
            return false;
        } else if (Email.isEmpty()) {
            ORegprogress.dismiss();
            OUser_Email.setError("Email is required");
            return false;
        } else {
            OUser_Email.setError(null);
            return true;
        }

    }

    public boolean validate_password() {
        String pass = OUser_Password.getText().toString();
        if (pass.isEmpty()) {
            ORegprogress.dismiss();
            OUser_Password.setError("Password field can't be Empty");
            return false;
        } else if (OUser_Password.getText().toString().length() < 8) {
            ORegprogress.dismiss();
            OUser_Password.setError("Password is too week");
            return false;
        } else {
            OUser_Password.setError(null);
            return true;
        }
    }

    public boolean validation_name(View v) {
        if (OUser_Name.getText().toString().isEmpty()) {
            ORegprogress.dismiss();
            OUser_Name.setError("Last name is required");
            return false;
        } else {
            OUser_Name.setError(null);
            return true;
        }
    }

    private void sendUserData()
    {
        ORegprogress.dismiss();
        FirebaseUser Current_user = FirebaseAuth.getInstance().getCurrentUser();
        // DatabaseReference myref = firebaseDatabase.getReference(firebaseAuth.getUid());

        String uid = Current_user.getUid();

        //userprofile user = new userprofile(mDisplayname.getText().toString(), mEmail.getText().toString());
        //myref.setValue(user);

        ODatabase = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
        //mDatabase1 = FirebaseDatabase.getInstance().getReference().child("reportmsg").child(uid);
        AdminData = FirebaseDatabase.getInstance().getReference().child("All User").child(uid);
        String name =OUser_Name.getText().toString();
        String email = OUser_Email.getText().toString();
        String contact_no = OUser_Contact.getText().toString();

        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("Name", name);
        userMap.put("Email", email);
        userMap.put("Contact_no", contact_no);
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeonly = new SimpleDateFormat("hh:mm");
        String dateString = sdf.format(date);
        String timeString = timeonly.format(date);

        HashMap<String, String> userMap1 = new HashMap<>();
        userMap1.put("name", name);
        userMap1.put("email", email);
        userMap1.put("Date",dateString);
        userMap1.put("Time",timeString);
        ODatabase.setValue(userMap);
        //mDatabase1.setValue(userMap1);
        AdminData.setValue(userMap1);
    }
}
