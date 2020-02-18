package com.workmanager.network;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.workmanager.bean.thingBean;
import com.workmanager.entity.Configuration;
import com.workmanager.entity.DryingEntity;
import com.workmanager.entity.FieldEntity;
import com.workmanager.entity.MapFieldEntity;
import com.workmanager.entity.WeightEntity;
import com.workmanager.function.GetDataBase;
import com.workmanager.util.FieldUtil;
import com.workmanager.util.JsonUtil;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class ThingSeletct {
    public thingBean thing =new thingBean();
    public ThingSeletct(){
        Call<thingBean> call = Network.thingSelect().thingSelect(Configuration.url);
        try {
            Response<thingBean> response =call.execute();
            if (response.isSuccessful()){
                thingBean result = response.body();
                if (result != null) {
                    for (int i=0;i<result.getData().size();i++){
                        JsonObject j = (JsonObject)result.getData().get(i);
                        if (j.get("typeId").getAsInt()==1){
                            getfield(j.get("thingId").getAsInt(),j.get("thingInfo").getAsJsonObject(),j.get("nfcTagId").getAsString());
                        }else if (j.get("typeId").getAsInt()==2){
                            getweight(j.get("thingId").getAsInt(),j.get("thingInfo").getAsJsonObject(),j.get("nfcTagId").getAsString());
                        }else if (j.get("typeId").getAsInt()==3){
                            getdrying(j.get("thingId").getAsInt(),j.get("thingInfo").getAsJsonObject(),j.get("nfcTagId").getAsString());
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getfield(int thingId,JsonObject field,String nfctagid){
        FieldEntity fieldlist = new FieldEntity();
        fieldlist.setThingId(thingId);
        fieldlist.setBaseFieldId(field.get("ID").getAsInt());
        fieldlist.setName(field.get("Name").getAsString());
        fieldlist.setAddress(field.get("Address").getAsString());
        fieldlist.setOwner(field.get("Owner").getAsString());
        fieldlist.setNcfTagId(nfctagid);
        JsonArray Arrayshape= field.get("Shape").getAsJsonArray();
        JSONObject shape=JsonUtil.JosnStringtoObject(FieldUtil.getshape(Arrayshape));

        //地图颜色添加
        MapFieldEntity mapfieldlist=new MapFieldEntity();
        mapfieldlist.setThingId(thingId);
        mapfieldlist.setNcfTagId(nfctagid);
        mapfieldlist.setArrayshape(Arrayshape);

        try {
            fieldlist.setLat(shape.getDouble("Lat"));
            fieldlist.setLon(shape.getDouble("Lon"));
        }catch (Exception e){
            e.printStackTrace();
        }
        GetDataBase.fieldList.add(fieldlist);
        GetDataBase.mapfieldList.add(mapfieldlist);
    }
    private void getdrying(int thingId,JsonObject drying,String nfctagid){
        DryingEntity dry = new DryingEntity();
        dry.setThingId(thingId);
        dry.setDrierName(drying.get("Name").getAsString());
        dry.setNcfTagId(nfctagid);
        GetDataBase.dryingList.add(dry);
    }
    private void getweight(int thingId,JsonObject weight,String nfctagid){
        WeightEntity we = new WeightEntity();
        we.setThingId(thingId);
        we.setWeighStationName(weight.get("Name").getAsString());
        we.setNcfTagId(nfctagid);
        GetDataBase.weightList.add(we);
    }

}

