package com.jinyun.antivirusfour.processManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.processManager.service.AutoKillProcessService;
import com.jinyun.antivirusfour.processManager.utils.SystemInfoUtils;


/**
 *设置进程界面的功能是控制系统进程是否显示、锁屏时是否清理进程。
 * 当开启显示系统进程，在进程管理界面中会同时看到系统进程和用户进程
 */

public class ProcessManagerSettingActivity extends Activity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    private ToggleButton mShowSysAppsTgb;
    private ToggleButton mKillProcessTgb;
    private SharedPreferences mSP;
    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_processmanagersetting);
        mSP = getSharedPreferences("config", MODE_PRIVATE);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {

        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.returns_arrow02);
        ((TextView) findViewById(R.id.tv_title)).setText("进程管理设置");

        mShowSysAppsTgb = (ToggleButton) findViewById(R.id.tgb_showsys_process);
        mKillProcessTgb = (ToggleButton) findViewById(R.id.tgb_killprocess_lockscreen);

        //设置是否显示系统进程
        mShowSysAppsTgb.setChecked(mSP.getBoolean("showSystemProcess", true));
        //判断自动杀死进程服务是否运行，并将结果设置给mKillProcessTgb控件(锁屏清理进程)

        running = SystemInfoUtils.isServiceRunning(this, "com.jinyun.antivirusfour.processManager.service.AutoKillProcessService");
        mKillProcessTgb.setChecked(running);
        initListener();

    }

    /**
     * 初始化监听
     */
    private void initListener() {
        mKillProcessTgb.setOnCheckedChangeListener(this);
        mShowSysAppsTgb.setOnCheckedChangeListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }


    /**
     * 用于监听ToggleButton 按钮的状态改变,
     * 当“显示系统进程”按钮状态改变时，调用saveStatus(方法将showSystemProcess 状态保存，
     * 当“锁屏清理进程”按钮状态是选中时时，会启动AutoKillProcessService 服务，否则将服务停止。
     * @param buttonView
     * @param isChecked
     *
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tgb_showsys_process:
                saveStatus("showSystemProcess", isChecked);
                break;
            case R.id.tgb_killprocess_lockscreen:
                Intent service = new Intent(this, AutoKillProcessService.class);
                if (isChecked) {
                    //开启服务
                    startService(service);
                } else {
                    //关闭服务
                    stopService(service);
                }
                break;
        }

    }

    /**
     * 保存“显示进程”的按钮状态。
     * @param string
     * @param isChecked
     */
    private void saveStatus(String string, boolean isChecked) {
        SharedPreferences.Editor edit = mSP.edit();
        edit.putBoolean(string, isChecked);
        edit.commit();
    }
}