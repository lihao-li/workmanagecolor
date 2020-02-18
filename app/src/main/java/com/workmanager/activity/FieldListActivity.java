package com.workmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.necer.ndialog.ChoiceDialog;
import com.workmanager.R;
import com.workmanager.adapter.FiledSearchListViewAdapter;
import com.workmanager.entity.FieldEntity;
import com.workmanager.function.GetDataBase;
import com.workmanager.function.TaskFunction;
import com.workmanager.util.CommonUtil;
import com.workmanager.util.DateUtil;
import java.io.Serializable;
import java.util.ArrayList;

public class FieldListActivity extends BaseNFCActivity implements View.OnClickListener {
    private TextView mFieldCancel;
    private ListView mFieldList;
    private EditText mFieldSearch;
    private TextView mFieldSearchTxt;
    private TextView mFieldNum;
    private TextView mFieldSwitch;
    private TextView mFieldTitle;
    private ArrayList<FieldEntity> fieldlist = new ArrayList<>();
    private String searchContent;
    private FiledSearchListViewAdapter mFiledSearchListViewAdapter;
    private ArrayList<FieldEntity> searchR=new ArrayList<>();
    private ArrayList<FieldEntity> sfieldList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_list);
        mFieldCancel=(TextView)findViewById(R.id.field_cancel);
        mFieldCancel.setOnClickListener(this);
        mFieldList=(ListView)findViewById(R.id.Field_list);
        mFieldSearch=(EditText)findViewById(R.id.Field_Search);
        mFieldSearchTxt=(TextView)findViewById(R.id.Field_SearchTxt);
        mFieldSearchTxt.setOnClickListener(this);
        mFieldNum=(TextView)findViewById(R.id.field_num);
        mFieldSwitch=(TextView)findViewById(R.id.field_switch);
        mFieldSwitch.setOnClickListener(this);
        mFieldTitle=(TextView)findViewById(R.id.field_title);

        mFieldSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                search(fieldlist);
            }
        });
        Intent data = getIntent();
        if (data!=null){
            if (!data.getStringExtra("title").equals("")){
                mFieldTitle.setText(data.getStringExtra("title"));
            }
        }
        setData();
    }
    private void setData() {
        try{
            fieldlist= GetDataBase.fieldList;
            search(fieldlist);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.field_cancel:
                finish();
                break;
            case R.id.field_switch:
                new ChoiceDialog(this, true)
                        .setItems(new String[]{"未紐付け","紐付け済","すべて"})
                        .hasCancleButton(true)
                        .setOnItemClickListener(new ChoiceDialog.OnItemClickListener() {
                            @Override
                            public void onItemClick(TextView onClickView, int position) {
                                if (position == 0) {
                                    mFieldSwitch.setText("未紐付け");
                                    sfieldList.clear();
                                    for (FieldEntity f:fieldlist){
                                        if ( f.getNcfTagId().equals("")){
                                            sfieldList.add(f);
                                        }
                                    }
                                    search(sfieldList);
                                }
                                if (position==1){
                                    mFieldSwitch.setText("紐付け済");
                                    sfieldList.clear();
                                    for (FieldEntity f:fieldlist){
                                        if (!f.getNcfTagId().equals("")){
                                            sfieldList.add(f);
                                        }
                                    }
                                    search(sfieldList);
                                }if (position==2){
                                    mFieldSwitch.setText("すべて");
                                    sfieldList.clear();
                                    for (FieldEntity f:fieldlist){
                                            sfieldList.add(f);
                                    }
                                    search(sfieldList);
                                }
                            }
                        }).create().show();
                break;
            default:
                finish();
                break;
        }

    }

    private void search(ArrayList<FieldEntity> list) {
        searchContent = mFieldSearch.getText().toString();
        searchR.clear();
        for (int i = 0;i<list.size();i++){
                if (list.get(i).getName().indexOf(searchContent)!=-1||list.get(i).getAddress().indexOf(searchContent)!=-1||list.get(i).getOwner().indexOf(searchContent)!=-1||list.get(i).getNcfTagId().indexOf(searchContent)!=-1){
                    searchR.add(list.get(i));
                }
        }
        mFieldNum.setText(String.format("対象件数：%d", searchR.size()));
        mFiledSearchListViewAdapter = new FiledSearchListViewAdapter(FieldListActivity.this, searchR, searchContent);
        mFieldList.setAdapter(mFiledSearchListViewAdapter);
        mFieldList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent data= new Intent();
                data.putExtra("field",(Serializable)searchR.get(i));
                setResult(RESULT_OK,data);
                finish();
            }
        });
    }
    private String nfcId;
    @Override
    protected void onNewIntent(Intent intent) {
        nfcId= CommonUtil.readNfcTag(intent);
        mFieldSearch.setText(TaskFunction.findFieldByNfcid(nfcId).getName());
        search(GetDataBase.fieldList);
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
