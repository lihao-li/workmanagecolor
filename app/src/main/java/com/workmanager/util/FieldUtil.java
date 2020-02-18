package com.workmanager.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class FieldUtil {
    public static String getshape(JsonArray fe){
        double maxLat=0,minLat=0,maxLon=0,minLon=0;
        maxLat=minLat=fe.get(0).getAsJsonObject().get("Lat").getAsDouble();
        maxLon=minLon=fe.get(0).getAsJsonObject().get("Lon").getAsDouble();
        for (int i=0;i<fe.size();i++){
            JsonObject shape =fe.get(i).getAsJsonObject();
            maxLat=shape.get("Lat").getAsDouble() >maxLat?shape.get("Lat").getAsDouble():maxLat;
            minLat=shape.get("Lat").getAsDouble() <minLat?shape.get("Lat").getAsDouble():minLat;
            maxLon=shape.get("Lon").getAsDouble() >maxLon?shape.get("Lon").getAsDouble():maxLon;
            minLon=shape.get("Lon").getAsDouble() <minLon?shape.get("Lon").getAsDouble():minLon;
        }
        return String.format("{\"Lat\":%f,\"Lon\":%f}",(maxLat+minLat)/2,(maxLon+minLon)/2);
    }
}
