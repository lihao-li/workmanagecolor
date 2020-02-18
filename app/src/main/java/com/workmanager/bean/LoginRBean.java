package com.workmanager.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRBean {
    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("authorityLevel")
    @Expose
    private String authorityLevel;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAuthorityLevel() {
        return authorityLevel;
    }

    public void setAuthorityLevel(String authorityLevel) {
        this.authorityLevel = authorityLevel;
    }
}
