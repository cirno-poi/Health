package com.example.administrator.a001.bean;

import java.io.Serializable;

/**
 * 健康信息请求参数bean
 *
 * Created by Cirno ⑨ on 2018/1/10 0010.
 */

public class HealthInfoResponseBean implements Serializable {

    private String weight;
    private String height;
    private String vitalCapacity;
    private String heartRate;

    public HealthInfoResponseBean() {
    }

    public String getWeight() {
        return weight;
    }

    public String getHeight() {
        return height;
    }

    public String getVitalCapacity() {
        return vitalCapacity;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setVitalCapacity(String vitalCapacity) {
        this.vitalCapacity = vitalCapacity;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }
}
