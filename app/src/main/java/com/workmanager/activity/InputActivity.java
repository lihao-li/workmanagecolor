package com.workmanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.necer.ndialog.ConfirmDialog;
import com.workmanager.R;
import com.workmanager.bean.TaskBean;
import com.workmanager.function.GetDataBase;
import com.workmanager.function.TaskManageFunction;
import com.workmanager.network.TaskManager;
import com.workmanager.util.CommonUtil;
import com.workmanager.util.DateUtil;

public class InputActivity extends BaseNFCActivity implements View.OnClickListener {
    private TextView Cancel;
    private TextView Comfire;
    private TextView Title;
    private EditText input;
    private EditText output;
    private EditText water;
    private EditText weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        Cancel=(TextView)findViewById(R.id.Input_Cancel);
        Cancel.setOnClickListener(this);
        Comfire=(TextView)findViewById(R.id.Input_Confirm);
        Comfire.setOnClickListener(this);
        Title=(TextView)findViewById(R.id.Input_Title);
        input=(EditText)findViewById(R.id.input);
        output=(EditText)findViewById(R.id.output);
        water=(EditText)findViewById(R.id.water);
        weight=(EditText)findViewById(R.id.weight);
        Intent data =getIntent();
        listen();

    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.Input_Cancel:
                finish();
                break;
            case R.id.Input_Confirm:
                if (CommonUtil.isNumeric(input.getText().toString())&&CommonUtil.isNumeric(output.getText().toString())&&CommonUtil.isNumeric(water.getText().toString())&&CommonUtil.isNumeric(weight.getText().toString())){
                    GetDataBase.spfE.setiWeight(input.getText().toString());
                    GetDataBase.spfE.setoWeight(output.getText().toString());
                    GetDataBase.spfE.setWater(water.getText().toString());
                    GetDataBase.spfE.setWeight(weight.getText().toString());
                    GetDataBase.spfE.setoWeightTime( DateUtil.cTime());
                    GetDataBase.spfE.setoWeightID(GetDataBase.spfE.getiWeightID());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = Message.obtain();
                            if (GetDataBase.spfE.getStatus().equals("3")){
                                if (TaskManageFunction.taskSend("3",0)==0){
                                    GetDataBase.spfE.setStatus("4");
                                    msg.what=0;
                                }else {
                                    msg.what=-1;
                                }
                            }else {
                                if (TaskManageFunction.taskSend("3",1)==0){
                                    msg.what=0;
                                }else {
                                    msg.what=-1;
                                }
                            }
                            mHandler.sendMessage(msg);
                        }
                    }).start();

                }else {
                    new ConfirmDialog(InputActivity.this, true)
                            .setTtitle("確認")
                            .setMessage("数で入力してください")
                            .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).setCancelable(false).create().show();
                }
                break;
                default:
                    break;
        }
    }

    private void dialog(){
        new ConfirmDialog(InputActivity.this, true)
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
                case -1:
                    dialog();
                    break;
                case 0:
                    finish();
                    break;
            }
        }
    };

    private void listen(){
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!CommonUtil.isNumeric(input.getText().toString())){
                    new ConfirmDialog(InputActivity.this, true)
                            .setTtitle("確認")
                            .setMessage("数で入力してください")
                            .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).setCancelable(false).create().show();
                }
            }
        });
        output.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!CommonUtil.isNumeric(output.getText().toString())){
                    new ConfirmDialog(InputActivity.this, true)
                            .setTtitle("確認")
                            .setMessage("数で入力してください")
                            .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).setCancelable(false).create().show();
                }
            }
        });
        water.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!CommonUtil.isNumeric(water.getText().toString())){
                    new ConfirmDialog(InputActivity.this, true)
                            .setTtitle("確認")
                            .setMessage("数で入力してください")
                            .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).setCancelable(false).create().show();
                }
            }
        });
        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!CommonUtil.isNumeric(weight.getText().toString())){
                    new ConfirmDialog(InputActivity.this, true)
                            .setTtitle("確認")
                            .setMessage("数で入力してください")
                            .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).setCancelable(false).create().show();
                }
            }
        });

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
        if (!DateUtil.sen(GetDataBase.lastlogindata)){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
