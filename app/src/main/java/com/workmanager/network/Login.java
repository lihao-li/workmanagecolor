package com.workmanager.network;
import com.workmanager.bean.LoginBean;
import com.workmanager.bean.LoginRBean;
import com.workmanager.entity.Configuration;
import com.workmanager.function.GetDataBase;
import com.workmanager.util.Sha256;

import retrofit2.Call;
import retrofit2.Response;

public class Login {
    public static LoginRBean loginstatus=new LoginRBean();
    public Login(String loginId,String password){
        LoginBean rl=new LoginBean();
        rl.setLoginId(loginId);
        rl.setPassWord(Sha256.getSHA256(password));
        Call<LoginRBean> call = Network.login().login(Configuration.url,rl);
        try {
            Response<LoginRBean> response= call.execute();
                LoginRBean result = response.body();
                if (result != null) {
                    if (result.getStatus()==0){
                        GetDataBase.cookie=response.headers().get("Set-Cookie").substring(0,43);
                    }
                    loginstatus=result;
                }
        }catch (Exception e){
            loginstatus.setStatus(-1);
            e.printStackTrace();
        }

    }
}
