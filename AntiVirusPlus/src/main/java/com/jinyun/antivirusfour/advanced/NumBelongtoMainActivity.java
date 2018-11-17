package com.jinyun.antivirusfour.advanced;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.advanced.model.Phone;
import com.jinyun.antivirusfour.advanced.mvp.MvpMainView;
import com.jinyun.antivirusfour.advanced.mvp.impl.MainPresenter;


public class NumBelongtoMainActivity extends FragmentActivity implements View.OnClickListener, MvpMainView {

    EditText input_phone;
    Button btn_search;
    TextView result_phone,result_province,result_type,result_carrier;
    MainPresenter mainPresenter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbelongto_main);

        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn_numbelongto);
        mLeftImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLeftImgv.setImageResource(R.drawable.returns_arrow02);

        initUI();
    }

    private void initUI() {
        input_phone = (EditText) findViewById(R.id.input_phone);
        btn_search = (Button) findViewById(R.id.btn_search);
        result_phone = (TextView) findViewById(R.id.result_phone);
        result_province = (TextView) findViewById(R.id.result_province);
        result_type = (TextView) findViewById(R.id.result_type);
        result_carrier = (TextView) findViewById(R.id.result_carrier);

        btn_search.setOnClickListener(this);

        mainPresenter = new MainPresenter(this);
        mainPresenter.attach(this);
    }

    @Override
    public void onClick(View view) {
        mainPresenter.sarchPhoneInfo(input_phone.getText().toString());
    }

    @Override
    public void showLoading() {
        if(progressDialog == null){
            progressDialog = ProgressDialog.show (this,"","正在加载...",true, false);
        }else if(progressDialog.isShowing()){
            progressDialog.setTitle("");
            progressDialog.setMessage("正在加载...");
        }
        progressDialog.show();
    }

    @Override
    public void hidenLoading() {
        if (progressDialog!=null&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateView() {
        Phone phone = mainPresenter.getPhoneInfo();
        result_phone.setText("手机号码:"+phone.getTelString());
        result_province.setText("省份:"+phone.getProvince());
        result_type.setText("运营商:"+phone.getCatName());
        result_carrier.setText("归属运营商:"+phone.getCarrier());
    }
}
