package com.workmanager.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssociateBean {
    @SerializedName("thingId")
    @Expose
    private int thingId;

    @SerializedName("nfcTagId")
    @Expose
    private String nfcTagId;

    @SerializedName("operation")
    @Expose
    private int operation;

    public int getThingId() {
        return thingId;
    }

    public void setThingId(int thingId) {
        this.thingId = thingId;
    }

    public String getNfcTagId() {
        return nfcTagId;
    }

    public void setNfcTagId(String nfcTagId) {
        this.nfcTagId = nfcTagId;
    }

    public int getUpdateFlg() {
        return operation;
    }

    public void setUpdateFlg(int operation) {
        this.operation = operation;
    }
}
