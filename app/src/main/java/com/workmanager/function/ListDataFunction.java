package com.workmanager.function;

import android.content.Context;
import android.widget.ListView;

import com.workmanager.adapter.ListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListDataFunction {
    private Context context;
    private SharedPreferencesFunction spf;
    private ListView listView;
    public ListDataFunction(Context context, SharedPreferencesFunction spf, ListView listView){
        this.context=context;
        this.spf=spf;
        this.listView=listView;
    }

    public void initData(){
            spf.ReadSharedPreferences();
        ArrayList<Map<String,String>> list = new ArrayList<>();
        //運搬中
        Map<String,String> tmap = new HashMap<>();
        String ttime=
        tmap.put("action","運搬中");
        tmap.put("remark", String.format("時間:%s", GetDataBase.spfE.getTransportTime().equals("")?"":String.format("(%s)", GetDataBase.spfE.getTransportTime())));
        tmap.put("status","ほ場選択");
        list.add(tmap);
        //計量入庫
        Map<String,String> imap = new HashMap<>();
        imap.put("action","計量入庫");
        imap.put("remark", String.format("時間:%s\n計量場名称:%s", GetDataBase.spfE.getiWeightTime().equals("")?"":String.format("(%s)", GetDataBase.spfE.getiWeightTime()),TaskFunction.findWeighByThingid(GetDataBase.spfE.getiWeightID()).getWeighStationName()));
        imap.put("status","計量場選択");
        list.add(imap);
        //計量出庫
        Map<String,String> omap = new HashMap<>();
        omap.put("action","計量出庫");
        omap.put("remark", String.format("時間:%s\n入車重量(KG):%s\n出車重量(KG):%s\n荷受重量(KG):%s\n水分(%%):%s",
                GetDataBase.spfE.getoWeightTime().equals("")?"":GetDataBase.spfE.getoWeightTime(),
                        GetDataBase.spfE.getiWeight(),
                        GetDataBase.spfE.getoWeight(),
                        GetDataBase.spfE.getWeight(),
                        GetDataBase.spfE.getWater()));
        if (Integer.valueOf((GetDataBase.spfE.getStatus()))>3||(GetDataBase.spfE.getStatus().equals("1")&&!GetDataBase.spfE.getoWeightTime().equals(""))){
            omap.put("status","荷受情報変更");
        }else {
            omap.put("status","荷受情報登録");
        }
        list.add(omap);
        //乾燥中(タスク完了)
        Map<String,String> map = new HashMap<>();
        map.put("action","乾燥待ち");
        map.put("remark", String.format("時間:%s\n乾燥機名称:%s", GetDataBase.spfE.getDryTime().equals("")?"": GetDataBase.spfE.getDryTime(), GetDataBase.spfE.getDryTime(),
                TaskFunction.findDrierByThingid(GetDataBase.spfE.getDryID()).getDrierName()));
        map.put("status","乾燥機選択");
        list.add(map);
        addItem(list);
    }
    public void addItem(ArrayList<Map<String,String>> map) {
        ListViewAdapter listviewadapter = new ListViewAdapter(context,map);
        listView.setAdapter(listviewadapter);
    }
}
