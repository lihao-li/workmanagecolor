package com.workmanager.entity;

public class WeightEntity {
    private String weighStationName="";
    private String ncfTagId="";
    private int thingId;

    public String getWeighStationName() {
        return weighStationName;
    }

    public void setWeighStationName(String weighStationName) {
        this.weighStationName = weighStationName;
    }

    public String getNcfTagId() {
        return ncfTagId;
    }

    public void setNcfTagId(String ncfTagId) {
        this.ncfTagId = ncfTagId;
    }

    public int getThingId() {
        return thingId;
    }

    public void setThingId(int thingId) {
        this.thingId = thingId;
    }
}
