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
import com.workmanager.entity.FieldEntity;
import com.workmanager.function.BindingFunction;
import com.workmanager.function.GetDataBase;
import com.workmanager.function.TaskFunction;
import com.workmanager.network.Associate;
import com.workmanager.util.DateUtil;
import com.workmanager.util.RequestCode;
import java.io.Serializable;

public class FieldBindingActivity extends BaseNFCActivity implements View.OnClickListener {
    private TextView mFieldBindingCancel;
    private TextView mFieldBindingConfirm;
    private TextView mFieldValue;
    private TextView mFieldNFCValue;
    private INBean fData=new INBean();
    private FieldEntity field =new FieldEntity();
    private static AssociateBean asdata = new AssociateBean();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_binding);
        mFieldBindingCancel=(TextView)findViewById(R.id.Field_Binding_Cancel);
        mFieldBindingCancel.setOnClickListener(this);
        mFieldBindingConfirm=(TextView)findViewById(R.id.Field_Binding_Confirm);
        mFieldBindingConfirm.setOnClickListener(this);
        mFieldValue=(TextView)findViewById(R.id.Field_Value);
        mFieldValue.setOnClickListener(this);
        mFieldNFCValue=(TextView)findViewById(R.id.Field_NFC_Value);
        mFieldNFCValue.setOnClickListener(this);
        Intent data = getIntent();
        if(data!=null){
            mFieldValue.setEnabled(data.getBooleanExtra("modify", false));
            if (data.getSerializableExtra("field")!=null){
                FieldEntity field=(FieldEntity)data.getSerializableExtra("field");
                fData.setName(field.getName());
                fData.setNfcId(field.getNcfTagId());
                fData.setThingid(field.getThingId());
                mFieldValue.setText(fData.getName());
                mFieldNFCValue.setText(fData.getNfcId());
            }
        }

    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.Field_Binding_Cancel:
                finish();
                break;
            case R.id.Field_Binding_Confirm:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = Message.obtain();
                        asdata.setThingId(fData.getThingid());
                        asdata.setNfcTagId(fData.getNfcId());
                        asdata.setUpdateFlg(0);
                        Associate as = new Associate(asdata);
                        if (as.status==0){
                            BindingFunction.fieldUpdate(asdata);
                            finish();
                        }else {
                            msg.what=1;
                        }
                        mHandler.sendMessage(msg);

                    }
                }).start();
                break;
            case R.id.Field_Value:
                final Intent IntentMNFC = new Intent(FieldBindingActivity.this, FieldListActivity.class);
                IntentMNFC.putExtra("title", "ほ場リスト");
                startActivityForResult(IntentMNFC,RequestCode.REQUESTCODE_FIELD);
                break;
            case R.id.Field_NFC_Value:
                if (!fData.getNfcId().equals("")){
                    new ConfirmDialog(FieldBindingActivity.this, true)
                            .setTtitle("確認")
                            .setMessage("紐付け関係解除します\nよろしいですか？")
                            .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, final int which) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Message msg = Message.obtain();
                                            asdata.setThingId(fData.getThingid());
                                            asdata.setNfcTagId(fData.getNfcId());
                                            asdata.setUpdateFlg(2);
                                            Associate as = new Associate(asdata);
                                            if (as.status==0){
                                                BindingFunction.fieldDelete(asdata);
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
                    new ConfirmDialog(FieldBindingActivity.this, true)
                            .setTtitle("確認")
                            .setMessage("入力方式を選択します")
                            .setPositiveButton("NFC入力", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent IntentNFC = new Intent(FieldBindingActivity.this, NfcBindingActivity.class);
                                    startActivityForResult(IntentNFC, RequestCode.REQUESTCODE_FIELD_NFC);

                                }
                            })
                            .setNegativeButton("手動入力", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent IntentmNFC = new Intent(FieldBindingActivity.this, MNFCActivity.class);
                                    IntentmNFC.putExtra("title", "");
                                    IntentmNFC.putExtra("data", (Serializable) TaskFunction.nin());
                                    startActivityForResult(IntentmNFC, RequestCode.REQUESTCODE_FIELD_MNFC);
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
            case RequestCode.REQUESTCODE_FIELD:
                if (resultCode == RESULT_OK) {
                    field= (FieldEntity) data.getSerializableExtra("field");
                    fData.setName(field.getName());
                    fData.setNfcId(field.getNcfTagId());
                    fData.setThingid(field.getThingId());
                    mFieldValue.setText(fData.getName());
                    mFieldNFCValue.setText(fData.getNfcId());
                }
                break;
            case RequestCode.REQUESTCODE_FIELD_MNFC:
                if (resultCode == RESULT_OK) {
                    INBean in = (INBean) data.getSerializableExtra("data");
                    fData.setNfcId(in.getNfcId());
                    mFieldNFCValue.setText(fData.getNfcId());
                }
                break;
            case RequestCode.REQUESTCODE_FIELD_NFC:
                if (resultCode == RESULT_OK) {
                    fData.setNfcId(data.getStringExtra("ID"));
                    mFieldNFCValue.setText(fData.getNfcId());
                }
                break;

        }
    }
    @Override
    protected void onNewIntent(Intent intent) {

    }

    private void dialog(){
        new ConfirmDialog(FieldBindingActivity.this, true)
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
                    fData.setNfcId("");
                    mFieldNFCValue.setText("NFCタグIDを入力する");
                    break;
                case 1:
                    dialog();
                    fData.setNfcId("");
                    mFieldNFCValue.setText("NFCタグIDを入力する");
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
