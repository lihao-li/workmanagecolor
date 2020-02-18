package com.workmanager.function;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.workmanager.bean.StatusBean;
import com.workmanager.bean.TaskScheduleBean;
import com.workmanager.entity.StatusEntity;
import com.workmanager.util.DateUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TaskScheduleSeleteFunction {

    public static void getTaskSchedule(TaskScheduleBean tsb){
        JsonArray jsonArray= tsb.getList();
        ArrayList<StatusEntity> list = new ArrayList<>();
        for(int i=0;i<jsonArray.size();i++){
            JsonObject jo= jsonArray.get(i).getAsJsonObject();
            StatusEntity se= new StatusEntity();
            se.setNfcTagId(jo.get("nfcTagId").getAsString());
            se.setCreateDate(jo.get("createDate").getAsLong());
            se.setUpdateDate(jo.get("updateDate").getAsLong());
            se.setDetail(jo.get("detail").getAsJsonObject());
            list.add(se);
        }
        Collections.sort(list, new Comparator<StatusEntity>() {
            @Override
            public int compare(StatusEntity o1, StatusEntity o2) {
                if(o1.getCreateDate()==0 || o2.getCreateDate()==0){
                    return 0;
                }
                return o1.getCreateDate()>o2.getCreateDate()?1:0;
            }
        });
        for (int i=0;i<list.size();i++){
            JsonObject detail =list.get(i).getDetail();
            String time = DateUtil.toymdhms(list.get(i).getUpdateDate());
            if (i==0){
               GetDataBase.spfE.setThingid(detail.get("thingId").getAsInt());
               GetDataBase.spfE.setFieldName(detail.get("fieldName").getAsString());
               GetDataBase.spfE.setCar(detail.get("car").getAsString());
               GetDataBase.spfE.setAddr(detail.get("addr").getAsString());
               GetDataBase.spfE.setTransportTime(time);
            }else if (i==1){
                GetDataBase.spfE.setiWeightID(detail.get("iWeightID").getAsInt());
                GetDataBase.spfE.setiWeightTime(time);
            }else if (i==2){
                GetDataBase.spfE.setoWeightID(detail.get("oWeightID").getAsInt());
                GetDataBase.spfE.setiWeight(detail.get("iWeight").getAsString());
                GetDataBase.spfE.setoWeight(detail.get("oWeight").getAsString());
                GetDataBase.spfE.setoWeightTime(time);
                GetDataBase.spfE.setWater(detail.get("water").getAsString());
            }else if (i==3){
                GetDataBase.spfE.setDryID(detail.get("dryID").getAsInt());
                GetDataBase.spfE.setDryTime(time);
            }
        }
    }
}
