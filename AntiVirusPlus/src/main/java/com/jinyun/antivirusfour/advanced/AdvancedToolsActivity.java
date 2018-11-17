package com.jinyun.antivirusfour.advanced;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinyun.antivirusfour.R;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;


/**
 * Created by ENG on 2018/3/18.
 */

public class AdvancedToolsActivity extends Activity implements View.OnClickListener {
    private static final int PHOTO_PERMISS= 100045;//权限申请时的常量

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_advancetools);
        initView();
        requestPhotoPermiss();
    }

    private void requestPhotoPermiss(){
        PermissionGen.with(AdvancedToolsActivity.this)
                .addRequestCode(PHOTO_PERMISS)
                .permissions(
                        Manifest.permission.READ_SMS)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
    @PermissionSuccess(requestCode = PHOTO_PERMISS)
    public void requestPhotoSuccess(){
        //成功之后的处理
        Toast.makeText(AdvancedToolsActivity.this, "获取权限成功！", Toast.LENGTH_SHORT).show();
    }

    @PermissionFail(requestCode = PHOTO_PERMISS)
    public void requestPhotoFail(){
        //失败之后的处理，
        Toast.makeText(AdvancedToolsActivity.this, "获取权限失败！", Toast.LENGTH_SHORT).show();
    }

    /** 初始化控件 */
    private void initView(){

        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.returns_arrow02);
        ((TextView) findViewById(R.id.tv_title)).setText("高级工具");

        findViewById(R.id.advanceview_applock).setOnClickListener(this);
        findViewById(R.id.advanceview_numbelongs).setOnClickListener(this);
        findViewById(R.id.advanceview_smsbackup).setOnClickListener(this);
        findViewById(R.id.advanceview_smsreducition).setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.advanceview_applock:
                //进入程序锁页面
                startActivity(AppLockActivity.class);
                break;
            case R.id.advanceview_numbelongs:
                //进入归属地查询页面
                startActivity(NumBelongtoMainActivity.class);
                break;
            case R.id.advanceview_smsbackup:
                //进入短信备份页面
                startActivity(SMSBackupActivity.class);
                break;
            case R.id.advanceview_smsreducition:
                //进入短信还原页面
                startActivity(SMSReductionActivity.class);
                break;
        }
    }
    /**
     *  开启新的activity不关闭自己
     *  @param cls 新的activity的字节码
     */
    public void startActivity(Class<?> cls){
        Intent intent=new Intent(this,cls);
        startActivity(intent);
    }
}
