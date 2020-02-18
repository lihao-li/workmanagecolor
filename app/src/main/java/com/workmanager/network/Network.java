package com.workmanager.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.workmanager.entity.Configuration;
import com.workmanager.network.i.ILogin;
import com.workmanager.network.i.ITaskManager;
import com.workmanager.network.i.IThingSeletct;
import com.workmanager.network.i.IVersion;
import com.workmanager.network.i.Iassociate;
import com.workmanager.network.i.IdeviceInfoUpdate;
import com.workmanager.network.i.InfcTagInfoSelect;
import com.workmanager.network.i.ItaskScheduleSelete;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {

    private static Network instance;
    private OkHttpClient client;
    private Retrofit retrofit;

    private Network() {
    }


    static {
        instance = new Network();
    }

    private static OkHttpClient getClient() {
        if (instance.client != null)
            return instance.client;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        instance.client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        return instance.client;
    }




    private static Retrofit getRetrofit() {
        if (instance.retrofit != null)
            return instance.retrofit;

        OkHttpClient client = getClient();

        Retrofit.Builder builder = new Retrofit.Builder();
        Gson gson = new GsonBuilder().create();
        instance.retrofit = builder.baseUrl(Configuration.base_url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return instance.retrofit;
    }

    public static ILogin login() {
        return Network.getRetrofit().create(ILogin.class);
    }


    public static IThingSeletct thingSelect(){
        return Network.getRetrofit().create(IThingSeletct.class);
    }

    public static IVersion version(){
        return Network.getRetrofit().create(IVersion.class);
    }

    public static IdeviceInfoUpdate deviceInfoUpdate(){
        return Network.getRetrofit().create(IdeviceInfoUpdate.class);
    }

    public static InfcTagInfoSelect nfcTagInfoSeletct(){
        return Network.getRetrofit().create(InfcTagInfoSelect.class);
    }
    public static Iassociate associate(){
        return Network.getRetrofit().create(Iassociate.class);
    }
    public static ITaskManager taskManager(){
        return Network.getRetrofit().create(ITaskManager.class);
    }
    public static ItaskScheduleSelete taskScheduleSelete(){
        return Network.getRetrofit().create(ItaskScheduleSelete.class);
    }
}
