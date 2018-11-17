package com.jinyun.antivirusfour.health;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinyun.antivirusfour.Application;
import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.health.entity.HumanInfo;
import com.keyboard3.BooheeRuler;
import com.keyboard3.RulerNumberLayout;

/**
 * 设置目标体重
 */

public class LoginHealthActivity2 extends Activity implements View.OnClickListener{

    private RulerNumberLayout rnl_target;
    private Button btn_target_next;
    private BooheeRuler br_target;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_target_kg);

        //初始化视图
        initView();
        Application.addActivity(this);
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("目标体重");
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.returns_arrow02);

        btn_target_next=(Button) findViewById(R.id.btn_target_next);
        btn_target_next.setOnClickListener(this);

        rnl_target = (RulerNumberLayout) findViewById(R.id.rnl_target);//目标体重
        br_target = (BooheeRuler) findViewById(R.id.br_target);
        rnl_target.bindRuler(br_target);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;             //回退上一级按钮

            case R.id.btn_target_next:

                //设置目标体重
                String target_weight = rnl_target.getScale();

                // 见LoginHealthActivity
                SharedPreferences.Editor editor = getSharedPreferences("LHA2", Context.MODE_PRIVATE).edit();
                editor.putString("target_weight", target_weight);
                editor.apply();

                Intent intent = new Intent(LoginHealthActivity2.this,LoginHealthActivity3.class);
                startActivityForResult(intent, 2);
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
