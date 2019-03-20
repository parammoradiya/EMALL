package com.example.param.emall;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
                createPdf(text.getText().toString());
            }


        });

    }

    private void createPdf(String toString) {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        Rect r=new Rect(10,10,280,200);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawRect(r, paint);
        Intent i = new Intent();
        //String custID = i.getStringExtra("CustomerID").toString();
        //String Total = i.getStringExtra("Total").toString();
        //Log.v("DATA",custID + " : " + Total);
        // border
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(r, paint);


        //  paint.setColor(Color.RED);
        //canvas.drawCircle(50, 50, 30, paint);
        // canvas.drawRect(50,50,50,50,paint);
        paint.setColor(Color.BLACK);
        canvas.drawText("Bill Number:-"+count1, 40, 50, paint);
        canvas.drawText("Customer Id:-"+checksum.custid, 40, 70, paint);
        canvas.drawText("Products :-"+count1+""+"Qty:-"+count1, 40, 90, paint);
        canvas.drawText("Total:-"+checksum.total, 40, 110, paint);

        document.finishPage(page);
        //   pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
        //  page = document.startPage(pageInfo);
        // canvas = page.getCanvas();
        //paint = new Paint();
        //paint.setColor(Color.BLUE);
        // canvas.drawCircle(100, 100, 100, paint);
        // document.finishPage(page);
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/myBill/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+""+count+".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            Log.e("main", "error " + e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();

    }
}
