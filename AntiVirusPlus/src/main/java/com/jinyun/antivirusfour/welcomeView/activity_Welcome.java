package com.jinyun.antivirusfour.welcomeView;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jinyun.antivirusfour.PermissionAcitivity;
import com.jinyun.antivirusfour.R;


public class activity_Welcome extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        //启动一个延迟线程
        new Thread(this).start();
    }
    public void run(){
        try{
            /**
             * 延迟1秒时间
             */
            Thread.sleep(1000);
            SharedPreferences preferences= getSharedPreferences("count", 0); // 存在则打开它，否则创建新的Preferences
            int count = preferences.getInt("count", 0); // 取出数据

            /**
             *如果用户不是第一次使用则直接调转到显示界面,否则调转到引导界面
             */
            if (count == 0) {
                Intent intent1 = new Intent();
                intent1.setClass(activity_Welcome.this, activity_Guide.class);
                startActivity(intent1);
            } else {
                Intent intent2 = new Intent();
                intent2.setClass(activity_Welcome.this, PermissionAcitivity.class);
                startActivity(intent2);
            }
            finish();

            //实例化Editor对象
            SharedPreferences.Editor editor = preferences.edit();
            //存入数据
            editor.putInt("count", 1); // 存入数据
            //提交修改
            editor.commit();
        } catch (InterruptedException e) {

        }

    }
}
