package com.workmanager.entity;

import com.google.gson.JsonArray;

import java.io.Serializable;

public class MapFieldEntity implements Serializable {
    private int thingId;
    private String ncfTagId="";
    private JsonArray Arrayshape;

    public int getThingId() {
        return thingId;
    }

    public void setThingId(int thingId) {
        this.thingId = thingId;
    }

    public String getNcfTagId() {
        return ncfTagId;
    }

    public void setNcfTagId(String ncfTagId) {
        this.ncfTagId = ncfTagId;
    }

    public JsonArray getArrayshape() {
        return Arrayshape;
    }

    public void setArrayshape(JsonArray arrayshape) {
        Arrayshape = arrayshape;
    }



}
