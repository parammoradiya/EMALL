package com.example.param.emall;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    ArrayList<cartactivitymodel> cartactivitymodels;

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
    }

    @Override
    public int getItemCount() {
        return cartactivitymodels.size() ;
    }

    class CartViewHolder extends RecyclerView.ViewHolder
    {

        TextView pname, pprice, pqty;
        public CartViewHolder(View itemView){
            super(itemView);
            pname = (TextView)itemView.findViewById(R.id.cpname);
            pprice = (TextView)itemView.findViewById(R.id.cpprice);
            pqty = (TextView)itemView.findViewById(R.id.cpquantity);
        }
    }
}
