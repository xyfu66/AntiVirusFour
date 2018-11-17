package com.jinyun.antivirusfour.traffic.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.TrafficStats;
import android.os.IBinder;

import com.jinyun.antivirusfour.traffic.db.dao.TrafficDao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 如果TrafficMonitoringService 服务没有开启，则开启服务
 * 该服务的主要作用是获取应用程序的实时流量信息
 */

public class TrafficMonitoringService extends Service {
    private long mOldRxBytes;
    private long mOldTxBytes;
    private TrafficDao dao;
    private SharedPreferences mSP;
    private long usedFlow;
    boolean flag=true;
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    /**
     * 刚启动时统计一次从本机到现在所使用的总流量，并运行子线程
     */
    @Override
    public void onCreate(){
        super.onCreate();
        mOldRxBytes= TrafficStats.getMobileRxBytes();
        mOldTxBytes= TrafficStats.getMobileTxBytes();
        dao=new TrafficDao(this);
        mSP=getSharedPreferences("config",MODE_PRIVATE);
        mThread.start();

    }

    //实时获取流量信息
    private Thread mThread=new Thread(){
        public void run() {
            while (flag) {
                try {
                    Thread.sleep(2000 * 60);//睡眠120秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateTodayGPRS();//向数据库中插入和更新的流量
            }
        }
            private void updateTodayGPRS(){
                //获得已经使用了的流量
                usedFlow=mSP.getLong("usedflow",0);
                Date date=new Date();
                Calendar calendar= Calendar.getInstance();//得到日历
                calendar.setTime(date);
                if(calendar.DAY_OF_MONTH==1 & calendar.HOUR_OF_DAY==0 & calendar.MINUTE<1 & calendar.SECOND<30){
                    usedFlow=0;
                }
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                String dataString=sdf.format(date);
                long moblieGPRS=dao.getMoblieGPRS(dataString);
                long mobileRxBytes= TrafficStats.getMobileRxBytes();
                long mobileTxBytes= TrafficStats.getMobileTxBytes();
                //新产生的流量
                long newGprs=(mobileRxBytes+mobileTxBytes)-mOldRxBytes-mOldTxBytes;
                mOldRxBytes=mobileRxBytes;
                mOldTxBytes=mobileTxBytes;
                if(newGprs<0){
                    //网络切换过
                    newGprs=mobileRxBytes+mobileTxBytes;
                }
                if(moblieGPRS==-1){
                    dao.insertTodayGPRS(newGprs);
                }else{
                    if(moblieGPRS<0){
                        moblieGPRS=0;
                    }
                    dao.UpdateTodayGPRS(moblieGPRS+newGprs);
                }
                usedFlow=usedFlow+newGprs;
                SharedPreferences.Editor edit=mSP.edit();
                edit.putLong("usedflow",usedFlow);
                edit.commit();
        };
    };
    @Override
    public void onDestroy(){
        //服务关闭就销毁
        if(mThread!=null & !mThread.interrupted()){
            flag=false;
            mThread.interrupt();
            mThread=null;
        }
        super.onDestroy();
    }
}

