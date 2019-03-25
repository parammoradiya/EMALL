package com.example.param.emall;

public class cartactivitymodel {

    private String Name;
    private  String Price;
    // private  String contact_no;
    private  String qty;

    public cartactivitymodel() {
    }

    public cartactivitymodel(String name, String price, String qty) {
        Name = name;
        Price = price;
        this.qty = qty;
    }

    public  String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
