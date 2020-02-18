package com.workmanager.function;

import com.workmanager.entity.DeviceEntity;
import com.workmanager.entity.DryingEntity;
import com.workmanager.entity.FieldEntity;
import com.workmanager.entity.MapFieldEntity;
import com.workmanager.entity.NFCEntity;
import com.workmanager.entity.SPFEntity;
import com.workmanager.entity.WeightEntity;

import java.util.ArrayList;
import java.util.Date;

public class GetDataBase {
    public static ArrayList<WeightEntity> weightList=new ArrayList<>();
    public static ArrayList<DryingEntity> dryingList=new ArrayList<>();
    public static ArrayList<FieldEntity> fieldList=new ArrayList<>();
    public static ArrayList<MapFieldEntity> mapfieldList=new ArrayList<>();
    public static ArrayList<NFCEntity> nfcList = new ArrayList<>();
    public static DeviceEntity device = new DeviceEntity();
    public static SPFEntity spfE= new SPFEntity();
    public static Date lastlogindata;
    public static String cookie;

    public static void deleteall(){
        weightList.clear();
        dryingList.clear();
        fieldList.clear();
        nfcList.clear();
    }
}
