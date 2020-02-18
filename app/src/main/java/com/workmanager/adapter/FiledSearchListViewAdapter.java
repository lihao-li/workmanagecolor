package com.workmanager.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.workmanager.R;
import com.workmanager.entity.FieldEntity;
import com.workmanager.util.KeyWordUtil;
import java.util.ArrayList;

public class FiledSearchListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FieldEntity> mUserList=new ArrayList<>();
    private String searchContent;
    private ArrayList<Integer> Index =new ArrayList<>();

    public FiledSearchListViewAdapter(Context context, ArrayList<FieldEntity> mUserList, String searchContent) {
        super();
        this.context = context;
        this.mUserList = mUserList;
        this.searchContent = searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    @Override
    public int getCount() {
        return mUserList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder;
            if(convertView==null){
                view= LayoutInflater.from(context).inflate(R.layout.fieldlist_item,null);
          /*      switch (flg){
                    case 0:
                        view.setBackgroundColor(Color.parseColor(color));
                        break;
                    case 1:
                        view.setBackgroundColor(Color.parseColor(color));
                        break;
                    case 2:
                        view.setBackgroundColor(Color.parseColor(color));
                        break;
                }
*/
                viewHolder=new ViewHolder();
                viewHolder.name=(TextView)view.findViewById(R.id.field_name) ;
                viewHolder.add=(TextView) view.findViewById(R.id.field_add);
                viewHolder.user=(TextView) view.findViewById(R.id.field_user);
                viewHolder.nfc=(TextView)view.findViewById(R.id.field_nfc);
                view.setTag(viewHolder);
                convertView=view;
            }else{

                viewHolder=(ViewHolder) convertView.getTag();
            }
           final FieldEntity f=mUserList.get(position);
            if (f!=null){
                String name =f.getName();
                String add = f.getAddress();
                String user = f.getOwner();
                String nfc=f.getNcfTagId();
                if(name!=null&&name.length()>0){
                    SpannableString number= KeyWordUtil.matcherSearchTitle(Color.parseColor("#ff9314"), name+"", searchContent);
                    viewHolder.name.setText(number);
                }else {
                    viewHolder.name.setText(name);
                }
                if(add!=null&&add.length()>0){
                    SpannableString number= KeyWordUtil.matcherSearchTitle(Color.parseColor("#ff9314"), add+"", searchContent);
                    viewHolder.add.setText(number);
                }else {
                    viewHolder.add.setText(add);
                }
                if(user!=null&&user.length()>0){
                    SpannableString number= KeyWordUtil.matcherSearchTitle(Color.parseColor("#ff9314"), user+"", searchContent);
                    viewHolder.user.setText(number);
                }else {
                    viewHolder.user.setText(user);
                }
                if(nfc!=null&&nfc.length()>0){
                    SpannableString number= KeyWordUtil.matcherSearchTitle(Color.parseColor("#ff9314"), nfc+"", searchContent);
                    viewHolder.nfc.setText(number);
                }else {
                    viewHolder.nfc.setText(nfc);
                }
            }
        return convertView;
    }

    class  ViewHolder{
        TextView name;
        TextView add;
        TextView user;
        TextView nfc;
    }

}

