package com.example.param.emall;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FinalBillActivity extends AppCompatActivity {
    Button btnCreate;
    TextView text;
    String count1;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_bill);
        btnCreate = (Button)findViewById(R.id.create);
        text =(TextView) findViewById(R.id.text1);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count=count+1;
                count1=String.valueOf(count);
                createpdf1();
            }


        });

    }

    private void createpdf1() {

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = 200, convertWidth=300;

        Resources mResources = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.mainlogo);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(790, 1122, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();


        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);



        bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 150, 0 , null);
        canvas.drawText("Bill:-"+count1, 40, 170, paint);
        canvas.drawText("Cutomer Id:-"+checksum.custid, 40, 190, paint);
        canvas.drawText("Products :-"+count1+" "+"Qty:-"+count1, 40, 210, paint);
        canvas.drawText("Total:-"+checksum.total, 40, 230, paint);
        document.finishPage(page);


        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/myBill/";
        File file = new File(directory_path);
        if (!file.exists())
        {
            file.mkdirs();
        }
        String targetPdf = directory_path+""+count+".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(FinalBillActivity.this,"Invoice Downloaded",Toast.LENGTH_LONG).show();

            //boolean_save=true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }
}

