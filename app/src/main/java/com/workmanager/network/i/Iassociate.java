package com.workmanager.network.i;

import com.workmanager.bean.AssociateBean;
import com.workmanager.bean.StatusBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Iassociate {
    @Headers({"Content-Type: application/json","Accept: application/json","Cache-Control:public,max-age=300"})
    @POST("/{path}/associateOperation")
    Call<StatusBean> associate(@Path ("path") String path,@Header ("Cookie") String cookie, @Body AssociateBean associate);
}
