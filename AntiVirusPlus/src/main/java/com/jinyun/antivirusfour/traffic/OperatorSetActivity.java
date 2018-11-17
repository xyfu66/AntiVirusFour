package com.jinyun.antivirusfour.traffic;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jinyun.antivirusfour.R;

/**
 * 选择手机运营商，并将其储存到SharedPreferneces对象中，点击确认按钮后进入到流量监控界面
 */

public class OperatorSetActivity extends Activity implements View.OnClickListener {
    private Spinner mSelectSP;
    private String[] operators={"中国移动","中国联通","中国电信"};
    private ArrayAdapter mSelectadapter;
    private SharedPreferences msp;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_operatorset);
        msp = getSharedPreferences("config",MODE_PRIVATE);
        initView();
    }

    @SuppressWarnings("unchecked")
    private void initView(){

        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.returns_arrow02);
        ((TextView) findViewById(R.id.tv_title)).setText("运营商信息设置");

        mSelectSP=(Spinner) findViewById(R.id.spinner_operator_select);
        mSelectadapter=new ArrayAdapter(this,R.layout.item_spinner_operatorset,R.id.tv_provice,operators);
        mSelectSP.setAdapter(mSelectadapter);
        findViewById(R.id.btn_operator_finish).setOnClickListener(this);
    }

    /**
     * 设置isset_operator是否为true 然后跳转到主界面中
     * @param v
     */
    @Override
    public void onClick(View v){
        SharedPreferences.Editor edit=msp.edit();
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                edit.putBoolean("isset_operator",false);
                finish();
                break;
            case R.id.btn_operator_finish:
                edit.putInt("operator",mSelectSP.getSelectedItemPosition()+1);
                edit.putBoolean("isset_operator",true);
                edit.commit();
                startActivity(new Intent(this,TrafficMonitoringActivity.class));
                finish();
                break;
        }
    }
}
