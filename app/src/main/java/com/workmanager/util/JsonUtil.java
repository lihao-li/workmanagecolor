package com.workmanager.util;

import org.json.JSONObject;

public class JsonUtil {

    public static JSONObject JosnStringtoObject(String jsonString){
        JSONObject jsonObject=null;
        try {
            jsonObject=new JSONObject(jsonString);
        } catch (Exception e) {
        }
        return jsonObject;
    }
}
