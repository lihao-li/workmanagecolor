package com.workmanager.bean;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VersionBean {
    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("version")
    @Expose
    private JsonObject version;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public JsonObject getData() {
        return version;
    }

    public void setData(JsonObject version) {
        this.version = version;
    }
}
