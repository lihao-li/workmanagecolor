package com.workmanager.network.i;

import com.workmanager.bean.DeviceBean;
import com.workmanager.bean.StatusBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IdeviceInfoUpdate {
    @Headers({"Content-Type: application/json","Accept: application/json","Cache-Control:public,max-age=300"})
    @POST("/{path}/deviceInfoUpdate")
    Call<StatusBean> deviceInfoUpdate(@Path("path") String path, @Header("Cookie") String cookie, @Body DeviceBean device);
}
