package com.jinyun.antivirusfour.advanced.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.advanced.entity.AppInfo;

import java.util.List;

/**
 * Created by ENG on 2018/3/18.
 */

public class AppLockAdapter extends BaseAdapter{
    private List<AppInfo> appInfos;
    private Context context;
    /**
     * 构造方法
     * @param appInfos
     * @param context
     */
    public AppLockAdapter(List<AppInfo> appInfos,Context context){
        super();
        this.appInfos=appInfos;
        this.context=context;
    }
    @Override
    public int getCount(){
        //TODO Auto-generated method stub
        return appInfos.size();
    }
    @Override
    public Object getItem(int position){
        //TODO Auto-generated method stub
        return appInfos.get(position);
    }
    @Override
    public long getItemId(int position){
        //TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        ViewHolder holder;
        if(convertView!=null && convertView instanceof RelativeLayout){
            holder=(ViewHolder) convertView.getTag();
        }else{
            holder=new ViewHolder();
            convertView = View.inflate(context, R.layout.item_list_applock,null);
            holder.mAppIconImgv=(ImageView) convertView.findViewById(R.id.imgv_appicon);
            holder.mAppNameTV=(TextView) convertView.findViewById(R.id.tv_appname);
            holder.mLockIcon=(ImageView) convertView.findViewById(R.id.imgv_lock);
            convertView.setTag(holder);
        }
        final AppInfo appInfo= appInfos.get(position);
        holder.mAppIconImgv.setImageDrawable(appInfo.icon);
        holder.mAppNameTV.setText(appInfo.appNmae);
        if(appInfo.isLock){
            //表示当前应用已经加锁
            holder.mLockIcon.setBackgroundResource(R.drawable.lock);
        }
        return convertView;
    }
    static class ViewHolder{
        TextView mAppNameTV;
        ImageView mAppIconImgv;
        /** 控制图片显示加锁还是不加锁 */
        ImageView mLockIcon;
    }
}
