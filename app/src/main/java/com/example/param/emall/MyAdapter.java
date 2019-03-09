package com.example.param.emall;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.viewHolder> {
    Context context;
    ArrayList<ItemData> list;


    public MyAdapter(Context context, ArrayList<ItemData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.data_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int i) {
       holder.name.setText(list.get(i).getProduct_name());
       holder.price.setText(list.get(i).getProduct_price());
       holder.qty.setText(list.get(i).getQty());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class viewHolder extends RecyclerView.ViewHolder{
        TextView name,price,qty;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.txtName);
            price = (TextView)itemView.findViewById(R.id.txtPrice);
            qty = (TextView)itemView.findViewById(R.id.txtQty);

        }
    }
}
