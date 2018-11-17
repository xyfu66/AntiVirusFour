package com.jinyun.antivirusfour.advanced.Fragment;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.advanced.adapter.AppLockAdapter;
import com.jinyun.antivirusfour.advanced.db.dao.AppLockDao;
import com.jinyun.antivirusfour.advanced.entity.AppInfo;
import com.jinyun.antivirusfour.advanced.utils.AppInfoParser;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ENG on 2018/3/18.
 */

public class AppUnLockFragment extends Fragment {
    private TextView mUnLockTV;
    private ListView mUnLockLV;
    List<AppInfo> unlockApps = new ArrayList<AppInfo>();
    private AppLockAdapter adapter;
    private AppLockDao dao;
    private Uri uri = Uri.parse("content://com.jinyun.antivirusfour.applock");
    private List<AppInfo> appInfos;
    private Handler mhandler=new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case 100:
                    unlockApps.clear();
                    unlockApps.addAll(((List<AppInfo>)msg.obj));
                    if(adapter==null){
                        adapter=new AppLockAdapter(unlockApps,getActivity());
                        mUnLockLV.setAdapter(adapter);
                    }else{
                        adapter.notifyDataSetChanged();
                    }
                    mUnLockTV.setText("未加锁应用"+unlockApps.size()+"个");
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container ,Bundle saveInstanceState){
        View view= inflater.inflate(R.layout.fragment_appunlock,null);
        mUnLockTV=(TextView) view.findViewById(R.id.tv_unlock);
        mUnLockLV=(ListView) view.findViewById(R.id.lv_unlock);
        return view;
    }
    @Override
    public void onResume(){
        dao=new AppLockDao(getActivity());
        appInfos= AppInfoParser.getAppInfos(getActivity());
        fillData();
        initListener();
        super.onResume();
        getActivity().getContentResolver().registerContentObserver(uri,true, new ContentObserver(new Handler()){
            @Override
            public void onChange(boolean selfChange){
                fillData();
            }
        });
    }
    public void fillData(){
        final List<AppInfo> aInfos=new ArrayList<AppInfo>();
        new Thread(){
            public void run(){
                for(AppInfo info:appInfos){
                    if(!dao.find(info.packageName)){
                        //未加锁
                        info.isLock=false;
                        aInfos.add(info);
                    }
                }
                Message msg=new Message();
                msg.obj=aInfos;
                msg.what=100;
                mhandler.sendMessage(msg);
            };
        }.start();
    }
    private void initListener(){
        mUnLockLV.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,View view,final int position,long id){
                if(unlockApps.get(position).packageName.equals("com.jinyun.antivirusfour")){
                    return;
                }
                //给应用加锁，播放一个动画效果
                TranslateAnimation ta;
                ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
                ta.setDuration(300);
                view.startAnimation(ta);
                new Thread(){
                    public void run(){
                        try{
                            Thread.sleep(300);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                //程序锁信息被加入到数据库了
                                dao.insert(unlockApps.get(position).packageName);
                                unlockApps.remove(position);
                                adapter.notifyDataSetChanged();//通知界面更新
                            }
                        });
                    };
                }.start();
            }
        });
    }
}
