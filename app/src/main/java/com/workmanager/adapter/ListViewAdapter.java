package com.workmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.workmanager.R;
import com.workmanager.function.GetDataBase;
import com.workmanager.function.TaskFunction;

import java.util.ArrayList;
import java.util.Map;

public class ListViewAdapter extends BaseAdapter {
    private  ArrayList<Map<String,String>> list;
    private Context context;
    public ListViewAdapter(Context context, ArrayList<Map<String,String>> list) {
        super();
        this.context=context;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView( final int position, View convertView, ViewGroup parent) {
        final Map<String,String> map=list.get(position);
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_timeline, parent, false);

            holder.imageView=(ImageView)convertView.findViewById(R.id.image);
            holder.imageView.setImageResource(R.drawable.wait);
            if ((Integer.valueOf(GetDataBase.spfE.getStatus())-2)>=position||GetDataBase.spfE.getStatus().equals("1")){
                holder.imageView.setImageResource(R.drawable.ic_ok);
            }
            holder.item_workprogress_action = (TextView) convertView.findViewById(R.id.item_workprogress_action);
            holder.item_workprogress_remark = (TextView) convertView.findViewById(R.id.item_workprogress_remark);
            holder.item_workprogress_status = (TextView) convertView.findViewById(R.id.item_workprogress_status);

            holder.item_workprogress_action.setText(map.get("action"));
            holder.item_workprogress_remark.setText(map.get("remark"));
            holder.item_workprogress_status.setText(map.get("status"));
            holder.item_workprogress_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TaskFunction.update(context, position);
                }
            });
        }
        return convertView;
    }
    class ViewHolder {
        private ImageView imageView;
        private TextView item_workprogress_action;
        private TextView item_workprogress_remark;
        private TextView item_workprogress_status;

    }
}
