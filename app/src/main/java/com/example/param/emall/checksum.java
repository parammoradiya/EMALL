package com.example.param.emall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class checksum extends AppCompatActivity implements PaytmPaymentTransactionCallback {

    static String status;
    static String total ="";
    static String orderId="", mid="",first,second,third,fourth;
    static FirebaseUser Current_user = FirebaseAuth.getInstance().getCurrentUser();
    static String custid = Current_user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        //initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeonly = new SimpleDateFormat("hh:mm");
        String dateString = sdf.format(date).toString();
        String timeString = timeonly.format(date).toString();

        UUID odid  = UUID.randomUUID();
        String[] parts = odid.toString().split("-");
        first = parts[0] ;
        second = parts[1];
        third = parts[2];
        fourth = parts[3];
        String oodid =  "OD" + first + second + third + fourth;

        Intent intent = getIntent();
        //orderId = intent.getExtras().getString("orderid");
        orderId = String.valueOf(oodid);
        custid = String.valueOf(custid);
        //custid = intent.getExtras().getString("custid");
        total = String.valueOf(CartAdapter.amount);
        Log.v("TOTAl >>> ",String.valueOf(CartAdapter.amount));
        CartAdapter.amount =0;
        mid = "PZbWtu87676061075527"; /// your marchant key
        sendUserDetailTOServerdd dl = new sendUserDetailTOServerdd();
        dl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    public class sendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(checksum.this);

        //private String orderId , mid, custid, amt;
        String url ="http://192.168.43.198/Paytm_App_Checksum_Kit_PHP-master/generateChecksum.php";
        String varifyurl =  "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID"+orderId;;

        // "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID"+orderId;
        String CHECKSUMHASH ="";

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected String doInBackground(ArrayList<String>... alldata) {
            JSONParser jsonParser = new JSONParser(checksum.this);
            String param=
                    "MID="+mid+
                            "&ORDER_ID=" + orderId+
                            "&CUST_ID="+custid+
                            "&CHANNEL_ID=WAP&TXN_AMOUNT="+total+"&WEBSITE=WEBSTAGING"+
                            "&CALLBACK_URL="+ varifyurl+"&INDUSTRY_TYPE_ID=Retail";
            Log.e("CheckSum param >>",param);
            Log.v("Total Amount >>>>",total);
            JSONObject jsonObject = jsonParser.makeHttpRequest(url,"POST",param);

            // yaha per checksum ke saht order id or status receive hoga..
            Log.e("CheckSum result >>",jsonObject.toString());
            if(jsonObject != null){
                Log.e("CheckSum result >>",jsonObject.toString());
                try {

                    CHECKSUMHASH=jsonObject.has("CHECKSUMHASH")?jsonObject.getString("CHECKSUMHASH"):"";
                    Log.e("CheckSum result >>",CHECKSUMHASH);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return CHECKSUMHASH;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(" setup acc ","  signup result  " + result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            PaytmPGService Service = PaytmPGService.getStagingService();
            // when app is ready to publish use production service
            // PaytmPGService  Service = PaytmPGService.getProductionService();

            // now call paytm service here
            //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
            HashMap<String, String> paramMap = new HashMap<String, String>();
            //these are mandatory parameters
            paramMap.put("MID", mid); //MID provided by paytm
            paramMap.put("ORDER_ID", orderId);
            paramMap.put("CUST_ID", custid);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", total);
            paramMap.put("WEBSITE", "WEBSTAGING");
            paramMap.put("CALLBACK_URL" ,varifyurl);
            //paramMap.put( "EMAIL" , "abc@gmail.com");   // no need
            // paramMap.put( "MOBILE_NO" , "9144040888");  // no need
            paramMap.put("CHECKSUMHASH" ,CHECKSUMHASH);
            //paramMap.put("PAYMENT_TYPE_ID" ,"CC");    // no need
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");

            PaytmOrder Order = new PaytmOrder(paramMap);
            Log.e("checksum ", "param "+ paramMap.toString());
            Service.initialize(Order,null);
            // start payment service call here
            Service.startPaymentTransaction(checksum.this, true, true,
                    checksum.this  );
        }
    }

    @Override
    public void onTransactionResponse(Bundle bundle) {
        Log.e("checksum ", " respon true " + bundle.toString());
        Toast.makeText(checksum.this,"Payment Successfull",Toast.LENGTH_SHORT).show();
        status = "Success";

        //Intent i = new Intent(checksum.this,FinalBillActivity.class);
        startActivity(new Intent(checksum.this,VerifyData.class));
       /* i.putExtra("CustomerID",custid);
        i.putExtra("Total",total);*/
       // startActivity(i);
        finish();
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(getApplicationContext(),"Network is not Available",Toast.LENGTH_SHORT).show();
        status = "Pending";
        startActivity(new Intent(checksum.this,VerifyData.class));
        finish();
    }

    @Override
    public void clientAuthenticationFailed(String s) {

    }

    @Override
    public void someUIErrorOccurred(String s) {
        Log.e("checksum ", " ui fail respon  "+ s );
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Log.e("checksum ", " error loading pagerespon true "+ s + "  s1 " + s1);
        status = "Failed";
        startActivity(new Intent(checksum.this,VerifyData.class));
        finish();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Log.e("checksum ", " cancel call back respon  " );
        //startActivity(new Intent(checksum.this,CartActivity.class));
        startActivity(new Intent(checksum.this,VerifyData.class));
        status = "Failed";
        finish();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Log.e("checksum ", "  transaction cancel " );
        Toast.makeText(checksum.this,"Transaction Cancel...",Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(checksum.this,CartActivity.class));
        startActivity(new Intent(checksum.this,VerifyData.class));
        status = "Failed";
        finish();
    }
}
