package com.jinyun.antivirusfour.traffic.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * 获取当前程序流量工具类
 */

public class SystemInfoUtils {
    /**
     * 判断一个服务是否处于运行状态
     * @param context 上下文
     * @return
     */
    public static boolean isServiceRunning(Context context, String className){
        ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos=am.getRunningServices(200);
        for(ActivityManager.RunningServiceInfo info:infos){
            String serviceClassName=info.service.getClassName();
            if(className.equals(serviceClassName)){
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @Description: 获取uid上传的流量(wifi+3g/4g)
     * @param uid 程序的uid
     * @return 上传的流量（tcp+udp）  返回-1 表示不支持得机型
     */
    public static long getTxTraffic(int uid) {
        return  TrafficStats.getUidTxBytes(uid);
    }

    /**
     *
     * @Description: 获取uid上传的流量(wifi+3g/4g)  通过读取/proc/uid_stat /uid/tcp_snd文件获取
     * @param uid 程序的uid
     * @return 上传的流量（tcp）  返回-1 表示出现异常
     */
    public static long getTxTcpTraffic(int uid){
        RandomAccessFile rafSnd = null;
        String sndPath = "/proc/uid_stat/" + uid + "/tcp_snd";
        long sndTcpTraffic;
        try {
            rafSnd = new RandomAccessFile(sndPath, "r");
            sndTcpTraffic = Long.parseLong(rafSnd.readLine());
        } catch (FileNotFoundException e) {
            sndTcpTraffic = -1;
        } catch (IOException e) {
            e.printStackTrace();
            sndTcpTraffic = -1;
        } finally {
            try {
                if (rafSnd != null){
                    rafSnd.close();
                }
            } catch (IOException e) {
                sndTcpTraffic = -1;
            }
        }
        return sndTcpTraffic;
    }

    /**
     *
     * @Description: 获取uid下載的流量(wifi+3g/4g)
     * @param uid 程序的uid
     * @return 下載的流量(tcp+udp) 返回-1表示不支持的机型
     */
    public static long getRxTraffic(int uid){
        return  TrafficStats.getUidRxBytes(uid);
    }

    /**
     *
     * @Description: 获取uid上传的流量(wifi+3g/4g) 通过读取/proc/uid_stat/uid/tcp_rcv文件获取
     * @author fengao
     * @param uid 程序的uid
     * @return 下载的流量（tcp）  返回-1 表示出现异常
     */
    public static long getRxTcpTraffic(int uid) {
        RandomAccessFile rafRcv = null; // 用于访问数据记录文件
        String rcvPath = "/proc/uid_stat/" + uid + "/tcp_rcv";
        long rcvTcpTraffic;
        try {
            rafRcv = new RandomAccessFile(rcvPath, "r");
            rcvTcpTraffic = Long.parseLong(rafRcv.readLine()); // 读取流量统计
        } catch (FileNotFoundException e) {
            rcvTcpTraffic = -1;
        } catch (IOException e) {
            rcvTcpTraffic = -1;
        } finally {
            try {
                if (rafRcv != null){
                    rafRcv.close();
                }
            } catch (IOException e) {
                rcvTcpTraffic = -1;
            }
        }
        return rcvTcpTraffic;
    }

    /**
     *
     * @Description: 得到uid的总流量（上传+下载）
     * @author fengao
     * @param uid 程序的uid
     * @return uid的总流量   当设备不支持方法且没有权限访问/proc/uid_stat/uid时 返回-1
     */
    public static long getTotalTraffic(int uid){
        long txTraffic = (getTxTraffic(uid)==-1)?getTxTcpTraffic(uid):getTxTraffic(uid);
        if(txTraffic==-1){
            return -1;
        }
        long rxTraffic = (getRxTraffic(uid)==-1)?getRxTcpTraffic(uid):getRxTraffic(uid);
        if(rxTraffic==-1){
            return -1;
        }
        return txTraffic+rxTraffic;
    }

    /**
     *
     * @Description: 取得程序的uid
     * @param context 上下文
     * @return 当前程序的uid  返回-1表示出现异常
     */
    public static int getUid(Context context) {
        try {
            PackageManager packageManager =context.getPackageManager();
            @SuppressLint("WrongConstant") ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            return applicationInfo.uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
