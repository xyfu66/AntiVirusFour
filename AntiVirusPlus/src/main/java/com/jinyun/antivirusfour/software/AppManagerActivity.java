package com.jinyun.antivirusfour.software;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.software.adapter.AppManagerAdapter;
import com.jinyun.antivirusfour.software.entity.AppInfo;
import com.jinyun.antivirusfour.software.utils.AppInfoParser;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class AppManagerActivity extends Activity implements View.OnClickListener{
    /** 手机剩余内存TextView */
    private TextView mPhoneMemoryTV;
    /** 展示SD卡剩余内存TextView */
    private TextView mSDMemoryTV;
    private ListView mListView;
    private List<AppInfo> appInfos;
    private List<AppInfo> userAppInfos=new ArrayList<AppInfo>();
    private List<AppInfo> systemAppinfos=new ArrayList<AppInfo>();
    private AppManagerAdapter adapter;
    /** 接受应用程序卸载成功的广播 */
    private UninstallRececiver receciver;
    private Handler mHandler=new Handler(){
        public void handleMessage(android.os.Message msg){
            switch(msg.what){
                case 10:
                    if(adapter==null){
                        adapter=new AppManagerAdapter(userAppInfos,systemAppinfos,AppManagerActivity.this);
                    }
                    mListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                case 15:
                    adapter.notifyDataSetChanged();
                    break;
            }
        };
    };
    private TextView mAppNumTV;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_app_manager);
        //注册广播
        receciver=new UninstallRececiver();
        IntentFilter intentFilter=new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(receciver,intentFilter);
        initView();
    }
    /** 初始化控件 */
    private void initView(){

        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.returns_arrow02);
        ((TextView) findViewById(R.id.tv_title)).setText("软件管家");

        mPhoneMemoryTV=(TextView) findViewById(R.id.tv_phonememory_appmanager);
        mSDMemoryTV=(TextView) findViewById(R.id.tv_sdmemory_appmanager);
        mAppNumTV=(TextView) findViewById(R.id.tv_appnumber);
        mListView=(ListView) findViewById(R.id.lv_appmanager);
        //取得手机剩余内存和SD卡剩余内存
        getMemoryFromPhone();
        initData();
        initListener();
    }
    private void initListener(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(adapter!=null){
                    new Thread(){
                        public void run(){
                            AppInfo mappInfo=(AppInfo) adapter.getItem(position);
                            //记住当前条目的状态
                            boolean flag=mappInfo.isSelected;
                            //先将集合中所有条目的AppInfos变成未选中状态
                            for(AppInfo appInfo:userAppInfos){
                                appInfo.isSelected=false;
                            }
                            for(AppInfo appInfo:systemAppinfos){
                                appInfo.isSelected=false;
                            }
                            if(mappInfo!=null){
                                //如果已经选中，则变为未选中
                                if(flag){
                                    mappInfo.isSelected=false;
                                }else{
                                    mappInfo.isSelected=true;
                                }
                                mHandler.sendEmptyMessage(15);
                            }
                        };
                    }.start();
                }
            }
        });

    mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem >= userAppInfos.size() + 1) {
                mAppNumTV.setText("系统程序:" + systemAppinfos.size() + "个");
            } else {
                mAppNumTV.setText("用户程序:" + userAppInfos.size() + "个");
            }
        }
    });
}
    private void initData(){
        appInfos=new ArrayList<AppInfo>();
        new Thread(){
            public void run(){
                appInfos.clear();
                userAppInfos.clear();
                systemAppinfos.clear();
                appInfos.addAll(AppInfoParser.getAppInfos(AppManagerActivity.this));
                for(AppInfo appInfo:appInfos){
                    //如果是用户App
                    if(appInfo.isUserApp){
                        userAppInfos.add(appInfo);
                    }else{
                        systemAppinfos.add(appInfo);
                    }
                }
                mHandler.sendEmptyMessage(10);
            };
        }.start();
    }
    /** 拿到手机和SD卡剩余内存 */
    private void getMemoryFromPhone(){
        long avail_sd= Environment.getExternalStorageDirectory().getFreeSpace();
        long avail_rom= Environment.getDataDirectory().getFreeSpace();
        //格式化内存
        String str_avail_sd= Formatter.formatFileSize(this,avail_sd);
        String str_avail_rom= Formatter.formatFileSize(this,avail_rom);
        mPhoneMemoryTV.setText("剩余手机内存:"+str_avail_rom);
        mSDMemoryTV.setText("剩余SD卡内存:"+str_avail_sd);
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }
    /**
    * 接受应用程序卸载的广播
    * @author admin
    */
    class UninstallRececiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //收到广播了
            initData();
        }
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(receciver);
        receciver = null;
        super.onDestroy();
    }
}