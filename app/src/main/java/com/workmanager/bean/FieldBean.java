package com.workmanager.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FieldBean {

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("base_field_id")
    @Expose
    private String base_field_id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("min_lat1")
    @Expose
    private double min_lat1;

    @SerializedName("min_lon1")
    @Expose
    private double min_lon1;

    @SerializedName("min_lat2")
    @Expose
    private double min_lat2;

    @SerializedName("min_lon2")
    @Expose
    private double min_lon2;

    @SerializedName("min_lat3")
    @Expose
    private double min_lat3;

    @SerializedName("min_lon3")
    @Expose
    private double min_lon3;

    @SerializedName("min_lat4")
    @Expose
    private double min_lat4;

    @SerializedName("drierNo")
    @Expose
    private double min_lon4;

    @SerializedName("associate_id")
    @Expose
    private int associate_id;

    @SerializedName("ncf_tag_id")
    @Expose
    private String ncf_tag_id;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("owner")
    @Expose
    private String owner;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBase_field_id() {
        return base_field_id;
    }

    public void setBase_field_id(String base_field_id) {
        this.base_field_id = base_field_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMin_lat1() {
        return min_lat1;
    }

    public void setMin_lat1(double min_lat1) {
        this.min_lat1 = min_lat1;
    }

    public double getMin_lon1() {
        return min_lon1;
    }

    public void setMin_lon1(double min_lon1) {
        this.min_lon1 = min_lon1;
    }

    public double getMin_lat2() {
        return min_lat2;
    }

    public void setMin_lat2(double min_lat2) {
        this.min_lat2 = min_lat2;
    }

    public double getMin_lon2() {
        return min_lon2;
    }

    public void setMin_lon2(double min_lon2) {
        this.min_lon2 = min_lon2;
    }

    public double getMin_lat3() {
        return min_lat3;
    }

    public void setMin_lat3(double min_lat3) {
        this.min_lat3 = min_lat3;
    }

    public double getMin_lon3() {
        return min_lon3;
    }

    public void setMin_lon3(double min_lon3) {
        this.min_lon3 = min_lon3;
    }

    public double getMin_lat4() {
        return min_lat4;
    }

    public void setMin_lat4(double min_lat4) {
        this.min_lat4 = min_lat4;
    }

    public double getMin_lon4() {
        return min_lon4;
    }

    public void setMin_lon4(double min_lon4) {
        this.min_lon4 = min_lon4;
    }

    public int getAssociate_id() {
        return associate_id;
    }

    public void setAssociate_id(int associate_id) {
        this.associate_id = associate_id;
    }

    public String getNcf_tag_id() {
        return ncf_tag_id;
    }

    public void setNcf_tag_id(String ncf_tag_id) {
        this.ncf_tag_id = ncf_tag_id;
    }
}