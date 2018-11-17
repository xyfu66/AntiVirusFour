package com.jinyun.antivirusfour.health;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinyun.antivirusfour.Application;
import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.health.bmiView.BMIView;
import com.jinyun.antivirusfour.health.bmiView.FatView;
import com.jinyun.antivirusfour.health.until.CalculatotCalories;
import com.jinyun.antivirusfour.health.view.DialogAge;
import com.jinyun.antivirusfour.health.view.DialogBMI;
import com.jinyun.antivirusfour.health.view.DialogFat;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PersonalHealthActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.bmiView)
    BMIView bmiView;
    @Bind(R.id.fatView)
    FatView fatView;
    @Bind(R.id.btn_personal_retry)
    Button btn_personal_retry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_personal);

        ButterKnife.bind(this);//初始化

        invalide();
        init();
    }


    public void invalide() {
        //取值
        SharedPreferences sp = getSharedPreferences("LHA", Context.MODE_PRIVATE);
        float weight = Float.parseFloat(sp.getString("weight", null));
        float height = Float.parseFloat(sp.getString("height",null));
        boolean humanSex = sp.getBoolean("humanSex",false);//男false 女true

        SharedPreferences sp4 = getSharedPreferences("LHA4", Context.MODE_PRIVATE);
        String birth_Year= String.valueOf(sp4.getInt("birthdayY",0));
        String birth_Mouth = String.valueOf(sp4.getInt("birthdayM",0));
        String birth_Day = String.valueOf(sp4.getInt("birthdayD",0));



        //先设置男女 再身高体重
        if (!humanSex) {
            bmiView.setGender(0);
            fatView.setGender(0);
        } else{
            bmiView.setGender(1);
            fatView.setGender(1);
        }

        //BMI值
        bmiView.setHeight(height/100f);
        bmiView.setWeight(weight);
        String BMITrueValue =String.valueOf(bmiView.getBmiValue()) + " " + bmiView.getBodyDescription();//中间的具体数值

        //体脂率值
        fatView.setHeight(height/100f);
        fatView.setWeight(weight);
        float userAge=getAgeByBirthDay(birth_Year,birth_Mouth,birth_Day);
        fatView.setAge(userAge);
        String FatTrueValue =String.valueOf(fatView.getBmiValue()) + " " + fatView.getBodyDescription();//中间的具体数值

        //BMI
        findViewById(R.id.rl_titlebar_health_number).setBackgroundColor(getResources().getColor(R.color.white));
        ((TextView) findViewById(R.id.tv_title_ealth_number)).setText(BMITrueValue);//设置中间数值
        ImageView mRightImgv = (ImageView) findViewById(R.id.imgv_rightbtn_ealth_number);
        mRightImgv.setOnClickListener(this);


        //Body fat rate
        ((TextView) findViewById(R.id.tv_title_health_fat)).setText(FatTrueValue);//设置中间数值
        ImageView mRightImgv2 = (ImageView) findViewById(R.id.imgv_rightbtn_health_fat);
        mRightImgv2.setOnClickListener(this);


        //age
        int myUserAge = (int) userAge;
        ((TextView) findViewById(R.id.tv_title_health_age)).setText(myUserAge+"岁");//设置中间数值
        ImageView mRightImgv3 = (ImageView) findViewById(R.id.imgv_rightbtn_health_age);
        mRightImgv3.setOnClickListener(this);

        //eat
        String calories =CalculatotCalories.CaloriesRun(weight,height,userAge,humanSex);//一天要消耗多少卡路里
        ((TextView) findViewById(R.id.tv_title_health_eat)).setText(calories+"千卡");//设置中间数值

        //running
        float userHeartMax = (220f-userAge)*0.8f;
        float userHeartMin = (220f-userAge)*0.6f;
        ((TextView) findViewById(R.id.tv_title_health_running)).setText(userHeartMin+"~"+userHeartMax+"次/分钟");//设置中间数值

        Application.clearActivity();//释放系统资源

    }

    private void init() {
        //最上面的标题栏
        ((TextView) findViewById(R.id.tv_title)).setText("健康报告");
        ImageView mLeftImgv0 = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv0.setOnClickListener(this);
        mLeftImgv0.setImageResource(R.drawable.returns_arrow02);

        btn_personal_retry.setOnClickListener(this);//重新测评的监听
    }

    public static int getAgeByBirthDay(String birth_Year,String birth_Mouth,String birth_Day){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //得到当前的年份
        String cYear = sdf.format(new Date()).substring(0,4);
        String cMouth = sdf.format(new Date()).substring(5,7);
        String cDay = sdf.format(new Date()).substring(8,10);


        int age = Integer.parseInt(cYear) - Integer.parseInt(birth_Year);
        if ((Integer.parseInt(cMouth) - Integer.parseInt(birth_Mouth))<0) {
            age=age-1;
        }else if ((Integer.parseInt(cMouth) - Integer.parseInt(birth_Mouth))==0) {
            if ( (Integer.parseInt(cDay) - Integer.parseInt(birth_Day))>0) {
                age=age-1;
            }else {
                age = Integer.parseInt(cYear) - Integer.parseInt(birth_Year);
            }
        }else if ((Integer.parseInt(cMouth) - Integer.parseInt(birth_Mouth))>0) {
            age = Integer.parseInt(cYear) - Integer.parseInt(birth_Year);
        }
        return age;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                Intent intent = new Intent(PersonalHealthActivity.this,HealthMainActivity.class);
                startActivityForResult(intent, 1);
                finish();
                break;
            case R.id.imgv_rightbtn_ealth_number:
                //关键步骤，样式
                DialogBMI dialogBMI = new DialogBMI(this,R.style.dialogBMI);
                dialogBMI.show();
                break;
            case R.id.btn_personal_retry:
                Intent intent_retry = new Intent(PersonalHealthActivity.this,LoginHealthActivity.class);
                startActivityForResult(intent_retry, 1);
                finish();
                break;
            case R.id.imgv_rightbtn_health_fat:
                //关键步骤，样式
                DialogFat dialogFat = new DialogFat(this,R.style.dialogBMI);
                dialogFat.show();
                break;
            case R.id.imgv_rightbtn_health_age:
                DialogAge dialogAge = new DialogAge(this,R.style.dialogBMI);
                dialogAge.show();
                break;
            default:break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //按返回键返回无笔健康主界面
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent healthMain = new Intent(PersonalHealthActivity.this,LoginHealthActivity.class);
            startActivityForResult(healthMain, 1);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
