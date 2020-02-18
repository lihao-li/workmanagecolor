package com.workmanager.network.i;

import com.workmanager.bean.TaskIdBean;
import com.workmanager.bean.TaskScheduleBean;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ItaskScheduleSelete {
    @Headers({"Content-Type: application/json","Accept: application/json","Cache-Control:public,max-age=300"})
    @POST("/{path}/taskScheduleSelete")
    Call<TaskScheduleBean> taskScheduleSelete(@Path("path") String path,@Body TaskIdBean taskId);

}
