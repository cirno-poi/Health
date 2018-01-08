package com.example.administrator.a001.bean;

import java.io.Serializable;

/**
 * 注册返回值bean
 *
 * Created by Cirno ⑨ on 2018/1/8 0008.
 */

public class RegisterResponseBean implements Serializable{

    private int statusCode;

    public RegisterResponseBean() {
    }

    public RegisterResponseBean(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
