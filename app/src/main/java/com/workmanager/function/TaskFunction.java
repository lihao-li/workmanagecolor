package com.workmanager.function;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.necer.ndialog.ConfirmDialog;
import com.workmanager.activity.DryActivity;
import com.workmanager.activity.InputActivity;
import com.workmanager.activity.LoginActivity;
import com.workmanager.activity.MNFCActivity;
import com.workmanager.activity.UMainActivity;
import com.workmanager.activity.WeightActivity;
import com.workmanager.bean.INBean;
import com.workmanager.entity.DryingEntity;
import com.workmanager.entity.FieldEntity;
import com.workmanager.entity.NFCEntity;
import com.workmanager.entity.WeightEntity;
import com.workmanager.util.DateUtil;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;



public class TaskFunction {
    public static WeightEntity findWeighByThingid(int id){
        WeightEntity we=new WeightEntity();
        for (WeightEntity list:GetDataBase.weightList){
            if (list.getThingId()==id){
                we=list;
                break;
            }
        }
        return we;
    }

    public static WeightEntity findWeighByNfcId(String nfcid){
        WeightEntity we=new WeightEntity();
        for (WeightEntity list:GetDataBase.weightList){
            if (list.getNcfTagId().equals(nfcid)){
                we=list;
                break;
            }
        }
        return we;
    }

    public static DryingEntity findDrierByThingid(int id){
        DryingEntity de=new DryingEntity();
        for (DryingEntity list:GetDataBase.dryingList){
            if (list.getThingId()==id){
                de=list;
                break;
            }
        }
        return de;
    }

    public static DryingEntity findrierByDnfcid(String nfcic){
        DryingEntity de=new DryingEntity();
        for (DryingEntity list:GetDataBase.dryingList){
            if (list.getNcfTagId().equals(nfcic)){
                de=list;
                break;
            }
        }
        return de;
    }

    public static FieldEntity findFieldByThingid(int id){
        FieldEntity fe=new FieldEntity();
        for (FieldEntity list:GetDataBase.fieldList){
            if (list.getThingId()==id){
                fe=list;
                break;
            }
        }
        return fe;
    }

    public static FieldEntity findFieldByNfcid(String nfcid){
        FieldEntity fe=new FieldEntity();
        for (FieldEntity list:GetDataBase.fieldList){
            if (list.getNcfTagId().equals(nfcid)){
                fe=list;
                break;
            }
        }
        return fe;
    }

    public static void update(final Context context, int position){
        if (position==0){
            if (GetDataBase.spfE.getStatus().equals("2")){
                new ConfirmDialog(context, true)
                        .setTtitle("確認")
                        .setMessage("ほ場変更を行います。ご確認ください")
                        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GetDataBase.spfE.setStatus("1");
                                LoginActivity.spf.writeSharedPreferences(GetDataBase.spfE);
                                Intent uMain=new Intent(context, UMainActivity.class);
                                context.startActivity(uMain);
                            }
                        })
                        .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();

            }else {
                new ConfirmDialog(context, true)
                        .setTtitle("確認")
                        .setMessage("計量入庫以降タスクもう着手しているので、ほ場変更はできません")
                        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create().show();
            }
        }else if(position==1){
            if (GetDataBase.spfE.getStatus().equals("2")){
                selecetWeightStation(context);
            }else if(GetDataBase.spfE.getStatus().equals("3")){
                new ConfirmDialog(context, true)
                        .setTtitle("確認")
                        .setMessage("計量入庫変更を行います。ご確認ください")
                        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selecetWeightStation(context);
                            }
                        })
                        .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }else {
                new ConfirmDialog(context, true)
                        .setTtitle("確認")
                        .setMessage("計量出庫以降タスクもう着手しているので、計量入庫変更はできません")
                        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create().show();
            }
        }else if (position==2){
            if (GetDataBase.spfE.getStatus().equals("3")){
                Intent input = new Intent(context, InputActivity.class);
                context.startActivity(input);
            }else if(GetDataBase.spfE.getStatus().equals("4")){
                new ConfirmDialog(context, true)
                        .setTtitle("確認")
                        .setMessage("計量出庫変更を行います。ご確認ください")
                        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent input = new Intent(context, InputActivity.class);
                                context.startActivity(input);
                            }
                        })
                        .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();

            }else {
                new ConfirmDialog(context, true)
                        .setTtitle("確認")
                        .setMessage("未完の前タスクがあって、先に前タスクをしてください。")
                        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create().show();
            }
        }else if (position==3){
            if (GetDataBase.spfE.getStatus().equals("4")){
                selecetDry(context);
            }else if (GetDataBase.spfE.getStatus().equals("1")){
                new ConfirmDialog(context, true)
                        .setTtitle("確認")
                        .setMessage("乾燥機変更を行います。ご確認ください")
                        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selecetDry(context);
                            }
                        })
                        .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }else {
                new ConfirmDialog(context, true)
                        .setTtitle("確認")
                        .setMessage("未完の前タスクがあって、先に前タスクをしてください。")
                        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create().show();
            }
        }
    }


    public static void selecetWeightStation(Context context){
        Intent w=new Intent(context, WeightActivity.class);
        context.startActivity(w);
    }

    public static void selecetDry(Context context){
        Intent d=new Intent(context, DryActivity.class);
        context.startActivity(d);
    }

    public static ArrayList<INBean> win(){
        ArrayList<INBean> in = new ArrayList<>();
        if (GetDataBase.weightList.size()>0){
            for (WeightEntity w:GetDataBase.weightList){
                INBean b = new INBean();
                b.setName(w.getWeighStationName());
                b.setNfcId(w.getNcfTagId());
                b.setThingid(w.getThingId());
                in.add(b);
            }
        }
        return in;
    }

    public static ArrayList<INBean> din(){
        ArrayList<INBean> in = new ArrayList<>();
        if (GetDataBase.dryingList.size()>0){
            for (DryingEntity w:GetDataBase.dryingList){
                INBean b = new INBean();
                b.setName(w.getDrierName());
                b.setNfcId(w.getNcfTagId());
                b.setThingid(w.getThingId());
                in.add(b);
            }
        }
        return in;
    }

    public static ArrayList<INBean> fin(){
        ArrayList<INBean> in = new ArrayList<>();
        if (GetDataBase.fieldList.size()>0){
            for (FieldEntity w:GetDataBase.fieldList){
                INBean b = new INBean();
                b.setName(w.getName());
                b.setNfcId(w.getNcfTagId());
                b.setThingid(w.getThingId());
                in.add(b);
            }
        }
        return in;
    }

    public static ArrayList<INBean> nin(){
        ArrayList<INBean> in = new ArrayList<>();
        if (GetDataBase.nfcList.size()>0){
            for (NFCEntity w:GetDataBase.nfcList){
                INBean b = new INBean();
                b.setName(w.getNfcTagId());
                b.setNfcId(w.getNfcTagId());
                in.add(b);
            }
        }
        return in;
    }


    public static String findNameByNfcid(ArrayList<INBean> list,String nfcid){
        String re="";
        if (list.size()>0){
            for (INBean i:list){
                if (i.getNfcId().equals(nfcid)){
                    re=i.getName();
                }
            }
        }
        return re;
    }


    public static void taskid(int fieldid){
        String taskid= "";
        DecimalFormat df = new DecimalFormat("000000");
        String s1 =df.format(fieldid);
        String s2 =DateUtil.taskTime();
        taskid=String.format("%s%s", s1, s2);
        GetDataBase.spfE.setTaskID(taskid);
    }




}
