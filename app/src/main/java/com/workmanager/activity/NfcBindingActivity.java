package com.workmanager.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.necer.ndialog.ConfirmDialog;
import com.workmanager.R;
import com.workmanager.function.GetDataBase;
import com.workmanager.util.CommonUtil;
import com.workmanager.util.DateUtil;

public class NfcBindingActivity  extends BaseNFCActivity implements View.OnClickListener {
    private TextView mBindingCancel;
    boolean key=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        mBindingCancel=(TextView)findViewById(R.id.Binding_Cancel);
        mBindingCancel.setOnClickListener(this);


    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.Binding_Cancel:
                finish();
                break;

            default:
                break;
        }
    }

    private String nfcId;
    @Override
    protected void onNewIntent(Intent intent) {
        if (key){
            key=false;
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Ndef ndef = Ndef.get(detectedTag);
        nfcId=CommonUtil.readNfcTag(intent);
        new ConfirmDialog(NfcBindingActivity.this, true)
                .setTtitle("確認")
                .setMessage(String.format("NFCタグID:%sに紐づけます\nよろしいですか？",nfcId ))
                .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent data = new Intent();
                        data.putExtra("ID", nfcId);
                        setResult(RESULT_OK,data);
                        finish();
                    }
                })
                .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        key=true;
                    }
                }).create().show();
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
