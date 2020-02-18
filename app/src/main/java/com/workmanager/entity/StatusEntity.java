package com.workmanager.entity;

import com.google.gson.JsonObject;

import java.util.Date;

public class StatusEntity {
    private String nfcTagId;
    private long createDate;
    private long updateDate;
    private JsonObject detail;

    public String getNfcTagId() {
        return nfcTagId;
    }

    public void setNfcTagId(String nfcTagId) {
        this.nfcTagId = nfcTagId;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public JsonObject getDetail() {
        return detail;
    }

    public void setDetail(JsonObject detail) {
        this.detail = detail;
    }
}
