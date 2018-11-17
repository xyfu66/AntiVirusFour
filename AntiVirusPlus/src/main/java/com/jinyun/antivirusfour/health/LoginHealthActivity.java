package com.jinyun.antivirusfour.health;

/**
 * 第一个界面 获取身高，体重，性别这三个参数
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinyun.antivirusfour.Application;
import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.health.entity.HumanInfo;

import java.util.List;

public class LoginHealthActivity extends Activity implements View.OnClickListener {
    private RulerView ruler_height;   //身高的view
    private RulerView ruler_weight ;  //体重的view
    private TextView tv_register_info_height_value,tv_register_info_weight_value;
    private Button btn_health_login,btn_health_no_login;
    private CheckBox btn_register_info_sex;
    private Context context;
    HumanInfo humanInfo = new HumanInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cm_kg_login);

        //初始化视图
        initView();
        Application.addActivity(this);
        firstIn();//程序第一次进入设置
    }


    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("无笔健康");
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.returns_arrow02);

        ruler_height=(RulerView)findViewById(R.id.ruler_height);
        ruler_weight=(RulerView)findViewById(R.id.ruler_weight);

        tv_register_info_height_value=(TextView) findViewById(R.id.tv_register_info_height_value);
        tv_register_info_weight_value=(TextView) findViewById(R.id.tv_register_info_weight_value);


        ruler_height.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                tv_register_info_height_value.setText(value+"");
//                humanInfo.setHumanHeight(value);
            }
        });

        ruler_weight.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                tv_register_info_weight_value.setText(value+"");
//                humanInfo.setHumanWeight(value);
            }
        });

        ruler_height.setValue(165, 80, 250, 1);
        ruler_weight.setValue(55, 20, 200, 0.1f);

        //下方两个按钮的点击事件
        btn_health_login=(Button) findViewById(R.id.btn_health_login);
        btn_health_login.setOnClickListener(this);
        btn_health_no_login=(Button) findViewById(R.id.btn_health_no_login);
        btn_health_no_login.setOnClickListener(this);

        btn_register_info_sex=(CheckBox)findViewById(R.id.btn_register_info_sex);
        //给CheckBox设置事件监听
        btn_register_info_sex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    humanInfo.setHumanSex(true);//女
                }else{
                    humanInfo.setHumanSex(false);//男 这是默认的
                }
            }
        });
    }

    //传递身高，体重，性别三个参数
    private void passingHWS(){
        //步骤1：获取输入值
        String weight = tv_register_info_weight_value.getText().toString().trim();
        String height = tv_register_info_height_value.getText().toString().trim();
        boolean humanSex = humanInfo.isHumanSex();

        //步骤2-1：创建一个SharedPreferences.Editor接口对象，lock表示要写入的XML文件名，
        // Context.MODE_WORLD_WRITEABLE:  指定该SharedPreferences数据能被其他应用程序读，写
        SharedPreferences.Editor editor = getSharedPreferences("LHA", Context.MODE_PRIVATE).edit();
        //步骤2-2：将获取过来的值放入文件
        editor.putString("weight", weight);
        editor.putString("height", height);
        editor.putBoolean("humanSex",humanSex);
        //步骤3：提交
        editor.apply();
    }


    //标记为第一次启动
    private void firstIn() {
        SharedPreferences.Editor firstInShare = getSharedPreferences("firstInLHA",Context.MODE_PRIVATE).edit();
        firstInShare.putBoolean("isFirstInLHA",false);
        firstInShare.apply();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;             //回退上一级按钮

            case R.id.btn_health_login:
                passingHWS();

                Intent intent = new Intent(LoginHealthActivity.this,LoginHealthActivity2.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_health_no_login:
                passingHWS();

                Intent intentNO = new Intent(LoginHealthActivity.this,LoginHealthActivity4.class);
                startActivityForResult(intentNO, 1);
                break;
            default:break;
        }
    }


    /**
     * 不给按返回键退出程序 ,不然后面有些值没有，程序会出现闪退
     * @param keyCode
     * @param event
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            Toast.makeText(this,"长官，现在不能退出哦", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
