package com.example.param.emall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FeedbackActivity extends AppCompatActivity {

    DatabaseReference FeedBackMsg;
    private Toolbar OToolbar;
    private EditText OSubjectEdittext,OMessageEdittext;
    private Button OEmailSubmitButton;
    String[] to = {"milanmiyani11@gmail.com","parammoradiya98@gmail.com"
            ,"akashgolakiya501@gmail.com","rajnigujarati567@gmail.com"};
    ProgressDialog OFeedbackProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        OSubjectEdittext = (EditText)findViewById(R.id.email_subject_edittext);
        OMessageEdittext = (EditText)findViewById(R.id.email_message_edittext);
        OEmailSubmitButton = (Button)findViewById(R.id.email_submit);
        OFeedbackProgressDialog = new ProgressDialog(FeedbackActivity.this);


        OToolbar = (Toolbar) findViewById(R.id.Feedback_toolbar);
        setSupportActionBar(OToolbar);
        getSupportActionBar().setTitle("FEEDBACK");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseUser Current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = Current_user.getUid();
        FeedBackMsg = FirebaseDatabase.getInstance().getReference().child("User FeedBack").child(uid);

        OEmailSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OFeedbackProgressDialog.setMessage("Wait a Second");
                OFeedbackProgressDialog.show();
                OFeedbackProgressDialog.setCanceledOnTouchOutside(false);

                String subject = OSubjectEdittext.getText().toString();
                String message = OMessageEdittext.getText().toString();

                if(CheckNetwork.isInternetAvailable(FeedbackActivity.this)){
                    if(subject.isEmpty()&& message.isEmpty()) {
                        OFeedbackProgressDialog.dismiss();
                        Toast.makeText(FeedbackActivity.this,"please fill detail first",Toast.LENGTH_LONG).show();
                    }
                    else{
                        OFeedbackProgressDialog.dismiss();
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.setData(Uri.parse("mailto:"));
                        email.putExtra(Intent.EXTRA_EMAIL, to);
                        email.putExtra(Intent.EXTRA_SUBJECT, subject);
                        email.putExtra(Intent.EXTRA_TEXT, message);

                        email.setType("message/rfc822");

                        HashMap<String,String> msg = new HashMap<>();
                        msg.put("Email","milanmiyani11@gmail.com");
                        msg.put("Subject",subject);
                        msg.put("message",message);

                        startActivity(Intent.createChooser(email, "Choose an Email client :"));
                        FeedBackMsg.setValue(msg);
                    }}
                else {
                    OFeedbackProgressDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(OEmailSubmitButton, "No Internet Connection", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }
}
