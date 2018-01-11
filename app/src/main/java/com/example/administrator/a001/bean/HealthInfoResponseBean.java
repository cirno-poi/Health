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
    private String blood_pressure;
    private String blood_sugar;
    private String body_temperature;

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

    public String getBlood_pressure() {
        return blood_pressure;
    }

    public String getBlood_sugar() {
        return blood_sugar;
    }

    public String getBody_temperature() {
        return body_temperature;
    }

    public void setBlood_pressure(String blood_pressure) {
        this.blood_pressure = blood_pressure;
    }

    public void setBlood_sugar(String blood_sugar) {
        this.blood_sugar = blood_sugar;
    }

    public void setBody_temperature(String body_temperature) {
        this.body_temperature = body_temperature;
    }
}
