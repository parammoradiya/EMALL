package com.example.param.emall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FinalBillActivity extends AppCompatActivity {

    Button btnCreate;
    TextView text;
    String count1;
    //int count=0;
    public String name,contcat;
    private DatabaseReference mUserdatabase;
    private FirebaseUser mCurrentuser;

    public static final String FRAGMENT_PDF_RENDERER_BASIC = "pdf_renderer_basic";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_bill);

      //  btnCreate = (Button)findViewById(R.id.create);
        //text =(TextView) findViewById(R.id.text1);

        count1=String.valueOf(CartActivity.count);

        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentuser.getUid();
        mUserdatabase = FirebaseDatabase.getInstance().getReference().child("user").child(current_uid);

        mUserdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("Name").getValue().toString();
                contcat = dataSnapshot.child("Contact_no").getValue().toString();
               //email = dataSnapshot.child("Email").getValue().toString();
               Log.v("name" ,name);
               Log.v("contact",contcat);

                /*tpname.setText(name);
                tpcontact.setText(contcat);
                tpemail.setText(email);*/
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //for handle error while retriving data
            }
        });

        createpdf1();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PdfRendererBasicFragment(),
                            FRAGMENT_PDF_RENDERER_BASIC)
                    .commit();
        }

        //startActivity(new Intent(FinalBillActivity.this,HomeActivity.class));
        //finish();

        /*btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }

    private void createpdf1() {

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int y=340;

        int convertHighet = 200, convertWidth=300;

        Resources mResources = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.pdf_heading);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(790, 1122, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, 710, 100, true);

        paint.setColor(Color.BLACK);
        canvas.drawBitmap(bitmap, 40, 50 , null);
        canvas.drawText("Order ID:- " + checksum.orderId, 40, 220, paint);
        //canvas.drawText("Cutomer Name:- " + String.valueOf(name) + "               " + "Mobile number:- " + String.valueOf(contcat) , 40, 240, paint );
        canvas.drawText("Date:- " + HomeActivity.dateString + "   Time:- " + HomeActivity.timeString, 40,260, paint);

        canvas.drawText("", 40, 280, paint);
        canvas.drawText("Products" + "                                                                                                                             " +
                "Quantity" + "          " +
                "Price/item"  + "          "+ "Price", 40, 300, paint);
        canvas.drawText("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", 40, 320, paint);

        for (int i=0;i<CartAdapter.qty.length;i++) {
            canvas.drawText( CartAdapter.product[i]  , 40, y, paint);
            canvas.drawText(String.valueOf(CartAdapter.qty[i]),480,y,paint);
            canvas.drawText(String.valueOf(CartAdapter.pri[i]),550,y,paint);
            canvas.drawText(String.valueOf(CartAdapter.qty[i]*CartAdapter.pri[i]),630,y,paint);
            //canvas.drawText("------------------------------------------------------------------------------------------", 40, 240, paint);
            y += 20;
        }
        y+= 20;
        canvas.drawText("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", 40, y, paint);

        canvas.drawText("Total:- " + checksum.total, 600, y+20, paint);
        document.finishPage(page);

        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/myBill/";
        File file = new File(directory_path);

        if (!file.exists())
        {
            file.mkdirs();
        }

        String targetPdf = directory_path + "Invoice"+checksum.orderId + ".pdf";
        File filePath = new File(targetPdf);

        try {

            if (!filePath.exists())
            {
                document.writeTo(new FileOutputStream(filePath));
            }
            if(filePath.exists()) {
                Toast.makeText(FinalBillActivity.this, "Invoice Downloaded", Toast.LENGTH_LONG).show();


               /*Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"parammoradiya98@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Order no--" + checksum.orderId);
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Thank you for shopping.");

                if (!file.exists() || !file.canRead()) {
                    return;
                }
                Uri uri = Uri.fromFile(file);
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));*/

            }

            //boolean_save=true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }
}

