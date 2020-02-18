package com.workmanager.activity;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.necer.ndialog.ConfirmDialog;
import com.workmanager.R;
import com.workmanager.bean.AssociateBean;
import com.workmanager.bean.INBean;
import com.workmanager.function.BindingFunction;
import com.workmanager.function.GetDataBase;
import com.workmanager.function.TaskFunction;
import com.workmanager.network.Associate;
import com.workmanager.util.DateUtil;
import com.workmanager.util.RequestCode;

public class WeightBindingActivity extends BaseNFCActivity implements View.OnClickListener {
    private TextView mWeightBindingCancel;
    private TextView mWeightBindingConfirm;
    private TextView mWeightValue;
    private TextView mWeightNFCValue;
    private INBean wData= new INBean();
    private static AssociateBean asdata = new AssociateBean();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_binding);
        mWeightBindingCancel = (TextView)findViewById(R.id.Weight_Binding_Cancel);
        mWeightBindingCancel.setOnClickListener(this);
        mWeightBindingConfirm=(TextView)findViewById(R.id.Weight_Binding_Confirm);
        mWeightBindingConfirm.setOnClickListener(this);
        mWeightValue=(TextView)findViewById(R.id.Weight_Value);
        mWeightValue.setOnClickListener(this);
        mWeightNFCValue=(TextView)findViewById(R.id.Weight_NFC_Value);
        mWeightNFCValue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Weight_Binding_Cancel:
                finish();
                break;
            case R.id.Weight_Binding_Confirm:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = Message.obtain();
                        asdata.setThingId(wData.getThingid());
                        asdata.setNfcTagId(wData.getNfcId());
                        asdata.setUpdateFlg(0);
                        Associate as = new Associate(asdata);
                        if (as.status==0){
                            BindingFunction.weightUpdate(asdata);
                            finish();
                        }else {
                            msg.what=1;
                        }
                        mHandler.sendMessage(msg);
                    }
                }).start();
                break;
            case R.id.Weight_Value:
                Intent IntentMNFC = new Intent(WeightBindingActivity.this, MNFCActivity.class);
                IntentMNFC.putExtra("data", TaskFunction.win());
                IntentMNFC.putExtra("title", "計量場リスト");
                startActivityForResult(IntentMNFC, RequestCode.REQUESTCODE_WEIGHT);
                break;
            case R.id.Weight_NFC_Value:
                if (!wData.getNfcId().equals("")){
                    new ConfirmDialog(WeightBindingActivity.this, true)
                            .setTtitle("確認")
                            .setMessage("紐付け関係解除します\nよろしいですか？")
                            .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Message msg = Message.obtain();
                                            asdata.setThingId(wData.getThingid());
                                            asdata.setNfcTagId(wData.getNfcId());
                                            asdata.setUpdateFlg(2);
                                            Associate as = new Associate(asdata);
                                            if (as.status==0){
                                                BindingFunction.weightDelete(asdata);
                                                msg.what=0;
                                            }else {
                                                msg.what=2;
                                            }
                                            mHandler.sendMessage(msg);
                                        }
                                    }).start();
                                }
                            })
                            .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                }else {
                    new ConfirmDialog(WeightBindingActivity.this, true)
                            .setTtitle("確認")
                            .setMessage("入力方式を選択します")
                            .setPositiveButton("NFC入力", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent IntentNFC = new Intent(WeightBindingActivity.this, NfcBindingActivity.class);
                                    startActivityForResult(IntentNFC,RequestCode.REQUESTCODE_WEIGHT_NFC);
                                }
                            })
                            .setNegativeButton("手動入力", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent IntentMNFC = new Intent(WeightBindingActivity.this, MNFCActivity.class);
                                    IntentMNFC.putExtra("data", TaskFunction.nin());
                                    IntentMNFC.putExtra("title", "");
                                    startActivityForResult(IntentMNFC,RequestCode.REQUESTCODE_WEIGHT_MNFC);
                                }
                            }).create().show();
                }
                break;

            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RequestCode.REQUESTCODE_WEIGHT:
                if (resultCode == RESULT_OK) {
                    INBean in = (INBean) data.getSerializableExtra("data");
                    wData.setNfcId(in.getNfcId());
                    wData.setName(in.getName());
                    wData.setThingid(in.getThingid());
                    mWeightValue.setText(wData.getName());
                    mWeightNFCValue.setText(wData.getNfcId());
                }
                break;
            case RequestCode.REQUESTCODE_WEIGHT_MNFC:
                if (resultCode == RESULT_OK) {
                    INBean in = (INBean) data.getSerializableExtra("data");
                    wData.setNfcId(in.getNfcId());
                    mWeightNFCValue.setText(wData.getNfcId());
                }
                break;
            case RequestCode.REQUESTCODE_WEIGHT_NFC:
                if (resultCode == RESULT_OK) {
                    wData.setNfcId(data.getStringExtra("ID"));
                    mWeightNFCValue.setText(wData.getNfcId());
                }
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {

    }

    private void dialog(){
        new ConfirmDialog(WeightBindingActivity.this, true)
                .setTtitle("確認")
                .setMessage("更新失败。")
                .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setCancelable(false).create().show();
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    wData.setNfcId("");
                    mWeightNFCValue.setText("NFCタグIDを入力する");
                    break;
                case 1:
                    wData.setNfcId("");
                    mWeightNFCValue.setText("NFCタグIDを入力する");
                    dialog();
                    break;
                case 2:
                    dialog();
                    break;
            }
        }
    };


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
        if (!DateUtil.sen(GetDataBase.lastlogindata)){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
