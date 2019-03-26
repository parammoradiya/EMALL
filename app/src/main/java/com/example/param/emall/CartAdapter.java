package com.example.param.emall;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

   static HashMap<String,String> allItem = new HashMap<>();
    Context context;
    static int amount =0,tqty = 0;
    static String productname="";
    String item ="";
    ArrayList<cartactivitymodel> cartactivitymodels;
    int[] qty = new int[20];
    int[] pri = new int[20];
    String[] product = new String[50];

    public CartAdapter(Context c,ArrayList<cartactivitymodel> ca)
    {
        context = c;
        cartactivitymodels = ca;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_layout,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i) {
        cartViewHolder.pname.setText(cartactivitymodels.get(i).getName());
        cartViewHolder.pprice.setText(cartactivitymodels.get(i).getPrice());
        cartViewHolder.pqty.setText(cartactivitymodels.get(i).getQty());

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
        productname = productname + product[i] +"\n";
    }

    @Override
    public int getItemCount() {
        return cartactivitymodels.size() ;
    }


    class CartViewHolder extends RecyclerView.ViewHolder
    {
        TextView pname, pprice, pqty,ptotalprice;
        public CartViewHolder(View itemView)
        {
            super(itemView);
            pname = (TextView)itemView.findViewById(R.id.cpname);
            pprice = (TextView)itemView.findViewById(R.id.cpprice);
            pqty = (TextView)itemView.findViewById(R.id.cpquantity);
            //ptotalprice = (TextView)itemView.findViewById(R.id.txt_amount);
        }
    }
}
