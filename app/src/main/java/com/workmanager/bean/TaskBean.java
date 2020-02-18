package com.workmanager.bean;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskBean {
    @SerializedName("taskId")
    @Expose
    private String taskId;

    @SerializedName("nfcTagId")
    @Expose
    private String nfcTagId;

    @SerializedName("imei")
    @Expose
    private String imei;


    @SerializedName("detail")
    @Expose
    private JsonObject detail;

    @SerializedName("statusId")
    @Expose
    private String statusId;

    @SerializedName("operation")
    @Expose
    private int operation;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getNfcTagId() {
        return nfcTagId;
    }

    public void setNfcTagId(String nfcTagId) {
        this.nfcTagId = nfcTagId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public JsonObject getDetail() {
        return detail;
    }

    public void setDetail(JsonObject detail) {
        this.detail = detail;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public int getUpdateFlg() {
        return operation;
    }

    public void setUpdateFlg(int operation) {
        this.operation = operation;
    }
}
