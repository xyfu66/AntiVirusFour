package com.jinyun.antivirusfour.advanced;

/**
 * Created by ENG on 2018/3/18.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.advanced.utils.SmsReducitionUtils;
import com.jinyun.antivirusfour.advanced.utils.UIUtils;
import com.jinyun.antivirusfour.advanced.widget.MyCircleProgress;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/** 短信还原 */
public class SMSReductionActivity extends Activity implements View.OnClickListener {
    private MyCircleProgress mProgressButton;
    private boolean flag=false;
    private SmsReducitionUtils smsReducitionUtils;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reducition);
        initView();
        smsReducitionUtils=new SmsReducitionUtils();
    }
    private void initView(){
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.returns_arrow02);
        ((TextView) findViewById(R.id.tv_title)).setText("短信还原");

        mProgressButton=(MyCircleProgress) findViewById(R.id.mcp_reducition);
        mProgressButton.setOnClickListener(this);
    }
    protected void onDestroy(){
        flag=false;
        smsReducitionUtils.setFlag(flag);
        super.onDestroy();
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.mcp_reducition:
                if(flag){
                    flag=false;
                    mProgressButton.setText("一键还原");
                }else{
                    flag=true;
                    mProgressButton.setText("取消还原");
                }
                smsReducitionUtils.setFlag(flag);
                new Thread(){
                    public void run(){
                        try{
                            smsReducitionUtils.reducitionSms(SMSReductionActivity.this,new SmsReducitionUtils.SmsReducitionCallBack(){
                                @Override
                                public void onSmsReducition(int process){
                                    mProgressButton.setProcess(process);
                                }
                                @Override
                                public void beforeSmsReducition(int size){
                                    mProgressButton.setMax(size);
                                }
                            });
                        }catch (XmlPullParserException e){
                            e.printStackTrace();
                            UIUtils.showToast(SMSReductionActivity.this,"文件格式错误");
                        }catch (IOException e){
                            e.printStackTrace();
                            UIUtils.showToast(SMSReductionActivity.this,"读写错误");
                        }
                    }
                }.start();
                break;
        }
    }
}
