package com.workmanager.activity;

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
import com.workmanager.entity.DryingEntity;
import com.workmanager.function.BindingFunction;
import com.workmanager.function.GetDataBase;
import com.workmanager.function.TaskFunction;
import com.workmanager.network.Associate;
import com.workmanager.util.DateUtil;
import com.workmanager.util.RequestCode;


public class DryingBindingActivity extends BaseNFCActivity implements View.OnClickListener {
    private TextView mDryingBindingCancel;
    private TextView mDryingBindingConfirm;
    private TextView mDryingValue;
    private TextView mDryingNFCValue;
    private INBean dData= new INBean();
    private static AssociateBean asdata = new AssociateBean();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drying_binding);
        mDryingBindingCancel=(TextView)findViewById(R.id.Drying_Binding_Cancel);
        mDryingBindingCancel.setOnClickListener(this);
        mDryingBindingConfirm=(TextView)findViewById(R.id.Drying_Binding_Confirm);
        mDryingBindingConfirm.setOnClickListener(this);
        mDryingValue=(TextView)findViewById(R.id.Drying_Value);
        mDryingValue.setOnClickListener(this);
        mDryingNFCValue=(TextView)findViewById(R.id.Drying_NFC_Value);
        mDryingNFCValue.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Drying_Binding_Cancel:
                finish();
                break;
            case R.id.Drying_Binding_Confirm:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = Message.obtain();
                        asdata.setThingId(dData.getThingid());
                        asdata.setNfcTagId(dData.getNfcId());
                        asdata.setUpdateFlg(0);
                        Associate as = new Associate(asdata);
                        if (as.status==0){
                            BindingFunction.dryUpdate(asdata);
                            finish();
                        }else {
                            msg.what=1;
                        }
                        mHandler.sendMessage(msg);
                    }
                }).start();
                break;
            case R.id.Drying_Value:
                Intent IntentMNFC = new Intent(DryingBindingActivity.this, MNFCActivity.class);
                IntentMNFC.putExtra("data", TaskFunction.din());
                IntentMNFC.putExtra("title", "乾燥機リスト");
                startActivityForResult(IntentMNFC,RequestCode.REQUESTCODE_DRYING);

                break;
            case R.id.Drying_NFC_Value:
                if (!dData.getNfcId().equals("")){
                    new ConfirmDialog(DryingBindingActivity.this, true)
                            .setTtitle("確認")
                            .setMessage("紐付け関係解除します\nよろしいですか？")
                            .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Message msg = Message.obtain();
                                            asdata.setThingId(dData.getThingid());
                                            asdata.setNfcTagId(dData.getNfcId());
                                            asdata.setUpdateFlg(2);
                                            Associate as = new Associate(asdata);
                                            if (as.status==0){
                                                BindingFunction.dryDelete(asdata);
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
                    new ConfirmDialog(DryingBindingActivity.this, true)
                            .setTtitle("確認")
                            .setMessage("入力方式を選択します")
                            .setPositiveButton("NFC入力", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent IntentNFC = new Intent(DryingBindingActivity.this, NfcBindingActivity.class);
                                    startActivityForResult(IntentNFC, RequestCode.REQUESTCODE_DRYING_NFC);
                                }
                            })
                            .setNegativeButton("手動入力", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent IntentMNFC = new Intent(DryingBindingActivity.this, MNFCActivity.class);
                                    IntentMNFC.putExtra("data", TaskFunction.nin());
                                    IntentMNFC.putExtra("title", "");
                                    startActivityForResult(IntentMNFC, RequestCode.REQUESTCODE_DRYING_MNFC);
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
            case RequestCode.REQUESTCODE_DRYING:
                if (resultCode == RESULT_OK) {
                    INBean in = (INBean) data.getSerializableExtra("data");
                    dData.setName(in.getName());
                    dData.setNfcId(in.getNfcId());
                    dData.setThingid(in.getThingid());
                    mDryingValue.setText(dData.getName());
                    mDryingNFCValue.setText(dData.getNfcId());
                }
                break;
            case RequestCode.REQUESTCODE_DRYING_MNFC:
                if (resultCode == RESULT_OK) {
                    INBean in = (INBean) data.getSerializableExtra("data");
                    dData.setNfcId(in.getNfcId());
                    mDryingNFCValue.setText(dData.getNfcId());
                }
                break;
            case RequestCode.REQUESTCODE_DRYING_NFC:
                if (resultCode == RESULT_OK) {
                    dData.setNfcId(data.getStringExtra("ID"));
                    mDryingNFCValue.setText(dData.getNfcId());
                }
                break;
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {

    }

    private void dialog(){
        new ConfirmDialog(DryingBindingActivity.this, true)
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
                    dData.setNfcId("");
                    mDryingNFCValue.setText("NFCタグIDを入力する");
                    break;
                case 1:
                    dialog();
                    dData.setNfcId("");
                    mDryingNFCValue.setText("NFCタグIDを入力する");
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
