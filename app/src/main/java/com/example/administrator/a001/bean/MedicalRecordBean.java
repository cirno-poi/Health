package com.example.administrator.a001.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Cirno ⑨ on 2018/1/13 0013.
 */

public class MedicalRecordBean implements Serializable {

    private List<String> medicalRecord;

    public MedicalRecordBean() {
    }

    public List<String> getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(List<String> medicalRecord) {
        this.medicalRecord = medicalRecord;
    }
}
