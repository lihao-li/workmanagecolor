package com.workmanager.network.i;

import com.workmanager.bean.VersionBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface IVersion {
    @Headers({"Content-Type: application/json","Accept: application/json","Cache-Control:public,max-age=300"})
    @GET("/{path}/versionUpCheck")
    Call<VersionBean> versionUpCheck(@Path("path") String path);
}
