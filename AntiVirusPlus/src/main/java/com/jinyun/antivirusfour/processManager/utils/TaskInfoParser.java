package com.jinyun.antivirusfour.processManager.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.processManager.entity.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 进程管理 进程信息的解析器
 */

public class TaskInfoParser {
    /**
     * 或许正在运行的所有进程信息
     * @param context
     * @return
     */
    @SuppressLint("ResourceType")
    public static List<TaskInfo> getRunningTaskInfos(Context context) {
        ActivityManager am= (ActivityManager) context.getSystemService (Context.ACTIVITY_SERVICE);
        //通过包管理器获取正在运行的程序。
        PackageManager pm=context.getPackageManager();
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        List<TaskInfo> taskInfos=new ArrayList<TaskInfo>() ;//用于存储进程对象。

        /**
         * 循环遍历所有正在运行的进程，并获取程序包名、占用内存大小、程序图标、程序名称，
         * 以及是否是用户程序，最后将进程信息封装到TaskInfo对象中并添加到List<TaskInfo>集合。
         */
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            String packname = processInfo.processName;
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setPackageName(packname);//进程名称
            Debug.MemoryInfo[] memroyinfos = am.getProcessMemoryInfo(new int[]{processInfo.pid});
            long task_memory = memroyinfos[0].getTotalPrivateDirty()* 1024;
            taskInfo.setTask_memory(task_memory);//程序占用的内存空间
            try {
                PackageInfo packInfo = pm.getPackageInfo(packname, 0);
                Drawable icon = packInfo.applicationInfo.loadIcon(pm);
                taskInfo.setTask_icon(icon);
                String appname = packInfo.applicationInfo.loadLabel(pm).toString();
                taskInfo.setTask_name(appname);

                if( (ApplicationInfo.FLAG_SYSTEM & packInfo.applicationInfo.flags)!=0){
                    //系统进程
                    taskInfo.isUserApp=false;
                }else{
                    //用户进程
                    taskInfo.isUserApp=true;
                }
            }catch (PackageManager.NameNotFoundException e){
                e.printStackTrace();
                taskInfo.setPackageName(packname);
                taskInfo.setTask_icon(context.getResources().getDrawable(R.id.icon));
            }
            taskInfos.add(taskInfo);
            }
        return taskInfos;
    }
}
