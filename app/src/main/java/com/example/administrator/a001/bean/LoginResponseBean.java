package com.example.administrator.a001.bean;

import java.io.Serializable;

/**
 * 登录返回值bean
 * <p>
 * Created by Cirno ⑨ on 2018/1/9 0009.
 */

public class LoginResponseBean implements Serializable {

    private int statusCode;
//    private String msg;
    private String token;


    public LoginResponseBean() {
    }

    public LoginResponseBean(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
