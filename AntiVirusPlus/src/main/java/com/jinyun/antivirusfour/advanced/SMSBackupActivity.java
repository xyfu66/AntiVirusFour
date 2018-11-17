package com.jinyun.antivirusfour.advanced;

/**
 * Created by ENG on 2018/3/18.
 */

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.advanced.utils.SmsBackUpUtils;
import com.jinyun.antivirusfour.advanced.utils.UIUtils;
import com.jinyun.antivirusfour.advanced.widget.MyCircleProgress;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


/** 短信备份 */
public class SMSBackupActivity extends Activity implements View.OnClickListener {
    private MyCircleProgress mProgressButton;
    /** 标识符，用来标识备份状态的 */

    private boolean flag=false;
    private SmsBackUpUtils smsBackUpUtils;
    private static final int CHANGE_BUTTON_TEXT=100;
    private Handler handler=new Handler(){
        public void handlerMessage(android.os.Message msg){
            switch(msg.what){
                case CHANGE_BUTTON_TEXT:
                    mProgressButton.setText("一键备份");
                    break;
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_smsbackup);
        smsBackUpUtils=new SmsBackUpUtils();
        initView();
        initPermission();//获取权限
    }


    private void initView(){
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.returns_arrow02);
        ((TextView) findViewById(R.id.tv_title)).setText("短信备份");

        mProgressButton=(MyCircleProgress) findViewById(R.id.mcp_smsbackup);
        mProgressButton.setOnClickListener(this);
    }

    private void initPermission() {
        ArrayList<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_SMS);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        String[] permissions = new String[permissionList.size()];
        permissions = permissionList.toArray(permissions);
        if (permissions.length > 0) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (permissions.length == 2) {
                    if (grantResults.length > 0 && grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED && grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "you allow 2 permission", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "you denied the permission", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else if (permissions.length == 1) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "you allow 1 permission", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "you denied the permission", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy(){
        flag=false;
        smsBackUpUtils.setFlag(flag);
        super.onDestroy();
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.mcp_smsbackup:
                if(flag){
                    flag=false;
                    mProgressButton.setText("一键备份");
                }else{
                    flag=true;
                    mProgressButton.setText("取消备份");
                }
                smsBackUpUtils.setFlag(flag);
                new Thread(){
                    public void run(){
                        try{
                            boolean backUpSms=smsBackUpUtils.backUpSms(SMSBackupActivity.this,new SmsBackUpUtils.BackupStatusCallback(){
                                @Override
                                public void onSmsBackup(int process){
                                    mProgressButton.setProcess(process);
                                }
                                @Override
                                public void beforeSmsBackup(int size) {
                                    if(size<=0){
                                        flag=false;
                                        smsBackUpUtils.setFlag(flag);
                                        UIUtils.showToast(SMSBackupActivity.this,"您还没有短信");
                                        handler.sendEmptyMessage(CHANGE_BUTTON_TEXT);
                                }else{
                                        mProgressButton.setMax(size);
                                    }
                                }
                            });
                            if(backUpSms){
                                UIUtils.showToast(SMSBackupActivity.this,"备份成功");
                            }else{
                                UIUtils.showToast(SMSBackupActivity.this,"备份失败");
                            }
                        }catch(FileNotFoundException e){
                            e.printStackTrace();
                            UIUtils.showToast(SMSBackupActivity.this,"文件生成失败");
                        }catch(IllegalStateException e){
                            e.printStackTrace();
                            UIUtils.showToast(SMSBackupActivity.this,"SD卡不可用或SD卡内存不足");
                        }catch(IOException e){
                            e.printStackTrace();
                            UIUtils.showToast(SMSBackupActivity.this,"读写错误");
                        }
                    };
                }.start();
                break;
        }
    }
}
