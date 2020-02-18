package com.workmanager.network.i;

import com.workmanager.bean.LoginBean;
import com.workmanager.bean.LoginRBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ILogin{
    @Headers({"Content-Type: application/json","Accept: application/json","Cache-Control:public,max-age=300"})
    @POST("/{path}/appLogin")
    Call<LoginRBean> login(@Path ("path") String path,@Body LoginBean login);
}
