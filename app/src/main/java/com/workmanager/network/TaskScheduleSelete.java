package com.workmanager.network;

import com.workmanager.bean.TaskIdBean;
import com.workmanager.bean.TaskScheduleBean;
import com.workmanager.entity.Configuration;
import retrofit2.Call;
import retrofit2.Response;

public class TaskScheduleSelete {
    public TaskScheduleBean ts = new TaskScheduleBean();
    public TaskScheduleSelete(String taskid){
        try {
            TaskIdBean tid= new TaskIdBean();
            tid.setTaskid(taskid);
            Call<TaskScheduleBean> call = Network.taskScheduleSelete().taskScheduleSelete(Configuration.url,tid);
            Response<TaskScheduleBean> response=call.execute();
            if (response.isSuccessful()){
                TaskScheduleBean result = response.body();
                if (result != null) {
                    ts=result;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
