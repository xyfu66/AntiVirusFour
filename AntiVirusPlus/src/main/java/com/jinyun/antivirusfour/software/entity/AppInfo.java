package com.jinyun.antivirusfour.software.entity;

import android.graphics.drawable.Drawable;

/**
 * 实体类
 */

public class AppInfo {
    /** 应用程序包名 */
    public String packageName;
    /** 应用程序图标 */
    public Drawable icon;
    /** 应用程序名称 */
    public String appName;
    /** 应用程序路径 */
    public String apkPath;
    /** 应用程序大小 */
    public long appSize;
    /** 是否是手机存储 */
    public boolean isInRoom;
    /** 是否是用户应用 */
    public boolean isUserApp;
    /** 是否选中，默认都为false */
    public boolean isSelected = false;
    /** 拿到App位置字符串 */
    public String getAppLocation(boolean isInRoom) {
        if(isInRoom){
            return "手机内存";
        }else{
            return "外部存储";
        }
    }
}
