package com.workmanager.network;

import com.google.gson.JsonObject;
import com.workmanager.bean.VersionBean;
import com.workmanager.entity.Configuration;
import com.workmanager.entity.VersionEntity;

import retrofit2.Call;
import retrofit2.Response;

public class Version {
    public static VersionEntity version=new VersionEntity();
    public Version(){
        try {
            Call<VersionBean> call = Network.version().versionUpCheck(Configuration.url);
            Response<VersionBean> response=call.execute();
            if (response.isSuccessful()){
                VersionBean result = response.body();
                if (result != null) {
                    if (result.getStatus()==0){
                        JsonObject j = (JsonObject)result.getData();
                        version.setVersionCode(j.get("versionCode").getAsInt());
                        version.setVersionName(j.get("versionName").getAsString());
                        version.setUrl(j.get("url").getAsString());
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
