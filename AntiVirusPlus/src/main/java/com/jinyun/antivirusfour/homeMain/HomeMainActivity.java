package com.jinyun.antivirusfour.homeMain;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.jinyun.antivirusfour.advanced.AdvancedToolsActivity;
import com.jinyun.antivirusfour.antitheft.LostFindActivity;
import com.jinyun.antivirusfour.antitheft.dialog.InterPasswordDialog;
import com.jinyun.antivirusfour.antitheft.dialog.SetUpPasswordDialog;
import com.jinyun.antivirusfour.antitheft.service.MyDeviceAdminReciever;
import com.jinyun.antivirusfour.antitheft.utils.MD5Utils;
import com.jinyun.antivirusfour.cacheClean.CacheClearListActivity;
import com.jinyun.antivirusfour.emptyFolderCleanup.EmptyMainActivity;
import com.jinyun.antivirusfour.homeMain.view.LauncherViewPagerAdapter;
import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.homeMain.view.RoundProgressBar;
import com.jinyun.antivirusfour.processManager.ProcessManagerActivity;
import com.jinyun.antivirusfour.setting.SettingsActivity;
import com.jinyun.antivirusfour.software.AppManagerActivity;
import com.jinyun.antivirusfour.telephoneList.SecurityPhoneActivity;
import com.jinyun.antivirusfour.traffic.TrafficMonitoringActivity;
import com.jinyun.antivirusfour.virusScan.VirusScanActivity;
import com.jinyun.antivirusfour.welcomeView.adapter.HomeAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeMainActivity extends AppCompatActivity implements View.OnClickListener {

    private GridView gv_home;
    private long mExitTime;

    private SharedPreferences msharedPreferences;//存储设置文件
    private DevicePolicyManager policyManager;//设备管理员
    private ComponentName componentName;//申请权限

    private RoundProgressBar mRoundProgressBar5;
    private int progress = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //初始化布局,全屏
        setContentView(R.layout.activity_home_main);

        msharedPreferences = getSharedPreferences("config", MODE_PRIVATE);

        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.homeGreen));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.returns_arrow02);
        ImageView mRightImgv = (ImageView) findViewById(R.id.imgv_rightbtn);
        mRightImgv.setOnClickListener(this);
        mRightImgv.setImageResource(R.drawable.settings03_min);
        ((TextView) findViewById(R.id.tv_title)).setText("安全卫士 ");

        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new HomeAdapter(HomeMainActivity.this));//填充GridView

        //单击事件
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //parent 代表gridView， view代表每个条目view对象，position代表每个条目的位置
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://手机防盗
                        if (isSetUpPassword()) {
                            //弹出输入密码的对话框
                            showInterPasswordDialog();
                        } else {
                            //弹出设置密码的对话框
                            showSetUpPasswordDialog();
                        }
                        break;
                    case 1://通讯卫士
                        startActivity(SecurityPhoneActivity.class);
                        break;
                    case 2://软件管家
                        startActivity(AppManagerActivity.class);
                        break;
                    case 3://病毒查杀
                        startActivity(VirusScanActivity.class);
                        break;
                    case 4://缓存清理
                        startActivity(CacheClearListActivity.class);
                        break;
                    case 5://进程管理
                        startActivity(ProcessManagerActivity.class);
                        break;
                    case 6://流量统计
                        startActivity(TrafficMonitoringActivity.class);
                        break;
                    case 7://空文件夹整理
                        startActivity(EmptyMainActivity.class);
                        break;
                    case 8://高级工具
                        startActivity(AdvancedToolsActivity.class);
                        break;
                }
            }
        });
        init();

    }

    //初始化
    private void init() {
        //获取设备管理员
        policyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        //申请权限
        componentName = new ComponentName(this, MyDeviceAdminReciever.class);
        //如果没有权限则申请权限
        boolean active = policyManager.isAdminActive(componentName);
        if (!active) {
            //没有管理员权限，则申请
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "获取超级管理员权限，用于远程锁屏和清除数据");
            startActivity(intent);
        }

        mRoundProgressBar5 = (RoundProgressBar) findViewById(R.id.roundProgressBar2);
        mRoundProgressBar5.setDefaultStr("一键体检");
        mRoundProgressBar5.setLogoStr("");
        mRoundProgressBar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startScanningListFragment();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (progress >= 78) {
                            progress -= 3;
                            System.out.println(progress);
                            mRoundProgressBar5.setProgress(progress);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                Date date = new Date();
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String da = s.format(date);
                ((TextView) findViewById(R.id.textView1)).setText(da);
            }
        });
        ((Button) findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button) findViewById(R.id.button1)).setText("正在体检");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (progress >= 78) {
                            progress -= 3;
                            System.out.println(progress);
                            mRoundProgressBar5.setProgress(progress);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        ((Button) findViewById(R.id.button1)).setText("体检完成");

                    }
                }).start();
            }
        });

