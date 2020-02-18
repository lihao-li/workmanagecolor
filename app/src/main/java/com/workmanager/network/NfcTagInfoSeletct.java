package com.workmanager.network;

import com.google.gson.JsonObject;
import com.workmanager.bean.NFCBean;
import com.workmanager.entity.Configuration;
import com.workmanager.entity.NFCEntity;
import com.workmanager.function.GetDataBase;
import retrofit2.Call;
import retrofit2.Response;

public class NfcTagInfoSeletct {
    public NfcTagInfoSeletct(){
        Call<NFCBean> call = Network.nfcTagInfoSeletct().nfcTagInfoSelect(Configuration.url);
        try {
            Response<NFCBean> response=call.execute();
            if (response.isSuccessful()){
                NFCBean result = response.body();
                if (result != null) {
                    for (int i=0;i<result.getData().size();i++){
                        NFCEntity nfc = new NFCEntity();
                        JsonObject json= (JsonObject)result.getData().get(i);
                        nfc.setNfcTagId(json.get("nfcTagId").getAsString());
                        GetDataBase.nfcList .add(nfc);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
