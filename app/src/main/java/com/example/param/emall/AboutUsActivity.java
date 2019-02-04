package com.example.param.emall;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class AboutUsActivity extends AppCompatActivity {

    private Toolbar OToolbar;

    ListView listViewobj;

    RelativeLayout OFacebook,OInstagram;
    //Button policy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        OToolbar = (Toolbar) findViewById(R.id.Aboutus_toolbar);

        setSupportActionBar(OToolbar);
        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        OFacebook = (RelativeLayout) findViewById(R.id.facebook);
        OInstagram = (RelativeLayout) findViewById(R.id.instagram);
        //  policy = (Button)findViewById(R.id.policy);

       /* listViewobj = (ListView) findViewById(R.id.listview);
        AboutusCustomaddapter custobj = new AboutusCustomaddapter(AboutusActivity.this, title, subtitle);
        listViewobj.setAdapter(custobj);*/

        OFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = openFacebook(AboutUsActivity.this);
                startActivity(i);
            }
        });


        OInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.instagram.com/m_m_a_150/?hl=en");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/m_m_a_150")));
                }
            }
        });

        /*policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }
    public static Intent openFacebook(Context context)
    {
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/100007949177744"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/akash.golakiya.7"));
        }
    }
}
