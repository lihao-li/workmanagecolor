package com.workmanager.function;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.google.gson.JsonObject;
import com.necer.ndialog.ConfirmDialog;
import com.workmanager.activity.FieldBindingActivity;
import com.workmanager.bean.TaskBean;
import com.workmanager.network.TaskManager;

public class TaskManageFunction {
    public static TaskBean task(String status, int updateflg) {
        TaskBean ta = new TaskBean();
        JsonObject j = new JsonObject();
        if (status.equals("1")) {
            j.addProperty("car", GetDataBase.device.getDeviceName());
            j.addProperty("fieldName", GetDataBase.spfE.getFieldName());
            j.addProperty("addr", GetDataBase.spfE.getAddr());
            j.addProperty("thingId", GetDataBase.spfE.getThingid());
            ta.setNfcTagId(TaskFunction.findFieldByThingid(GetDataBase.spfE.getThingid()).getNcfTagId());
            ta.setStatusId("1");
            ta.setUpdateFlg(updateflg);
        } else if (status.equals("2")) {
            j.addProperty("iWeightID", GetDataBase.spfE.getiWeightID());
            ta.setNfcTagId(TaskFunction.findWeighByThingid(GetDataBase.spfE.getiWeightID()).getNcfTagId());
            ta.setStatusId("2");
            ta.setUpdateFlg(updateflg);
        } else if (status.equals("3")) {
            j.addProperty("iWeight", GetDataBase.spfE.getiWeight());
            j.addProperty("oWeightID", GetDataBase.spfE.getoWeightID());
            j.addProperty("oWeight", GetDataBase.spfE.getoWeight());
            j.addProperty("water", GetDataBase.spfE.getWater());
            ta.setNfcTagId(TaskFunction.findWeighByThingid(GetDataBase.spfE.getiWeightID()).getNcfTagId());
            ta.setStatusId("3");
            ta.setUpdateFlg(updateflg);
        } else if (status.equals("4")) {
            j.addProperty("dryID", GetDataBase.spfE.getDryID());
            ta.setNfcTagId(TaskFunction.findDrierByThingid(GetDataBase.spfE.getDryID()).getNcfTagId());
            ta.setStatusId("4");
            ta.setUpdateFlg(updateflg);
        }
        ta.setDetail(j);
        ta.setImei(GetDataBase.device.getImei());
        ta.setTaskId(GetDataBase.spfE.getTaskID());
        return ta;
    }

    public static int taskSend(final String status, int update){
        if (status.equals("2")){
            if (update==0){
                TaskManager t1=new TaskManager(TaskManageFunction.task("1",0));
                TaskManager t2=new TaskManager(TaskManageFunction.task("2",1));
                if (t1.status==-1||t2.status==-1){
                    return -1;
                }
            }else if (update==1){
                TaskManager t2=new TaskManager(TaskManageFunction.task("2",1));
                if (t2.status==-1){
                    return -1;
                }

            }
        }else if (status.equals("3")){
            if (update==0){
                TaskManager t2=new TaskManager(TaskManageFunction.task("3",1));
                if (t2.status==-1){
                    return -1;
                }
            }else if (update==1) {
                TaskManager t2= new TaskManager(TaskManageFunction.task("3", 1));
                if (t2.status==-1){
                    return -1;
                }
            }
        }
        else if (status.equals("4")){
            if (update==0){
                TaskManager t2=new TaskManager(TaskManageFunction.task("4",1));
                if (t2.status==-1){
                    return -1;
                }
            }else if (update==1){
                TaskManager t2=new TaskManager(TaskManageFunction.task("4",1));
                if (t2.status==-1){
                    return -1;
                }
            }
        }
        return 0;
    }
}
