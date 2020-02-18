package com.workmanager.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DrierBean {

    @SerializedName("drierNo")
    @Expose
    private String drierNo;

    @SerializedName("drierName")
    @Expose
    private String drierName;

    public String getDrierNo() {
        return drierNo;
    }

    public void setDrierNo(String drierNo) {
        this.drierNo = drierNo;
    }

    public String getDrierName() {
        return drierName;
    }

    public void setDrierName(String drierName) {
        this.drierName = drierName;
    }
}