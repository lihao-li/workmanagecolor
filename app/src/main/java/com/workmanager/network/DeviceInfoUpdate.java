package com.workmanager.network;

import com.workmanager.bean.DeviceBean;
import com.workmanager.bean.StatusBean;
import com.workmanager.entity.Configuration;
import com.workmanager.function.GetDataBase;

import retrofit2.Call;
import retrofit2.Response;

public class DeviceInfoUpdate {
    public static int status=-1;
    public DeviceInfoUpdate(){
        DeviceBean dev = new DeviceBean();
        dev.setImei(GetDataBase.device.getImei());
        dev.setAppVersion(GetDataBase.device.getVersionName());
        dev.setDeviceType(GetDataBase.device.getDeviceType());
        dev.setDeviceName(GetDataBase.device.getDeviceName());
        dev.setOsVersion(GetDataBase.device.getOsVersion());
        try {
            Call<StatusBean> call = Network.deviceInfoUpdate().deviceInfoUpdate(Configuration.url,GetDataBase.cookie,dev);
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
