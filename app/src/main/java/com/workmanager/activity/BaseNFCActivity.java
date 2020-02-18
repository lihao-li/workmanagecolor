package com.workmanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.workmanager.util.NFCUtil;


public class BaseNFCActivity extends Activity {
    protected NFCUtil nfcHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nfcHelper = new NFCUtil(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (nfcHelper.isSupportNFC()) {
            if (nfcHelper.isEnableNFC()) {
                nfcHelper.registerNFC(this);
            } else {
                nfcHelper.showFNCSetting(this);
            }
        } else {
            showToast("NFCに対応しない端末を検出しました。");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        nfcHelper.unRegisterNFC(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    public void start(Class clazz) {
        startActivity(new Intent(this, clazz));
    }

    public void showToast(String content) {
        if (TextUtils.isEmpty(content))
            return;
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    public void log(String content) {
        Log.e(getClass().getSimpleName(), content);
    }

}
