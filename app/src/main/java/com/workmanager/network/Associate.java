package com.workmanager.network;

import com.workmanager.bean.AssociateBean;
import com.workmanager.bean.StatusBean;
import com.workmanager.entity.Configuration;
import com.workmanager.function.GetDataBase;

import retrofit2.Call;
import retrofit2.Response;

public class Associate {
    public int status=-1;
    public Associate(AssociateBean associate){
        Call<StatusBean> call = Network.associate().associate(Configuration.url,GetDataBase.cookie,associate);
        try {
            Response<StatusBean> response=  call.execute();
            if (response.isSuccessful()){
                StatusBean result = response.body();
                if (result != null) {
                    status=result.getStatus();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
