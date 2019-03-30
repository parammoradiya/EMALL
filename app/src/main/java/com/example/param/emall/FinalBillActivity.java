package com.example.param.emall;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FinalBillActivity extends AppCompatActivity {


    private static ArrayList<Activity> activities=new ArrayList<Activity>();

    Button btnCreate;
    TextView text;
    String count1;
    private Toolbar OToolbar;
    //int count=0;
    public String name="",contact="";
    private DatabaseReference mUserdatabase;
    private FirebaseUser mCurrentuser;
    UserData ud;

    public static final String FRAGMENT_PDF_RENDERER_BASIC = "InvoiceFragment";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_bill);

        activities.add(this);

      //  btnCreate = (Button)findViewById(R.id.create);
        //text =(TextView) findViewById(R.id.text1);

        count1=String.valueOf(CartActivity.count);

        OToolbar = (Toolbar) findViewById(R.id.Cart_toolbar);

        //  Log.v("CODE::>",""+testCodelist.size());

//        setSupportActionBar(OToolbar);
      //  getSupportActionBar().setTitle("INVOICE");
     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseUser Current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = Current_user.getUid();
        mUserdatabase = FirebaseDatabase.getInstance().getReference().child("user").child(uid);

      /*  mUserdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("Name").getValue().toString();
                contact = dataSnapshot.child("Contact_no").getValue().toString();
                //email = dataSnapshot.child("Email").getValue().toString();
                //ud = new UserData(name,contact);
                if(!name.equals("") && !contact.equals("")){
                ud.setName(dataSnapshot.child("Name").getValue().toString());
                ud.setContact(dataSnapshot.child("Contact_no").getValue().toString());
                    Log.v("Name,Contact",ud.getName()+ " " + ud.getContact());
                }

                *//*tpname.setText(name);
                tpcontact.setText(contcat);
                tpemail.setText(email);*//*
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //for handle error while retriving data
            }
        });*/


        createpdf1();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new InvoiceFragment(),
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

    private void createpdf1(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;
        Log.v("HELLLOO","MILAN");
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
        //String CustName = checksum.name;
        //String CustContact = checksum.contact;
        paint.setColor(Color.BLACK);
        canvas.drawBitmap(bitmap, 40, 50 , null);
        //Log.v("Name And Contact : ",checksum.name + " " + checksum.contact);
        canvas.drawText("Order ID:- " + checksum.orderId, 40, 220, paint);
        //canvas.drawText("Cutomer Name:- " + ud.getName() + "               " + "Mobile number:- " + ud.getContact() , 40, 240, paint );
        canvas.drawText("Date:- " + HomeActivity.dateString + "   Time:- " + HomeActivity.timeString, 40,260, paint);

        canvas.drawText("", 40, 280, paint);
        canvas.drawText("Products" + "                                                                                                                             " +
                "Quantity" + "          " +
                "Price/item"  + "          "+ "Price", 40, 300, paint);
        canvas.drawText("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", 40, 320, paint);
       //Log.v("PRODUCT >>",CartItemConfirmActivity.ProductData[0]);

        for (int i=0;i<CartActivity.ProductQty.length;i++) {
            canvas.drawText( CartActivity.ProductData[i] , 40, y, paint);
            canvas.drawText(String.valueOf(CartActivity.ProductQty[i]),480,y,paint);
            canvas.drawText(String.valueOf(CartActivity.ProductPrice[i]),550,y,paint);
            canvas.drawText(String.valueOf(CartActivity.ProductQty[i]*CartActivity.ProductPrice[i]),630,y,paint);
           // Log.v("DATA >>",String.valueOf(CartAdapter.qty[i]));
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

        String targetPdf = directory_path + "InvoiceFragment"+checksum.orderId + ".pdf";
        File filePath = new File(targetPdf);

        try {

            if (!filePath.exists())
            {
                document.writeTo(new FileOutputStream(filePath));
            }
            if(filePath.exists()) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                Uri uri = Uri.parse(directory_path);
                Log.v("@@@","@@@ "+uri);
                intent.setDataAndType(uri, "text/pdf");
                //  startActivity(intent);
                //startActivity(Intent.createChooser(intent, "Open folder"));



      /* MimeTypeMap mime = MimeTypeMap.getSingleton();
       //Log.d(AppConfig.APP_TAG, "File to download = " + String.valueOf(file));
       String ext=filePath.getName().substring(filePath.getName().indexOf(".")+1);
       String type = mime.getMimeTypeFromExtension(ext);
       Intent openFile = new Intent(Intent.ACTION_VIEW, Uri.fromFile(filePath));
    //   openFile.setDataAndType(Uri.fromFile(filePath), type);

       Uri apkURI = FileProvider.getUriForFile(
               MainActivity.this,
               this.getApplicationContext()
                       .getPackageName() + ".provider", filePath);
       Log.v("@@@",""+apkURI);
       openFile.setDataAndType(apkURI, type);
       openFile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
       PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), openFile, 0);
       // startActivity(intent);*/
                PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);


                Notification noti = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {

                    noti = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Hello "+ "Customer ")
                            .setContentText("Your Invoice is downloded !!").setSmallIcon(R.drawable.splash_icon).
                                    setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.splash_icon))
                            .setContentIntent(pIntent)
                            .setAutoCancel(true)
                            .build();
              /* noti.sound = Uri.parse("android.resource://"
                       + MainActivity.this + "/" + R.raw.sound);*/
                }
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                assert noti != null;
                noti.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(0, noti);

                Toast.makeText(FinalBillActivity.this, "InvoiceFragment Downloaded", Toast.LENGTH_LONG).show();


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

    /*@Override
    protected void onRestart() {
        super.onRestart();
        startActivity(new Intent(FinalBillActivity.this,HomeActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
       // startActivity(new Intent(FinalBillActivity.this,HomeActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        //startActivity(new Intent(FinalBillActivity.this,HomeActivity.class));
    }*/

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        activities.remove(this);
    }

    public static void finishAll()
    {
        for(Activity activity:activities)
            activity.finish();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(new Intent(FinalBillActivity.this,HomeActivity.class));
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FinalBillActivity.this,HomeActivity.class));
        finishAffinity();
    }
}

