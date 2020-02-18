package com.workmanager.activity;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.necer.ndialog.ConfirmDialog;
import com.workmanager.R;
import com.workmanager.entity.Configuration;
import com.workmanager.entity.VersionEntity;
import com.workmanager.function.GetDataBase;
import com.workmanager.network.DeviceInfoUpdate;
import com.workmanager.network.Version;
import com.workmanager.util.SystemUtil;
import com.workmanager.util.compareUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SplashActivity extends BaseNFCActivity {

    protected static final int UPDATE_VERSION = 100;
    protected static final int ENTER_HOME = 101;
    private static final int sleepTime = 2000;
    @Override
    protected void onCreate(Bundle arg0) {
        final View view = View.inflate(this, R.layout.activity_splash, null);
        setContentView(view);
        super.onCreate(arg0);
        Configuration.base_url=getString(R.string.base_url);
        Configuration.url=getString(R.string.second_url);
        judgePermission();
        Switch();
        getdivice();
    }


    private void getdivice(){
        GetDataBase.device.setDeviceName(SystemUtil.getName());
        GetDataBase.device.setDeviceType("1");
        GetDataBase.device.setImei(SystemUtil.getIMEI(this));
        GetDataBase.device.setOsVersion(SystemUtil.getSystemVersion());
        GetDataBase.device.setVersionCode(getVersionCode());
        GetDataBase.device.setVersionName(getVersionName());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Intent IntentWork = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(IntentWork);
    }
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_VERSION:
                    downloadApk();
                    break;
                case ENTER_HOME:
                    enterHome();
                    break;
                case -100:
                    new ConfirmDialog(SplashActivity.this, true)
                            .setTtitle("確認")
                            .setMessage("wifiをオンにするかどうか。続行しますか？")
                            .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Switch();
                                }
                            })
                            .setCancelable(false).create().show();
                    break;
                case -200:
                    new ConfirmDialog(SplashActivity.this, true)
                            .setTtitle("確認")
                            .setMessage("位置をオンにするかどうか。続行しますか？")
                            .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Switch();
                                }
                            })
                            .setCancelable(false).create().show();
                    break;

            }
        }
    };

    private void downloadApk() {
        ProgressDialog dialog = new ProgressDialog(SplashActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.setTitle("新しいアプリバージョンが見つかりました。更新しますか。");
        dialog.show();
        new Thread(new DownloadApk(dialog)).start();
    }


    private class DownloadApk implements Runnable {
        private ProgressDialog dialog;
        public DownloadApk(ProgressDialog dialog) {
            this.dialog = dialog;
        }
        @Override
        public void run() {
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                try {
                    URL url = new URL(Version.version.getUrl());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    dialog.setMax(conn.getContentLength());
                    InputStream is = conn.getInputStream();
                    File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    int total = 0;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        total += len;
                        dialog.setProgress(total);
                    }
                    fos.close();
                    bis.close();
                    is.close();
                    installApk(file);
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void installApk(File file) {
        Intent intent =new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
        finish();
    }


    protected void enterHome() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkVersion() {
        new Thread(){
            public void run() {
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {

                    Version ver = new Version();
                    VersionEntity s =ver.version;
                    int a =compareUtil.compareVersion(s.getVersionName(),GetDataBase.device.getVersionName());
                    if (a>0){
                        msg.what=UPDATE_VERSION;
                    }else {
                        msg.what = ENTER_HOME;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    long endTime = System.currentTimeMillis();
                    if(endTime-startTime<sleepTime){
                        try {
                            Thread.sleep(sleepTime-(endTime-startTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                }
            };
        }.start();
    }

    private int getVersionCode() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String getVersionName() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    protected void judgePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] SdCardPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, SdCardPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, SdCardPermission, 100);
            }

            String[] readPhoneStatePermission = {Manifest.permission.READ_PHONE_STATE};
            if (ContextCompat.checkSelfPermission(this, readPhoneStatePermission[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, readPhoneStatePermission, 200);
            }

            String[] locationPermission = {Manifest.permission.ACCESS_FINE_LOCATION};
            if (ContextCompat.checkSelfPermission(this, locationPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, locationPermission, 300);
            }

            String[] ACCESS_COARSE_LOCATION = {Manifest.permission.ACCESS_COARSE_LOCATION};
            if (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, ACCESS_COARSE_LOCATION, 400);
            }


            String[] READ_EXTERNAL_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, READ_EXTERNAL_STORAGE, 500);
            }

            String[] WRITE_EXTERNAL_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, WRITE_EXTERNAL_STORAGE, 600);
            }
        }
    }

    private long exitTime = 0;
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次切换后台", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            Toast.makeText(getApplicationContext(), "应用程序已切换到后台", Toast.LENGTH_SHORT).show();
            moveTaskToBack(true);
        }
    }

    private void Switch(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                WifiManager mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
                int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
                if (!mWifiManager.isWifiEnabled() || ipAddress == 0) {
                    msg.what=-100;
                    mHandler.sendMessage(msg);
                    return;
                }
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                List<String> list = locationManager.getProviders(true);
                if (!list.contains(LocationManager.GPS_PROVIDER)&&!list.contains(LocationManager.NETWORK_PROVIDER)) {
                    msg.what=-200;
                    mHandler.sendMessage(msg);
                    return;
                }
               // checkVersion();
                enterHome();
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}