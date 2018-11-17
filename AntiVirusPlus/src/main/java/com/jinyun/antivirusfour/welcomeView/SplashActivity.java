package com.jinyun.antivirusfour.welcomeView;


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.welcomeView.utils.MyUtils;


public class SplashActivity extends Activity {
    //应用程序版本号
    private TextView mVersionTV;
    //本地版本号
    private String mVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);



        mVersion = MyUtils.getVersion(getApplicationContext());//获取对应的本地版本号

        initView();


    }

        /**
         * 初始化控件
         * 将获取到的本地版本号显示在界面上
         * 权限已经配置：<uses-permission android:name="android.permission.INTERNET" />
                         <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
         */

    private void initView() {
        mVersionTV = (TextView) findViewById(R.id.tv_splash_version);
        mVersionTV.setText("我不会告诉你我的版本号是：" + mVersion +"的！");

    }
}


