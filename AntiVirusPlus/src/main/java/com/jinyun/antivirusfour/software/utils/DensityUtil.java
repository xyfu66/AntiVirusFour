package com.jinyun.antivirusfour.software.utils;

import android.content.Context;

/**
 * dip->px
 */

public class DensityUtil {
    /**
     * dip 转化像素 px
     */
    public static int dip2px(Context context, float dpValue){
        try{
            final float scale=context.getResources().getDisplayMetrics().density;
            return (int) (dpValue*scale+0.5f);
        } catch(Exception e){
            e.printStackTrace();
        }
        return (int) dpValue;
    }
    /**
     * 像素px转化为dip
     */
    public static int px2dip(Context context, float pxValue){
        try{
            final float scale=context.getResources().getDisplayMetrics().density;
            return (int) (pxValue/scale+0.5f);
        }catch (Exception e){
            e.printStackTrace();
        }
        return (int) pxValue;
    }
}
