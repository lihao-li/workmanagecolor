package com.workmanager.activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.necer.ndialog.ConfirmDialog;
import com.workmanager.R;
import com.workmanager.function.GetDataBase;
import com.workmanager.function.ListDataFunction;
import com.workmanager.function.SharedPreferencesFunction;
import com.workmanager.function.TaskFunction;
import com.workmanager.function.TaskManageFunction;
import com.workmanager.function.TaskScheduleSeleteFunction;
import com.workmanager.network.TaskScheduleSelete;
import com.workmanager.util.CommonUtil;
import com.workmanager.util.DateUtil;

public class NFCActivity extends BaseNFCActivity implements View.OnClickListener{
    private TextView mWorkConfirm;
    private TextView mWorkTitle;
    private TextView car_name;
    private TextView fieldname;
    private TextView addname;
    private TextView username;
    public static SharedPreferencesFunction spf;
    private boolean Flg=false;
    SharedPreferences workInfo;
    private ListView listView;
    private ListDataFunction ldf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workInfo= getSharedPreferences("workInfo", MODE_PRIVATE);
        spf=new SharedPreferencesFunction(workInfo);
        setContentView(R.layout.activity_work);
        mWorkConfirm=(TextView)findViewById(R.id.Work_Confirm);
        mWorkConfirm.setOnClickListener(this);
        mWorkConfirm.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!GetDataBase.spfE.getTaskID().equals("")){
                    new ConfirmDialog(NFCActivity.this, true)
                            .setTtitle("確認")
                            .setMessage("タスクの終了を確認する")
                            .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    spf.delAllSP();
                                    GetDataBase.spfE.setStatus("1");
                                    spf.writeSharedPreferences(GetDataBase.spfE);
                                    finish();
                                }
                            })
                            .setNegativeButton("いいえ",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                }else {
                    new ConfirmDialog(NFCActivity.this, true)
                            .setTtitle("確認")
                            .setMessage("進行中の任務がない")
                            .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                }
                return true;
            }
        });
        mWorkTitle=(TextView)findViewById(R.id.Work_Title);
        mWorkTitle.setOnClickListener(this);
        car_name=(TextView)findViewById(R.id.car_name);
        fieldname=(TextView)findViewById(R.id.field_name1);
        addname=(TextView)findViewById(R.id.add_name);
        username=(TextView)findViewById(R.id.user_name);
        listView=(ListView)findViewById(R.id.listview);
        Intent data = getIntent();
        if (data!=null) {
            Flg = data.getBooleanExtra("flg", false);
        }
        spf.ReadSharedPreferences();
        if (Flg){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = Message.obtain();
                    TaskScheduleSelete ts= new TaskScheduleSelete(GetDataBase.spfE.getTaskID());
                    if (ts.ts.getStatus()==0){
                        TaskScheduleSeleteFunction.getTaskSchedule(ts.ts);
                        msg.what=1;
                    }else {
                        msg.what=-1;
                    }
                    mHandler.sendMessage(msg);
                }
            }).start();
        }
        getData();
        ldf= new ListDataFunction(this,  spf, listView);
        flg(Flg);
    }

    private void getData(){
        car_name.setText(GetDataBase.device.deviceName);
        fieldname.setText(GetDataBase.spfE.getFieldName());
        addname.setText(GetDataBase.spfE.getAddr());
        username.setText(TaskFunction.findFieldByThingid(GetDataBase.spfE.getThingid()).getOwner());
            if (GetDataBase.spfE.getStatus().equals("1")){
                TaskFunction.taskid(GetDataBase.spfE.getThingid());
                GetDataBase.spfE.setTransportTime(DateUtil.cTime());
                GetDataBase.spfE.setStatus("2");
            }
            spf.writeSharedPreferences( GetDataBase.spfE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DateUtil.sen(GetDataBase.lastlogindata)){
            spf.writeSharedPreferences(GetDataBase.spfE);
            ldf.initData();
        }else {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }


    private void flg(boolean flg){
        spf.ReadSharedPreferences();
        mWorkConfirm.setText("End");
        mWorkConfirm.setTextColor(getResources().getColor(R.color.red));
        if (flg){
            new ConfirmDialog(NFCActivity.this, true)
                    .setTtitle("確認")
                    .setMessage("タスクは全部完了していないため、当ページが終わりません")
                    .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
        }else {
            GetDataBase.spfE.setTransportTime(DateUtil.cTime());
        }
        spf.writeSharedPreferences(GetDataBase.spfE);
        ldf.initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Work_Confirm:
                if (GetDataBase.spfE.getStatus().equals("1")){
                    new ConfirmDialog(NFCActivity.this, true)
                            .setTtitle("確認")
                            .setMessage("タスクは全部完了になります。ご確認ください。")
                            .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    spf.delAllSP();
                                    finish();
                                }
                            })
                            .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();

                }else {
                    new ConfirmDialog(NFCActivity.this, true)
                            .setTtitle("確認")
                            .setMessage("タスクは全部完了していないため、当ページが終わりません")
                            .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                }
                break;
            default:
                break;
        }
    }

    private String nfcId;
    @Override
    protected void onNewIntent(Intent intent) {
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Ndef ndef = Ndef.get(detectedTag);
        nfcId= CommonUtil.readNfcTag(intent);
        saveData(nfcId);
    }

    private void saveData(final String nfcId){
        if (GetDataBase.spfE.getStatus().equals("2")){
            new ConfirmDialog(NFCActivity.this, true)
                    .setTtitle("確認")
                    .setMessage(String.format("「%s」でよろしいですか？", TaskFunction.findWeighByNfcId(nfcId).getWeighStationName()))
                    .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Message msg = Message.obtain();
                                    if (TaskManageFunction.taskSend("2",0)==0){
                                        GetDataBase.spfE.setStatus("3");
                                        GetDataBase.spfE.setiWeightTime(DateUtil.cTime());
                                        GetDataBase.spfE.setiWeightID(TaskFunction.findWeighByNfcId(nfcId).getThingId());
                                        msg.what=0;
                                    }else {
                                        msg.what=-1;
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
            return;
        }
        if (GetDataBase.spfE.getStatus().equals("3")){
            Intent input = new Intent(NFCActivity.this,InputActivity.class);
            startActivity(input);
            spf.writeSharedPreferences(GetDataBase.spfE);
            return;
        }
        if (GetDataBase.spfE.getStatus().equals("4")){
            new ConfirmDialog(NFCActivity.this, true)
                    .setTtitle("確認")
                    .setMessage(String.format("「%s」でよろしいですか？",TaskFunction.findrierByDnfcid(nfcId).getDrierName()))
                    .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Message msg = Message.obtain();
                                    if ( TaskManageFunction.taskSend("4",0)==0){
                                        GetDataBase.spfE.setStatus("1");
                                        GetDataBase.spfE.setDryTime(DateUtil.cTime());
                                        GetDataBase.spfE.setDryID(TaskFunction.findrierByDnfcid(nfcId).getThingId());
                                        msg.what=0;
                                    }else {
                                        msg.what=-1;
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
            return;
        }
    }


    private void dialog(){
        new ConfirmDialog(NFCActivity.this, true)
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
                    spf.writeSharedPreferences(GetDataBase.spfE);
                    ldf.initData();
                    break;
                case 1:
                    getData();
                    spf.writeSharedPreferences(GetDataBase.spfE);
                    ldf.initData();
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


}
