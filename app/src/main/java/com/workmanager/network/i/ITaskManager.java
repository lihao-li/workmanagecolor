package com.workmanager.network.i;

import com.workmanager.bean.StatusBean;
import com.workmanager.bean.TaskBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ITaskManager {
    @Headers({"Content-Type: application/json","Accept: application/json","Cache-Control:public,max-age=300"})
    @POST("/{path}/taskManage")
    Call<StatusBean> taskManager(@Path("path") String path, @Header("Cookie") String cookie, @Body TaskBean task);
}
