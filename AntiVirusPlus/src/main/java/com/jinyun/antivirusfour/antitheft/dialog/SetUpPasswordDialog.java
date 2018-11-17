package com.jinyun.antivirusfour.antitheft.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jinyun.antivirusfour.R;


public class SetUpPasswordDialog extends Dialog implements View.OnClickListener{

    private TextView mTitleTV;//标题栏
    public EditText mFirstPWDET;//首次输入密码文本框
    public EditText mAffirmET;//确认密码文本框

    private MyCallBack myCallBack;//回调接口

    //引入自定义对话框样式
    public SetUpPasswordDialog(Context context) {
        super(context, R.style.dialog_custom);
    }

    //传递一个MyCallBack接口
    public void setCallBack(MyCallBack myCallBack){
        this.myCallBack = myCallBack;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.setup_password_dialog);
        super.onCreate(savedInstanceState);
        initView();
    }

    /**
     * 老规矩，初始化控件
     */
    private void initView() {
        mTitleTV = (TextView) findViewById(R.id.tv_setuppwd_title);
        mFirstPWDET  = (EditText) findViewById(R.id.et_firstpwd);
        mAffirmET = (EditText) findViewById(R.id.et_affirm_password);
        findViewById(R.id.btn_ok).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    /**
     * 设置对话框标题栏
     * @param title
     */
    public void setTitle(String title){
        if(!TextUtils.isEmpty(title)){
            mTitleTV.setText(title);
        }
    }

    /**
     * Called when a view has been clicked.
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
                myCallBack.ok();//点击时执行OK方法
                break;
            case R.id.btn_cancel:
                myCallBack.cancel();//取消执行cancle
                break;
        }

    }

    public interface MyCallBack{
        void ok();
        void cancel();
    }
}
