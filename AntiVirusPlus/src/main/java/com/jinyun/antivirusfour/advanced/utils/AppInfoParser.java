package com.jinyun.antivirusfour.advanced.utils;

/**
 * Created by ENG on 2018/3/18.
 */

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;


import com.jinyun.antivirusfour.advanced.entity.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具类用来获取应用信息，此类重复
 */
public class AppInfoParser {
    /**
     * 获取手机里面的所有的应用程序
     * @param context 上下文
     * @return
     */
    public static List<AppInfo> getAppInfos(Context context){
        //得到一个包管理器
        PackageManager pm=context.getPackageManager();
        List<PackageInfo> packInfos=pm.getInstalledPackages(0);
        List<AppInfo> appinfos=new ArrayList<AppInfo>();
        for(PackageInfo packInfo:packInfos){
            AppInfo appinfo=new AppInfo();
            String packname=packInfo.packageName;
            appinfo.packageName=packname;
            Drawable icon=packInfo.applicationInfo.loadIcon(pm);
            appinfo.icon=icon;
            String appname=packInfo.applicationInfo.loadLabel(pm).toString();
            appinfo.appNmae=appname;
            //应用程序APK包的路径
            String apkpath=packInfo.applicationInfo.sourceDir;
            appinfo.apkPath=apkpath;
            appinfos.add(appinfo);
            appinfo=null;
        }
        return appinfos;
    }
}
