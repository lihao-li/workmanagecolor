package com.workmanager.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.necer.ndialog.ConfirmDialog;
import com.workmanager.R;
import com.workmanager.adapter.SearchListViewAdapter;
import com.workmanager.bean.INBean;
import com.workmanager.function.GetDataBase;
import com.workmanager.function.TaskFunction;
import com.workmanager.util.CommonUtil;
import com.workmanager.util.DateUtil;

import java.io.Serializable;
import java.util.ArrayList;

public class MNFCActivity extends BaseNFCActivity implements View.OnClickListener {
    private TextView mBindingCancel;
    private EditText mBindingSearch;
    private ListView mBindingList;
    private TextView mBindingSearchTxt;
    private TextView mNFcTitle;

    private ArrayList<INBean> dataList= new ArrayList<>();

    private ArrayList<INBean> searchR=new ArrayList<>();
    private String searchContent;
    private SearchListViewAdapter mSearchListViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnfc);
        mBindingCancel=(TextView)findViewById(R.id.mNFc_Binding_Cancel);
        mBindingCancel.setOnClickListener(this);
        mNFcTitle=(TextView)findViewById(R.id.mNFc_Binding_Title);
        Intent data =getIntent();
        if (data!=null){
            if (!data.getStringExtra("title").equals("")){
                mNFcTitle.setText(data.getStringExtra("title"));
            }
            dataList=(ArrayList<INBean>)data.getSerializableExtra("data");
        }

        mBindingSearch=(EditText)findViewById(R.id.mNFc_Binding_Search);
        mBindingSearchTxt=(TextView)findViewById(R.id.mNFc_Binding_SearchTxt);
        mBindingSearchTxt.setOnClickListener(this);
        mBindingList=(ListView)findViewById(R.id.mNFc_Binding_List);


        mBindingSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                search();
            }

        });
        search();

    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.mNFc_Binding_Cancel:
                finish();
                break;
            case R.id.mNFc_Binding_SearchTxt:
                search();
                break;
            default:
                break;
        }

    }


    private TextWatcher textWatcher=new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String content=mBindingSearch.getText().toString();
            if(content.isEmpty()){
                mBindingSearchTxt.setVisibility(View.GONE);
                mBindingList.setVisibility(View.GONE);
            }else{
                mBindingSearchTxt.setVisibility(View.VISIBLE);
                mBindingList.setVisibility(View.VISIBLE);
            }
        }
    };

    private void search() {
        searchContent = mBindingSearch.getText().toString();
        searchR.clear();
        for(INBean d :dataList){
            if (d.getName().indexOf(searchContent)!=-1){
                searchR.add(d);
            }
        }
        mSearchListViewAdapter = new SearchListViewAdapter(MNFCActivity.this, searchR, searchContent);
        mBindingList.setAdapter(mSearchListViewAdapter);
        mBindingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent data = new Intent();
                    data.putExtra("data", (Serializable) searchR.get(i));
                    setResult(RESULT_OK,data);
                 finish();
            }
        });
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case -1:
                   dialog();
                    break;
            }
        }
    };
    private String nfcId;

    @Override
    protected void onNewIntent(Intent intent) {
            nfcId= CommonUtil.readNfcTag(intent);
            mBindingSearch.setText(TaskFunction.findNameByNfcid(dataList,nfcId));
            search();
        }

    private void dialog(){
        new ConfirmDialog(MNFCActivity.this, true)
                .setTtitle("確認")
                .setMessage("更新失败。")
                .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setCancelable(false).create().show();
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
