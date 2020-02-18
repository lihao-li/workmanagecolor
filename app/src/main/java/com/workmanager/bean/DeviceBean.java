package com.workmanager.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceBean {
    @SerializedName("imei")
    @Expose
    private String imei="";

    @SerializedName("deviceName")
    @Expose
    private String deviceName="";

    @SerializedName("deviceType")
    @Expose
    private String deviceType="1";

    @SerializedName("appVersion")
    @Expose
    private String appVersion="";

    @SerializedName("osVersion")
    @Expose
    private String osVersion="";

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }
}
