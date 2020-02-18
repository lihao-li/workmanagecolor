package com.workmanager.entity;

public class DryingEntity {

    private String drierName="";
    private String ncfTagId="";
    private int thingId;

    public String getDrierName() {
        return drierName;
    }

    public void setDrierName(String drierName) {
        this.drierName = drierName;
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
