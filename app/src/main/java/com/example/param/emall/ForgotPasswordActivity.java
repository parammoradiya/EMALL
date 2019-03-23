package com.example.param.emall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    Button OReset;
    EditText OChange;
    private Toolbar OToolbar;
    private ProgressDialog OForgotProcessDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        OChange = (EditText) findViewById(R.id.reset_pass);
        OReset = (Button) findViewById(R.id.reset_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        OToolbar = (Toolbar) findViewById(R.id.ForgotPassword_toolbar);

        Intent i = new Intent();
        Bundle bundle = getIntent().getExtras();
        String header = bundle.getString("Header");
        setSupportActionBar(OToolbar);
        getSupportActionBar().setTitle(header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        OForgotProcessDialog = new ProgressDialog(this);

        OReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetwork.isInternetAvailable(ForgotPasswordActivity.this)) {
                    String email = OChange.getText().toString();
                    OForgotProcessDialog.setMessage("sendind reset link to this E-mail");
                    OForgotProcessDialog.show();
                    if (email.equals("")) {
                        OForgotProcessDialog.dismiss();
                        Toast.makeText(ForgotPasswordActivity.this, "Email is required...", Toast.LENGTH_SHORT).show();
                    } else {
                        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    OForgotProcessDialog.dismiss();
                                    Toast.makeText(ForgotPasswordActivity.this, "Password Reset Link send to your mail", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                                } else {
                                    OForgotProcessDialog.dismiss();
                                    Toast.makeText(ForgotPasswordActivity.this, "Error in sending reset link...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    OForgotProcessDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(OReset, "No Internet Connection", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

}
