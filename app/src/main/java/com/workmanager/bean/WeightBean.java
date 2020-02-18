package com.workmanager.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeightBean {
    @SerializedName("weigh_station_id")
    @Expose
    private String weigh_station_id;

    @SerializedName("weigh_station_name")
    @Expose
    private String weigh_station_name;

    public String getWeigh_station_id() {
        return weigh_station_id;
    }

    public void setWeigh_station_id(String weigh_station_id) {
        this.weigh_station_id = weigh_station_id;
    }

    public String getWeigh_station_name() {
        return weigh_station_name;
    }

    public void setWeigh_station_name(String weigh_station_name) {
        this.weigh_station_name = weigh_station_name;
    }
}