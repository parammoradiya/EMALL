package com.example.param.emall;

public class productdetail {
    String P_code,P_name;

    productdetail(){

    }

    public productdetail(String p_code, String p_name) {
        P_code = p_code;
        P_name = p_name;
    }

    public String getP_code() {
        return P_code;
    }

    public void setP_code(String p_code) {
        P_code = p_code;
    }

    public String getP_name() {
        return P_name;
    }

    public void setP_name(String p_name) {
        P_name = p_name;
    }
}
