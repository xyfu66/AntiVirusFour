package com.jinyun.antivirusfour.advanced.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by ENG on 2018/3/18.
 */

public class UIUtils{
    public static void showToast(final Activity context,final String msg){
        if("main".equals(Thread.currentThread().getName())){
            Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
        }else{
            context.runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
