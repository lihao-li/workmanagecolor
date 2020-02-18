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
import com.workmanager.bean.INBean;
import com.workmanager.util.KeyWordUtil;

import java.util.ArrayList;

public class SearchListViewAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<INBean> mUserList=new ArrayList<>();
	private String searchContent;

	public SearchListViewAdapter(Context context, ArrayList<INBean> mUserList, String searchContent) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		String ID = mUserList.get(position).getName();
		View view;
		ViewHolder viewHolder;
		if(convertView==null){
			view= LayoutInflater.from(context).inflate(R.layout.nfc_item,null);
			viewHolder=new ViewHolder();
			viewHolder.NFC_ID=(TextView) view.findViewById(R.id.NFC_ID);
			view.setTag(viewHolder);
		}else{
			view=convertView;
			viewHolder=(ViewHolder) view.getTag();
		}
		if(ID!=null&&ID.length()>0){
			SpannableString number= KeyWordUtil.matcherSearchTitle(Color.parseColor("#ff9314"), ID+"", searchContent);
			viewHolder.NFC_ID.setText(number);
		}else {
			viewHolder.NFC_ID.setText(ID);
		}
		return view;
	}

	class  ViewHolder{
		TextView NFC_ID;
	}

}
