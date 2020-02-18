package com.workmanager.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginBean {

    @SerializedName("loginId")
    @Expose
    private String loginId;


    @SerializedName("passWord")
    @Expose
    private String passWord;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