//        HomeFragment homeFragment=new HomeFragment();
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.activity_test_frag, homeFragment, "").commit();
//

    }





    /**
     * 开启新的Activity不关闭自己,cls是行Activity字节码
     */
    public void startActivity(Class<?> cls){
        Intent intent = new Intent(HomeMainActivity.this,cls);
        startActivity(intent);
    }


    /**
     * 按两次返回键退出程序 ,真有必要，你懂得
     * @param keyCode
     * @param event
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            if((System.currentTimeMillis() - mExitTime)<2000){
                System.exit(0);
            }else{
                Toast.makeText(this,"再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     *
     * 设置密码对话框
     */
    public void showSetUpPasswordDialog(){
        final SetUpPasswordDialog msetPwdDialog = new SetUpPasswordDialog(HomeMainActivity.this);
        msetPwdDialog.setCallBack(new SetUpPasswordDialog.MyCallBack(){
            @Override
            public void ok() {
                String firstPwd = msetPwdDialog.mFirstPWDET.getText().toString().trim();
                String affirmPwd = msetPwdDialog.mAffirmET.getText().toString().trim();

                if (!TextUtils.isEmpty(firstPwd) && !TextUtils.isEmpty(affirmPwd)) {
                    //两次密码一致，则保存
                    if (firstPwd.equals(affirmPwd)) {
                        savePwd(affirmPwd);
                        //显示输入密码对话框
                        showInterPasswordDialog();
                    } else {
                        Toast.makeText(HomeMainActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeMainActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void cancel() {
                msetPwdDialog.dismiss();
            }
        });
        msetPwdDialog.setCancelable(true);
        msetPwdDialog.show();
    }


    /**
     * 输入密码对话框
     * @return
     */
    public void showInterPasswordDialog(){
        final String password = getPassword();
        final InterPasswordDialog mInterPasswordDialog =  new InterPasswordDialog(HomeMainActivity.this);

        mInterPasswordDialog.setCallBack(new InterPasswordDialog.MyCallBack() {
            @Override
            public void confirm() {
                if(TextUtils.isEmpty(mInterPasswordDialog.getPassword())){
                    Toast.makeText(HomeMainActivity.this,"密码不能为空", Toast.LENGTH_SHORT).show();
                }else if(password.equals(MD5Utils.encode(mInterPasswordDialog.getPassword()))){
                    //进入防盗界面
                    mInterPasswordDialog.dismiss();
                    startActivity(LostFindActivity.class);
                    Toast.makeText(HomeMainActivity.this,"成功进入防盗界面", Toast.LENGTH_SHORT).show();
                }else{
                    mInterPasswordDialog.dismiss();
                    Toast.makeText(HomeMainActivity.this,"密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void dismiss() {
                mInterPasswordDialog.dismiss();
            }
        });

        mInterPasswordDialog.setCancelable(true);
        mInterPasswordDialog.show();
    }

    /**
     * 保存密码
     * @return
     */
    public void savePwd(String affirmPwd){
        SharedPreferences.Editor editor = msharedPreferences.edit();
        editor.putString("PhoneAntiTheftPWD", MD5Utils.encode(affirmPwd));
        editor.commit();
    }

    /**
     * 获取密码
     * @return
     */
    public String getPassword(){
        String password  = msharedPreferences.getString("PhoneAntiTheftPWD",null);
        if(TextUtils.isEmpty(password))
            return "";
        return password;
    }
    //判断是否设置过密码
    public boolean isSetUpPassword(){
        String password = msharedPreferences.getString("PhoneAntiTheftPWD", null);
        if(TextUtils.isEmpty(password)){
            return false;
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.imgv_rightbtn:
                startActivity(SettingsActivity.class);
                break;
            default:
                break;
        }
    }
}

