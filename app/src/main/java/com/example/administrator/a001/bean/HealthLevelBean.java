package com.example.administrator.a001.bean;

import java.io.Serializable;

/**
 * Created by Cirno ⑨ on 2018/1/11 0011.
 */

public class HealthLevelBean implements Serializable {

    private int heart_rate = 0;
    private int vital_capacity = 0;
    private int bmi = 0;
    private int total = 0;


    public HealthLevelBean() {
    }

    public int getHeart_rate() {
        return heart_rate;
    }

    public int getVital_capacity() {
        return vital_capacity;
    }

    public int getBmi() {
        return bmi;
    }

    public int getTotal() {
        return total;
    }

    public void setHeart_rate(int heart_rate) {
        this.heart_rate = heart_rate;
    }

    public void setVital_capacity(int vital_capacity) {
        this.vital_capacity = vital_capacity;
    }

    public void setBmi(int bmi) {
        this.bmi = bmi;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
