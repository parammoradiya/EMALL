package com.example.param.emall;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
TextView txt_name,txt_price;
EditText edt_update;
Button btn_update;
   static HashMap<String,String> allItem = new HashMap<>();
    Context context;
    static int amount =0,tqty = 0;
    DatabaseReference reference;
    static String productname="";
    String item ="";
    ArrayList<cartactivitymodel> cartactivitymodels;
    static int[] qty ;
    static int[] pri;
    static String[] product ;
   // ArrayList<String> testCodelist;

    public CartAdapter(Context c,ArrayList<cartactivitymodel> ca)
    {
        context = c;
        cartactivitymodels = ca;
        //testCodelist = new ArrayList<>();
        qty = new int[cartactivitymodels.size()];
        pri = new int[cartactivitymodels.size()];
        product = new String[cartactivitymodels.size()];
        //testCodelist = testCode;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_layout,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder,int i) {
       final cartactivitymodel cm = cartactivitymodels.get(i);
       final int pos = i;
        cartViewHolder.pname.setText(cm.getName());
        cartViewHolder.pprice.setText(cm.getPrice());
        cartViewHolder.pqty.setText(cm.getQty());
         item += "Item :"+cartactivitymodels.get(i).getName() +"\nPrice : "+cartactivitymodels.get(i).getPrice() +"\nQty : " +cartactivitymodels.get(i).getQty() + "\n";
       /* allItem.put("Item "+i,cartactivitymodels.get(i).getName());
        allItem.put("Price "+i,cartactivitymodels.get(i).getPrice());
        allItem.put("qty "+i,cartactivitymodels.get(i).getQty());*/
       allItem.put("All Item",item);

        qty[i] = Integer.parseInt(cartactivitymodels.get(i).getQty());
        pri[i] = Integer.parseInt(cartactivitymodels.get(i).getPrice());
        product[i] = String.valueOf(cartactivitymodels.get(i).getName());
        amount = amount + (qty[i] * pri[i]);
        tqty = tqty + qty[i];
        //productname = productname + product[i] +"\n";;

        cartViewHolder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            //    Toast.makeText(context,cm.getName(),Toast.LENGTH_SHORT).show();
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                View mView = View.inflate(context,R.layout.update_data,null);
                txt_name = (TextView)mView.findViewById(R.id.txt_Name);
                txt_price = (TextView)mView.findViewById(R.id.txt_Price);
                edt_update = (EditText)mView.findViewById(R.id.edt_update_qty);
                btn_update = (Button)mView.findViewById(R.id.btn_update);
                txt_name.setText(cm.getName());
                txt_price.setText(cm.getPrice());
                mBuilder.setView(mView);
                Log.v("CODE >>>",cm.getCode());
                edt_update.setText(cm.getQty());
                final AlertDialog dialog = mBuilder.create();
                //Log.v("Code Position >>>>",String.valueOf(pos));
                btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.v("CODE",">>>>>");
                        String update_qty =  edt_update.getText().toString();
                        FirebaseUser Current_user = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = Current_user.getUid();
                        //String clickCode = HomeActivity.CodeList.toString();
                        //String clickCode = Codelist.get(0);
                       reference = FirebaseDatabase.getInstance().getReference().child("user").child(uid).child("product_list").child(cm.getCode());

                        HashMap<String,String> updateData = new HashMap<>();
                        updateData.put("Name",cm.getName());
                        updateData.put("Price",cm.getPrice());
                        updateData.put("Qty",String.valueOf(Integer.parseInt(update_qty) +Integer.parseInt(cm.getQty())));
                        updateData.put("Code",cm.getCode());

                        reference.setValue(updateData);
                        HomeActivity.arrayAdapter.clear();
                        dialog.dismiss();
                        Intent intent= new Intent(context,CartActivity.class);
                        context.startActivity(intent);
                        Toast.makeText(context,"Data Updated!!!",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();

            }

        });
    }

    @Override
    public int getItemCount() {
        return cartactivitymodels.size() ;
    }


    class CartViewHolder extends RecyclerView.ViewHolder
    {
        TextView pname, pprice, pqty,ptotalprice;
        RelativeLayout rel;
        public CartViewHolder(View itemView)
        {
            super(itemView);
            pname = (TextView)itemView.findViewById(R.id.cpname);
            pprice = (TextView)itemView.findViewById(R.id.cpprice);
            pqty = (TextView)itemView.findViewById(R.id.cpquantity);
            rel = (RelativeLayout) itemView.findViewById(R.id.rel);
            //ptotalprice = (TextView)itemView.findViewById(R.id.txt_amount);
        }
    }
}
