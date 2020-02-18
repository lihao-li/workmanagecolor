package com.workmanager.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.necer.ndialog.ConfirmDialog;

import java.util.List;
import java.util.Locale;


public class SystemUtil {


    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }


    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }


    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }


    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }


    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }


    public static String getIMEI(Context ctx) {
        String imei="";
        TelephonyManager TelephonyMgr = (TelephonyManager)ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (TelephonyMgr != null) {
            imei= TelephonyMgr.getDeviceId();
        }
        return imei;

    }

    public static String getName(){
        BluetoothAdapter  mBluetoothAdapter;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter.getName();
    }

    public static boolean isWiFiActive(final Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
        if (mWifiManager.isWifiEnabled() || ipAddress != 0) {
            return true;
        } else {
            new ConfirmDialog(context, true)
                    .setTtitle("確認")
                    .setMessage("wifiをオンにするかどうか。続行しますか？")
                    .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                   .setCancelable(false).create().show();
            return false;
        }
    }
    public static boolean location(Context context,Activity activity){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String provider;
        Location location=null;
        List<String> list = locationManager.getProviders(true);
       if (list.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        }
        else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            return false;
        }
        return true;
    }
}