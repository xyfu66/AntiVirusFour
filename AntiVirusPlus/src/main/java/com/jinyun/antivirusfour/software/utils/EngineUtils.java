package com.jinyun.antivirusfour.software.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.jinyun.antivirusfour.software.entity.AppInfo;
import com.stericson.RootTools.RootTools;


/**
 * 当选中某个条目时，隐藏在下面的线性布局就会弹出
 * 展示 启动 卸载 分享 设置
 * 业务工具类
 * */
public class EngineUtils {
    /**
     * 分享应用
     */
    public static void shareApplication(Context context, AppInfo appInfo){
        Intent intent=new Intent("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"推荐您使用："+appInfo.appName
                +"下载路径：http://play.google.com/store/apps/details?id="+appInfo.packageName);
        context.startActivity(intent);
    }
    /**
     * 开启应用程序
     */
    public static void startApplication(Context context, AppInfo appInfo){
        //打开这个应用程序入口 activity
        PackageManager pm=context.getPackageManager();
        Intent intent=pm.getLaunchIntentForPackage(appInfo.packageName);
        if(intent!=null){
            context.startActivity(intent);
        }else {
            Toast.makeText(context,"该应用没有启动界面", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 开启应用设置页面
     * @param context
     * @param appInfo
     */
    public static void SettingAppDetail(Context context, AppInfo appInfo){
        Intent intent=new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:"+appInfo.packageName));
        context.startActivity(intent);
    }
    /** 卸载应用 */
    public static void uninstallAppliction(Context context, AppInfo appInfo){
        if(appInfo.isUserApp){
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:"+appInfo.packageName));
            context.startActivity(intent);
        }else {
            //系统应用需要root权限，利用linux命令删除文件
            if(!RootTools.isRootAvailable()){
                Toast.makeText(context,"卸载系统应用，必须要root权限", Toast.LENGTH_SHORT).show();
                return;
            }
            try{
                if(!RootTools.isAccessGiven()){
                    Toast.makeText(context,"请授权root权限", Toast.LENGTH_SHORT).show();
                    return;
                }
                RootTools.sendShell("mount -o remount ,rw /system",3000);
                RootTools.sendShell("rm -r"+appInfo.apkPath,3000);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
