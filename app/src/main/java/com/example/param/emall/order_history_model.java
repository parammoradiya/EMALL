package com.example.param.emall;

import android.util.Log;

public class order_history_model {

    String allItem;
    String orderId;
    String date;
    String Status;
    String Time;
    String Amount;



    public order_history_model(String allItem, String orderId, String date, String status,String Amount) {
        this.allItem = allItem;
        this.orderId = orderId;
        this.date = date;
        this.Status = status;
        this.Amount = Amount;

    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public order_history_model() {
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String Amount) {
        this.Amount = Amount;
    }

    public String getAllItem() {
        return allItem;
    }

    public void setAllItem(String allItem) {
        this.allItem = allItem;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
