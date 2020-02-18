package com.workmanager.function;

import android.content.SharedPreferences;

import com.workmanager.entity.SPFEntity;


public class SharedPreferencesFunction   {
    private static SharedPreferences info;

    public SharedPreferencesFunction(SharedPreferences info){
        this.info=info;
    }
    public void writeSharedPreferences(SPFEntity spfe){
        SharedPreferences.Editor editor = info.edit();
            editor.putString("taskID", spfe.getTaskID());
            editor.putString("car", spfe.getCar());
            editor.putString("fieldName", spfe.getFieldName());
            editor.putString("addr", spfe.getAddr());
            editor.putString("status", spfe.getStatus());
            editor.putInt("fieldID", spfe.getThingid());
            editor.putString("transportTime", spfe.getTransportTime());
            editor.putInt("iWeightID", spfe.getiWeightID());
            editor.putString("iWeight", spfe.getiWeight());
            editor.putString("iWeightTime", spfe.getiWeightTime());
            editor.putInt("oWeightID", spfe.getoWeightID());
            editor.putString("oWeight", spfe.getoWeight());
            editor.putString("oWeightTime", spfe.getoWeightTime());
            editor.putInt("dryID", spfe.getDryID());
            editor.putString("dryTime", spfe.getDryTime());
            editor.putString("water", spfe.getWater());
            editor.putString("weight", spfe.getWeight());
        editor.commit();
    }

    public void ReadSharedPreferences(){
        if (!info.getString("taskID", "").equals("")){
            GetDataBase.spfE.setTaskID(info.getString("taskID", ""));
        }
        if (!info.getString("car", "").equals("")){
            GetDataBase.spfE.setCar(info.getString("car", "car"));
        }
        if (!info.getString("fieldName", "").equals("")){
            GetDataBase.spfE.setFieldName( info.getString("fieldName", ""));
        }
        if (!info.getString("addr", "").equals("")){
            GetDataBase.spfE.setAddr(info.getString("addr", ""));
        }
        if (!info.getString("status", "").equals("")){
            GetDataBase.spfE.setStatus(info.getString("status", ""));
        }
        if (info.getInt("fieldID", 0)!=0){
            GetDataBase.spfE.setThingid(info.getInt("fieldID", 0));
        }
        if (!info.getString("transportTime", "").equals("")){
            GetDataBase.spfE.setTransportTime(info.getString("transportTime", ""));
        }
        if (info.getInt("iWeightID", 0)!=0){
            GetDataBase.spfE.setiWeightID(info.getInt("iWeightID", 0));
        }
        if (!info.getString("iWeight", "").equals("")){
            GetDataBase.spfE.setiWeight(info.getString("iWeight", ""));
        }
        if (!info.getString("iWeightTime", "").equals("")){
            GetDataBase.spfE.setiWeightTime(info.getString("iWeightTime", ""));
        }
        if (info.getInt("oWeightID", 0)!=0){
            GetDataBase.spfE.setoWeightID(info.getInt("oWeightID", 0));
        }
        if (!info.getString("oWeight", "").equals("")){
            GetDataBase.spfE.setoWeight(info.getString("oWeight", ""));
        }
        if (!info.getString("oWeightTime", "").equals("")){
            GetDataBase.spfE.setoWeightTime(info.getString("oWeightTime", ""));
        }
        if (info.getInt("dDryID", 0)!=0){
            GetDataBase.spfE.setDryID(info.getInt("dryID", 0));
        }
        if (!info.getString("dryTime", "").equals("")){
            GetDataBase.spfE.setDryTime(info.getString("dryTime", ""));
        }
        if (!info.getString("water", "").equals("")){
            GetDataBase.spfE.setWater(info.getString("water", ""));
        }
        if (!info.getString("weight", "").equals("")){
            GetDataBase.spfE.setWeight(info.getString("weight", ""));
        }
    }


    public void delAllSP(){
        info.edit().clear().commit();
        GetDataBase.spfE=new SPFEntity();
    }


}
