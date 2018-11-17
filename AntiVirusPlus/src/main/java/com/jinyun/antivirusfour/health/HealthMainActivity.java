package com.jinyun.antivirusfour.health;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.health.adapter.HealthAdapter;
import com.jinyun.antivirusfour.health.adapter.HealthGoodsOneAdapter;
import com.jinyun.antivirusfour.health.cook.activity.GuideActivity;
import com.jinyun.antivirusfour.health.foodsDetailOne.base.BundleKey;
import com.jinyun.antivirusfour.health.foodsDetailOne.model.entity.HomeBase;
import com.jinyun.antivirusfour.health.foodsDetailOne.ui.activity.DetailActivity;
import com.jinyun.antivirusfour.health.lesson.view.MainActivity;
import com.jinyun.antivirusfour.health.smartDevices.SmartDevicesActivity;

import butterknife.Bind;


/**
 * 健康模块 主界面
 */

public class HealthMainActivity extends AppCompatActivity implements View.OnClickListener {

    private GridView gv_health;
    private GridView gv_health_main_goods1;//为您甄选
    private ImageView iv_health_goods_two;//精品推荐

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_main);

        ((TextView) findViewById(R.id.tv_title)).setText("无笔健康");
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.returns_arrow02);

        init();
        initData();
        initImageView();
    }

    private void init() {
        gv_health = (GridView) findViewById(R.id.gv_health);
        gv_health.setAdapter(new HealthAdapter(HealthMainActivity.this));//填充GridView

        //单击事件
        gv_health.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //parent 代表gridView， view代表每个条目view对象，position代表每个条目的位置
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://健康报告
                        startActivity(PersonalHealthActivity.class);
                        break;
                    case 1://运动健康
                        startActivity(com.jinyun.antivirusfour.health.running.activity.MainActivity.class);
                        break;
                    case 2://健康新闻
                        Toast.makeText(HealthMainActivity.this, "别着急，正在开发中", Toast.LENGTH_SHORT).show();
                        break;
                    case 3://智能设备
                        startActivity(SmartDevicesActivity.class);
                        break;
                    case 4://健康菜谱
                        startActivity(GuideActivity.class);
                        break;
                    case 5://课程
                        startActivity(MainActivity.class);
                        break;


                }
            }
        });
    }

    //商品详细信息(为您甄选)
    private void initData()
    {
        gv_health_main_goods1 = (GridView) findViewById(R.id.gv_health_main_goods1);
        gv_health_main_goods1.setAdapter(new HealthGoodsOneAdapter(HealthMainActivity.this));//填充GridView

        //单击事件
        gv_health_main_goods1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //parent 代表gridView， view代表每个条目view对象，position代表每个条目的位置
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://food1
                        startDetailActivity("【薏米红豆燕麦全麦饼干】",24.00,BundleKey.PARCELABLE);
                        break;
                    case 1://food2
                        startDetailActivity("【三只松鼠_零食大礼包】休闲网红小吃货一箱整箱批发端午节礼包",49.90,BundleKey.PARCELABLE);
                        break;
                    case 2://food3
                        startDetailActivity("【螺旋藻全麦饼干】",29.90,BundleKey.PARCELABLE);
                        break;
                }
            }
        });
    }

    //为您推荐
    private void initImageView(){
        ImageView iv_health_goods_two = (ImageView) findViewById(R.id.iv_health_goods_two);
        iv_health_goods_two.setOnClickListener(this);
    }
    /**
     * 开启商品详情页
     */
    private void startDetailActivity(String name,double price,String extra){
        HomeBase homeBase_food_one = new HomeBase();
        homeBase_food_one.setName(name) ;
        homeBase_food_one.setPrice(price);

        Intent intent = new Intent();
        intent.setClass(HealthMainActivity.this,DetailActivity.class);
        intent.putExtra(extra,homeBase_food_one);

        startActivity(intent);
    }


    /**
     * 开启新的Activity不关闭自己,cls是行Activity字节码
     */
    public void startActivity(Class<?> cls){
        Intent intent = new Intent(HealthMainActivity.this,cls);
        startActivity(intent);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;             //回退上一级按钮
            case R.id.iv_health_goods_two:
                startDetailActivity("【七折上新价】方回春堂 芝麻核桃五仁膏粉黑",51.00,BundleKey.PARCELABLE);
                break;
            default:break;
        }
    }
}
