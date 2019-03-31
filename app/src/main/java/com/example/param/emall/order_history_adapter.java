package com.example.param.emall;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class order_history_adapter extends RecyclerView.Adapter<order_history_adapter.orderViewHolder> {

    Context context;
    ArrayList<order_history_model> orderhistorymodels;

    public order_history_adapter(Context context, ArrayList<order_history_model> orderhistoryadapters) {
        this.context = context;
        this.orderhistorymodels = orderhistoryadapters;
    }

    @NonNull
    @Override
    public orderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new orderViewHolder(LayoutInflater.from(context).inflate(R.layout.order_history_list,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull orderViewHolder orderViewHolder, final int i) {
        /*failedViewHolder.userId.setText("UserId:- "+failedModels.get(i).getOrderId());
        //Log.v("Id >>>",String.valueOf(failedModels.get(i).getOrderId()));
        failedViewHolder.allItem.setText(failedModels.get(i).getAllItem());*/
        //failedViewHolder.date.setText("Date:- "+failedModels.get(i).getDate());
        orderViewHolder.userId.setText("OrderId:- "+orderhistorymodels.get(i).getOrderId());
        //Log.v("Id >>>",String.valueOf(failedModels.get(i).getOrderId()));
        //orderViewHolder.allItem.setText(orderhistorymodels.get(i).getAllItem());
        orderViewHolder.date.setText("Date:- "+orderhistorymodels.get(i).getDate() + "\nTime:-" + orderhistorymodels.get(i).getTime());
        orderViewHolder.amount.setText("Amount:- "+orderhistorymodels.get(i).getAmount());

        Log.v("aaaaaaaaa",String.valueOf(orderhistorymodels.get(i).getAmount()));

        if(orderhistorymodels.get(i).getStatus().equalsIgnoreCase("Success")){
            orderViewHolder.statusImage.setImageResource(R.drawable.success);
        }
        else if(orderhistorymodels.get(i).getStatus().equalsIgnoreCase("Pending")){
            orderViewHolder.statusImage.setImageResource(R.drawable.pending);
        }
        else {
            orderViewHolder.statusImage.setImageResource(R.drawable.failed);
        }

       orderViewHolder.rel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent = new Intent(context,OrderDetailsActivity.class);
               intent.putExtra("allItem",orderhistorymodels.get(i).getAllItem());

               intent.putExtra("Date",orderhistorymodels.get(i).getDate());
               intent.putExtra("Time",orderhistorymodels.get(i).getTime());
               intent.putExtra("Status",orderhistorymodels.get(i).getStatus());
               intent.putExtra("TotalAmount",orderhistorymodels.get(i).getAmount());
               intent.putExtra("Orderid",orderhistorymodels.get(i).getOrderId());
               Log.v("aaaaaaaaaaaaaaaaaaaaaaa",String.valueOf(orderhistorymodels.get(i).getOrderId()));
               context.startActivity(intent);

           }
       });
    }

    @Override
    public int getItemCount() {
        return orderhistorymodels.size();
    }

    class orderViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        TextView userId, allItem, date,amount;
        ImageView statusImage;
        RelativeLayout rel;
        public orderViewHolder(@NonNull View itemView) {
            super(itemView);
            userId = (TextView)itemView.findViewById(R.id.userId);
            //allItem = (TextView)itemView.findViewById(R.id.itemData);
            date = (TextView)itemView.findViewById(R.id.date);
            statusImage = (ImageView)itemView.findViewById(R.id.statusImage);
            amount = (TextView)itemView.findViewById(R.id.order_amount);

            rel = (RelativeLayout) itemView.findViewById(R.id.rel);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
