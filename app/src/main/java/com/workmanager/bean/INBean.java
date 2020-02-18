package com.workmanager.bean;

import java.io.Serializable;

public class INBean  implements Serializable {
    private String name="";
    private int thingid;
    private String nfcId="";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNfcId() {
        return nfcId;
    }

    public void setNfcId(String nfcId) {
        this.nfcId = nfcId;
    }

    public int getThingid() {
        return thingid;
    }

    public void setThingid(int thingid) {
        this.thingid = thingid;
    }
}
