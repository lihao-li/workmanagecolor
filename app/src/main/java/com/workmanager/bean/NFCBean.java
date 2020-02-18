package com.workmanager.bean;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NFCBean {

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("list")
    @Expose
    private JsonArray list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public JsonArray getData() {
        return list;
    }

    public void setData(JsonArray list) {
        this.list = list;
    }
}
