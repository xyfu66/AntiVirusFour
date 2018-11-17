package com.jinyun.antivirusfour.health;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinyun.antivirusfour.Application;
import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.health.until.CalculatorPlan;
import com.keyboard3.BooheeRuler;
import com.keyboard3.RulerCallback;
import com.keyboard3.RulerNumberLayout;


/**
 * 设置目标体重
 */

public class LoginHealthActivity3 extends Activity implements View.OnClickListener{

    private TextView tv_plan_time;
    private RulerNumberLayout rnl_plan;
    private Button btn_plan_next;
    private BooheeRuler br_plan;

    //更新完成的周数
    private Handler mTimeHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 2130:
                    String count = (String)msg.obj;
                    tv_plan_time.setText("计划完成需要："+count+"周");
                    sendEmptyMessageDelayed(0, 100);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_plan_kg);


        initView();//初始化视图
        initData();//有关数据的更新
        Application.addActivity(this);


    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("每周减重");
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.returns_arrow02);

        btn_plan_next=(Button) findViewById(R.id.btn_plan_next);
        btn_plan_next.setOnClickListener(this);

        rnl_plan = (RulerNumberLayout) findViewById(R.id.rnl_plan);//目标体重
        br_plan = (BooheeRuler) findViewById(R.id.br_plan);
        rnl_plan.bindRuler(br_plan);

        tv_plan_time = (TextView) findViewById(R.id.tv_plan_time);

    }


    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    Message msg = new Message();
                    msg.what = 2130;
                    SharedPreferences sp = getSharedPreferences("LHA", Context.MODE_PRIVATE);
                    String weight = sp.getString("weight", null);

                    SharedPreferences sp2 = getSharedPreferences("LHA2", Context.MODE_PRIVATE);
                    String target_weight = sp2.getString("target_weight", null);

                    String my_plan_to_loss_weight=rnl_plan.getAfterScale();//

                    msg.obj = CalculatorPlan.runPlan(weight,target_weight,my_plan_to_loss_weight);//得到结果;
                    mTimeHandler.sendMessage(msg);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;             //回退上一级按钮

            case R.id.btn_plan_next:
                //设置计划每周减重
                String plan_to_loss_weight = rnl_plan.getScale();
                // 见LoginHealthActivity
                SharedPreferences.Editor editor = getSharedPreferences("LHA3", Context.MODE_PRIVATE).edit();
                editor.putString("plan_to_loss_weight", plan_to_loss_weight);
                editor.apply();

//                SharedPreferences sp = getSharedPreferences("LHA", Context.MODE_PRIVATE);
//                String weight = sp.getString("weight", null);
//                String height = sp.getString("height", null);

                Intent intent = new Intent(LoginHealthActivity3.this,LoginHealthActivity4.class);
                startActivityForResult(intent, 3);
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
