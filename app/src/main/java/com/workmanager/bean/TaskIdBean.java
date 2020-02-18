package com.workmanager.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskIdBean {
    @SerializedName("taskId")
    @Expose
    private String Taskid;

    public String getTaskid() {
        return Taskid;
    }

    public void setTaskid(String taskid) {
        Taskid = taskid;
    }
}
