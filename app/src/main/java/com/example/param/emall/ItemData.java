package com.example.param.emall;

public class ItemData {
    public String product_name;
    public String product_price;
    public String qty;
    public int id;

    public ItemData() {
    }

    public ItemData(String product_name, String product_price, String qty) {
        this.product_name = product_name;
        this.product_price = product_price;
        this.qty = qty;
    }

    public ItemData( int id, String product_name, String product_price, String qty) {
        this.product_name = product_name;
        this.product_price = product_price;
        this.qty = qty;
        this.id = id;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public String getQty() {
        return qty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
