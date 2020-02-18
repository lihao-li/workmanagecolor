package com.workmanager.network;

import com.google.gson.JsonObject;
import com.workmanager.bean.StatusBean;
import com.workmanager.bean.TaskBean;
import com.workmanager.entity.Configuration;
import com.workmanager.function.GetDataBase;

import retrofit2.Call;
import retrofit2.Response;

public class TaskManager {
    public int status=-1;
  public TaskManager(TaskBean task){
      JsonObject j = new JsonObject();
        Call<StatusBean> call = Network.taskManager().taskManager(Configuration.url, GetDataBase.cookie,task);
        try {
            Response<StatusBean> response=call.execute();
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
