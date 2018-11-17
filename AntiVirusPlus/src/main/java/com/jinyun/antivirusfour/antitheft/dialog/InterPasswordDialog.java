package com.jinyun.antivirusfour.antitheft.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jinyun.antivirusfour.R;


public class InterPasswordDialog extends Dialog implements View.OnClickListener{

    private Context context;
    private EditText mInterPWDET;
    private TextView mTitleTV;
    private Button mOkButton;
    private Button mCancelButton;
    private MyCallBack myCallBack;



    public InterPasswordDialog(Context context) {
        super(context, R.style.dialog_custom);
        this.context=context;
    }

    public void setCallBack(MyCallBack mycallBack){
        this.myCallBack = mycallBack;
    }//回调

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.inter_password_dialog);
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mTitleTV = (TextView) findViewById(R.id.tv_interpwd_title);
        mInterPWDET  = (EditText) findViewById(R.id.et_inter_password);
        mOkButton = (Button) findViewById(R.id.btn_confirm);
        mOkButton.setOnClickListener(this);
        mCancelButton = (Button) findViewById(R.id.btn_dismiss);
        mCancelButton.setOnClickListener(this);
    }

    public void setTitle(String title){
        if(!TextUtils.isEmpty(title)){
            mTitleTV.setText(title);
        }
    }

    public String getPassword(){
        return mInterPWDET.getText().toString();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
                myCallBack.confirm();
                break;
            case R.id.btn_dismiss:
                myCallBack.dismiss();
                break;
        }

    }

    public interface MyCallBack{
        void confirm();
        void dismiss();
    }
}
