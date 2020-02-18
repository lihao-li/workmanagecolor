package com.workmanager.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.necer.ndialog.ConfirmDialog;
import com.workmanager.R;
import com.workmanager.function.GetDataBase;
import com.workmanager.function.SharedPreferencesFunction;
import com.workmanager.network.DeviceInfoUpdate;
import com.workmanager.network.Login;
import com.workmanager.network.NfcTagInfoSeletct;
import com.workmanager.network.ThingSeletct;
import com.workmanager.util.CommonUtil;
import com.workmanager.util.ProgressDialogUtil;

import java.util.Date;

public class LoginActivity extends BaseNFCActivity implements View.OnClickListener {
    private TextView mLoginCancel;
    private TextView mLoginConfirm;
    private TextView  mLogin;
    private EditText mPwdText;
    public static SharedPreferences login;
    public static SharedPreferences workInfo;
    public static SharedPreferencesFunction spf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginCancel=(TextView)findViewById(R.id.Login_Cancel);
        mLoginCancel.setOnClickListener(this);
        mLoginConfirm=(TextView)findViewById(R.id.Login_Confirm);
        mLoginConfirm.setOnClickListener(this);
        mLogin=(TextView)findViewById(R.id.login_value);
        mPwdText=(EditText)findViewById(R.id.pwd_text);
        mPwdText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!CommonUtil.ispass(mPwdText.getText().toString())){
                    new ConfirmDialog(LoginActivity.this, true)
                            .setTtitle("確認")
                            .setMessage("半角英数で入力してください")
                            .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).setCancelable(false).create().show();
                }
            }

        });
        login=getSharedPreferences("login", MODE_PRIVATE );
        workInfo= getSharedPreferences("workInfo", MODE_MULTI_PROCESS);
        if (!login.getString("loginid", "").equals("")){
            mLogin.setText(login.getString("loginid", ""));
        }
        spf=new SharedPreferencesFunction(LoginActivity.workInfo);
    }

    private Dialog dialog;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Login_Cancel:
                finish();
                break;
            case R.id.Login_Confirm:
                if (mLogin.getText().toString().equals("") || mPwdText.getText().toString().length() == 0) {
                    new ConfirmDialog(LoginActivity.this, true)
                            .setTtitle("確認")
                            .setMessage("アカウントを入力してください。")
                            .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }

                            }).setCancelable(false).create().show();
                    return;
                } else if (mPwdText.getText().toString().equals("") || mPwdText.getText().toString().length() == 0) {
                    new ConfirmDialog(LoginActivity.this, true)
                            .setTtitle("確認")
                            .setMessage("パスワードを入力してください。")
                            .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }

                            }).setCancelable(false).create().show();
                } else {
                    dialog= ProgressDialogUtil.createLoadingDialog(LoginActivity.this, "Loading...",false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Login log = new Login(mLogin.getText().toString(),mPwdText.getText().toString());
                            switch (log.loginstatus.getStatus()){
                                case 0:
                                    new DeviceInfoUpdate();
                                    GetDataBase.lastlogindata=new Date();
                                    login.edit().putString("loginid", mLogin.getText().toString()).commit();
                                    if (log.loginstatus.getAuthorityLevel().equals("3")){
                                        new NfcTagInfoSeletct();
                                        new ThingSeletct();
                                        Intent IntentFieldBinding = new Intent(LoginActivity.this, MMainActivity.class);
                                        startActivity(IntentFieldBinding);
                                        dialog.dismiss();
                                        finish();
                                    }else if (log.loginstatus.getAuthorityLevel().equals("2")){
                                        new ThingSeletct();
                                        Intent IntentWork = new Intent(LoginActivity.this, UMainActivity.class);
                                        startActivity(IntentWork);
                                        dialog.dismiss();
                                        finish();
                                    }else {
                                        dialog.dismiss();
                                        Looper.prepare();
                                        new ConfirmDialog(LoginActivity.this, true)
                                                .setTtitle("確認")
                                                .setMessage("ログインIDが未登録。アカウントとパスワードを確認してください。")
                                                .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                }).setCancelable(false).create().show();
                                        Looper.loop();
                                    }
                                    break;
                                case -1:
                                    dialog.dismiss();
                                    dialog();
                                    break;
                                case -100:
                                    dialog.dismiss();
                                    Looper.prepare();
                                    new ConfirmDialog(LoginActivity.this, true)
                                            .setTtitle("確認")
                                            .setMessage("ログインIDが未登録。アカウントとパスワードを確認してください。")
                                            .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }).setCancelable(false).create().show();
                                    Looper.loop();
                                    break;
                                case -200:
                                    dialog.dismiss();
                                    dialog();
                                    break;
                                case -300:
                                    dialog.dismiss();
                                    Looper.prepare();
                                    new ConfirmDialog(LoginActivity.this, true)
                                            .setTtitle("確認")
                                            .setMessage("有効期限切れ。アカウントとパスワードを確認してください。")
                                            .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            }).setCancelable(false).create().show();
                                    Looper.loop();
                                    break;
                            }
                        }
                    }).start();
                }
                break;
                default:
                    break;
        }
    }
    private void dialog(){
        Looper.prepare();
        new ConfirmDialog(LoginActivity.this, true)
                .setTtitle("確認")
                .setMessage("ログイン失敗しました。アカウントとパスワードを確認してください。")
                .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setCancelable(false).create().show();
        Looper.loop();
    }
    @Override
    protected void onNewIntent(Intent intent) {

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

    @Override
    protected void onResume() {
        super.onResume();
    }
}
