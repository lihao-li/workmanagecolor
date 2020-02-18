package com.workmanager.entity;

import java.io.Serializable;

public class FieldEntity implements Serializable {

    private int baseFieldId;
    private String name="";
    private double lat;
    private double lon;
    private String ncfTagId="";
    private String address="";
    private String owner="";
    private int thingId;

    public int getBaseFieldId() {
        return baseFieldId;
    }

    public void setBaseFieldId(int baseFieldId) {
        this.baseFieldId = baseFieldId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getNcfTagId() {
        return ncfTagId;
    }

    public void setNcfTagId(String ncfTagId) {
        this.ncfTagId = ncfTagId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getThingId() {
        return thingId;
    }

    public void setThingId(int thingId) {
        this.thingId = thingId;
    }
}
