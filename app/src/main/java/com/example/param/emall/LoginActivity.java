package com.example.param.emall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText OUser_Email,OUser_Password;
    Button OLoginbtn;
    TextView OSign_up,OForgot_Password;/*OContinue_Without_Login;*/
    Toolbar OToolbar;
    ProgressDialog OLoginProgressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //to hide keyboard when activity start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        OUser_Email = (EditText)findViewById(R.id.u_name);
        OUser_Password = (EditText)findViewById(R.id.u_password);
        OLoginbtn = (Button) findViewById(R.id.loginbtn);
        OSign_up = (TextView)findViewById(R.id.sign);
        OForgot_Password = (TextView)findViewById(R.id.reset);
        /*OContinue_Without_Login = (TextView)findViewById(R.id.continue_without_login);*/
        OToolbar = (Toolbar) findViewById(R.id.Login_toolbar);

        setSupportActionBar(OToolbar);
        getSupportActionBar().setTitle("Login");

        firebaseAuth = FirebaseAuth.getInstance();

        OLoginProgressDialog = new ProgressDialog(this);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null)
        {
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            finish();
        }

        OLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = OUser_Email.getText().toString();
                String Password = OUser_Password.getText().toString();

                OLoginProgressDialog.setMessage("Wait a Second");
                OLoginProgressDialog.show();
                OLoginProgressDialog.setCanceledOnTouchOutside(false);

                if(CheckNetwork.isInternetAvailable(LoginActivity.this)){
                    if (!validate_email(OUser_Email) | !validate_password(OUser_Password)) {
                        return;
                    }
                    else {
                            validate(name, Password);
                    }
                }
                else{
                    OLoginProgressDialog.dismiss();

                    Snackbar snackbar = Snackbar.make(OLoginbtn, "No Internet Connection", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });

        OSign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                finish();
            }
        });

        OForgot_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
                finish();
            }
        });
    }

    private void validate(String email,String password)
    {
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    OLoginProgressDialog.dismiss();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {
                        // user is verified, so you can finish this activity or send user to activity which you want.
                        finish();
                        Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                        Intent mLginintent = new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(mLginintent);
                        finish();
                    } else {
                        // email is not verified, so just prompt the message to the user and restart this activity.
                        // NOTE: don't forget to log out the user.
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(LoginActivity.this, "Varify your email first!", Toast.LENGTH_SHORT).show();
                        //restart this activity
                    }

                    //Log.w("TAG", "signInWithEmail:failed", task.getException());

                } else {
                    OLoginProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"Login Failed!!!",Toast.LENGTH_SHORT).show();


                }
            }
        });
        /*firebaseAuth.signInWithEmailAndPassword(email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    OLoginProgressDialog.dismiss();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {
                        // user is verified, so you can finish this activity or send user to activity which you want.
                        finish();
                        Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                        Intent mLginintent = new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(mLginintent);
                        finish();
                    } else {
                        // email is not verified, so just prompt the message to the user and restart this activity.
                        // NOTE: don't forget to log out the user.
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(LoginActivity.this, "Varify your email first!", Toast.LENGTH_SHORT).show();
                        //restart this activity
                    }

                    //Log.w("TAG", "signInWithEmail:failed", task.getException());

                } else {
                    OLoginProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"Login Failed!!!",Toast.LENGTH_SHORT).show();


                }
            }
        });*/
    }


    public boolean validate_email(View view) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email = OUser_Email.getText().toString();
        if (email.isEmpty()) {
            OUser_Email.setError("Email required");
            return false;
        } else if (!email.matches(emailPattern)) {
            OLoginProgressDialog.dismiss();
            OUser_Email.setError("Email is not valid");
            return false;
        } else {
            OUser_Email.setError(null);
            return true;
        }
    }

    public boolean validate_password(View view) {
        String pass = OUser_Password.getText().toString();

        if (pass.isEmpty()) {
            OLoginProgressDialog.dismiss();
            OUser_Password.setError("Password required");
            return false;
        } else if (OUser_Password.getText().toString().length() < 8) {
            OLoginProgressDialog.dismiss();
            OUser_Password.setError("Password is too week");
            return false;
        } else {
            OUser_Password.setError(null);
            return true;
        }
    }

}
