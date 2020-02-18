package com.workmanager.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String toymdhms(long timestamp){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(Long.parseLong(String.valueOf(timestamp))));
    }

    public static String cTime(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    /**
     *
     * @param s 上次登陆日期
     * @return true 当天，继续执行 fasle返回登陆
     */

    public static boolean sen(Date s){
        boolean flg =true;
        Date c = new Date();
        if (newDate(c).compareTo(newDate(s))==0){
            flg=true;
        }else {
            flg=false;
        }
        return flg;
    }

    public static Date newDate(Date date){
        Date da= null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            da= format.parse(format.format(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        return da;
    }


    public static String taskTime(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(date);
    }

}
