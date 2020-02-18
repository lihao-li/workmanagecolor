package com.workmanager.network.i;

import com.workmanager.bean.NFCBean;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface InfcTagInfoSelect {
    @Headers({"Content-Type: application/json","Accept: application/json","Cache-Control:public,max-age=300"})
    @GET("/{path}/nfcTagInfoSeletct")
    Call<NFCBean> nfcTagInfoSelect(@Path("path") String path);
}
