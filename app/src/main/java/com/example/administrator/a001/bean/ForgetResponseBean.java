package com.example.administrator.a001.bean;

import java.io.Serializable;

/**
 * <p>
 * Created by Cirno ⑨ on 2018/1/8 0008.
 */

public class ForgetResponseBean implements Serializable {

    private int statusCode;
    private String question;

    public ForgetResponseBean() {
    }

    public ForgetResponseBean(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
