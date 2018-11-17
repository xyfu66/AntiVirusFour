package com.jinyun.antivirusfour.health.foodsDetailOne.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.health.foodsDetailOne.base.BundleKey;
import com.jinyun.antivirusfour.health.foodsDetailOne.model.entity.HomeBase;
import com.jinyun.antivirusfour.health.foodsDetailOne.model.entity.HomeTop;
import com.jinyun.antivirusfour.health.foodsDetailOne.ui.adapter.GoodsDetailPagerAdapter;
import com.jinyun.antivirusfour.health.foodsDetailOne.ui.adapter.GoodsDetailPagerFourAdapter;
import com.jinyun.antivirusfour.health.foodsDetailOne.ui.adapter.GoodsDetailPagerThreeAdapter;
import com.jinyun.antivirusfour.health.foodsDetailOne.ui.adapter.GoodsDetailPagerTwoAdapter;
import com.jinyun.antivirusfour.health.foodsDetailOne.ui.adapter.ImageHomeAdapter;
import com.jinyun.antivirusfour.health.foodsDetailOne.utils.StatusBarUtil;
import com.jinyun.antivirusfour.health.foodsDetailOne.widget.CirclePageIndicator;
import com.jinyun.antivirusfour.health.foodsDetailOne.widget.GradientScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class DetailActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.status_bar)
    View statusBar;
    @Bind(R.id.scroll_view)
    GradientScrollView scrollView;
    @Bind(R.id.auto_view_pager)
    AutoScrollViewPager autoViewPager;
    @Bind(R.id.indicator)
    CirclePageIndicator indicator;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

    private Activity activity;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_health_foods_one_detail);

        ButterKnife.bind(this);

        activity = this;
        context = getApplicationContext();

        initView();
    }

    private void initView() {
        toolbar.setTitle("商品详情");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        toolbar.setTitleTextColor(Color.argb(0, 255, 255, 255));
        toolbar.getBackground().mutate().setAlpha(0);
        statusBar.getBackground().mutate().setAlpha(0);
        scrollView.setHeader(toolbar, statusBar);
        StatusBarUtil.setTransparentForImageView(this, tvName);

        HomeBase bean = getIntent().getParcelableExtra(BundleKey.PARCELABLE);

        tvName.setText(bean.getName());
        tvPrice.setText("¥" + bean.getPrice());

        List<HomeTop.Carousel> list = new ArrayList<>();
        if (bean.getPrice()==24.00){
            //薏米饼
            list.add(new HomeTop.Carousel(1, "https://g-search1.alicdn.com/img/bao/uploaded/i4/imgextra/i2/140150231730749973/TB2aXEVpVXXXXXuXXXXXXXXXXXX_!!0-saturn_solar.jpg"));
            list.add(new HomeTop.Carousel(2, "https://img.alicdn.com/imgextra/i4/735580970/TB2Vk4GrxGYBuNjy0FnXXX5lpXa_!!735580970-0-item_pic.jpg"));
            list.add(new HomeTop.Carousel(3, "https://img.alicdn.com/imgextra/i1/735580970/TB2aNFBmx9YBuNjy0FfXXXIsVXa_!!735580970.jpg"));
            list.add(new HomeTop.Carousel(4, "https://img.alicdn.com/imgextra/i3/735580970/TB2IdM2q8USMeJjy1zjXXc0dXXa_!!735580970.jpg"));
            list.add(new HomeTop.Carousel(5, "https://img.alicdn.com/imgextra/i1/735580970/TB2_nQan46I8KJjSszfXXaZVXXa_!!735580970.jpg"));
            autoViewPager.setAdapter(new ImageHomeAdapter(context, activity, list));

            GoodsDetailPagerAdapter adapter = new GoodsDetailPagerAdapter(getSupportFragmentManager(),
                    new String[]{"商品描述", "评论(20)"});
            viewPager.setAdapter(adapter);
        }else if (bean.getPrice()==49.90){
            //三只松鼠
            list.add(new HomeTop.Carousel(1, "https://img.alicdn.com/imgextra/i1/880734502/TB1EHXliaAoBKNjSZSyXXaHAVXa_!!0-item_pic.jpg"));
            list.add(new HomeTop.Carousel(2, "https://img.alicdn.com/imgextra/i3/880734502/TB2mDdwik7mBKNjSZFyXXbydFXa_!!880734502.jpg"));
            list.add(new HomeTop.Carousel(3, "https://img.alicdn.com/imgextra/i4/880734502/TB2yC76xGSWBuNjSsrbXXa0mVXa_!!880734502.jpg"));
            list.add(new HomeTop.Carousel(4, "https://img.alicdn.com/imgextra/i2/880734502/TB26dpiseySBuNjy1zdXXXPxFXa_!!880734502.jpg"));
            list.add(new HomeTop.Carousel(5, "https://img.alicdn.com/imgextra/i1/880734502/TB2tMsWj46I8KJjy0FgXXXXzVXa_!!880734502.jpg"));
            autoViewPager.setAdapter(new ImageHomeAdapter(context, activity, list));

            GoodsDetailPagerTwoAdapter adapter = new GoodsDetailPagerTwoAdapter(getSupportFragmentManager(),
                    new String[]{"商品描述", "评论(25)"});
            viewPager.setAdapter(adapter);
        }else if (bean.getPrice()==29.90){
            //螺旋藻全麦饼干
            list.add(new HomeTop.Carousel(1, "https://img.alicdn.com/imgextra/i3/735580970/TB1YpPomlDH8KJjy1zeXXXjepXa_!!0-item_pic.jpg"));
            list.add(new HomeTop.Carousel(2, "https://img.alicdn.com/imgextra/i2/735580970/TB2dTo7meOSBuNjy0FdXXbDnVXa_!!735580970.jpg"));
            list.add(new HomeTop.Carousel(3, "https://img.alicdn.com/imgextra/i4/735580970/TB2SqKbmlfH8KJjy1XbXXbLdXXa_!!735580970.jpg"));
            list.add(new HomeTop.Carousel(4, "https://img.alicdn.com/imgextra/i1/735580970/TB2WOmydDqWBKNjSZFxXXcpLpXa_!!735580970.jpg"));
            list.add(new HomeTop.Carousel(5, "https://img.alicdn.com/imgextra/i3/735580970/TB2A5PTn4TI8KJjSspiXXbM4FXa_!!735580970.jpg"));
            autoViewPager.setAdapter(new ImageHomeAdapter(context, activity, list));

            GoodsDetailPagerThreeAdapter adapter = new GoodsDetailPagerThreeAdapter(getSupportFragmentManager(),
                    new String[]{"商品描述", "评论(28)"});
            viewPager.setAdapter(adapter);
        }else if(bean.getPrice()==51.00){
            //方回春堂 芝麻核桃五仁膏粉黑
            list.add(new HomeTop.Carousel(1, "https://img.alicdn.com/imgextra/i3/399037970/TB1RbYscYZnBKNjSZFhXXc.oXXa_!!0-item_pic.jpg"));
            list.add(new HomeTop.Carousel(2, "https://img.alicdn.com/imgextra/i1/399037970/TB22GEBpDdYBeNkSmLyXXXfnVXa_!!399037970.jpg"));
            list.add(new HomeTop.Carousel(3, "https://img.alicdn.com/imgextra/i2/399037970/TB2Ol9CiBjTBKNjSZFwXXcG4XXa_!!399037970.jpg"));
            list.add(new HomeTop.Carousel(4, "https://img.alicdn.com/imgextra/i4/399037970/TB13WyEfFooBKNjSZFPXXXa2XXa_!!0-item_pic.jpg"));
            list.add(new HomeTop.Carousel(5, "https://img.alicdn.com/imgextra/i4/399037970/TB2ZIMXl3DD8KJjy0FdXXcjvXXa_!!399037970.jpg"));
            autoViewPager.setAdapter(new ImageHomeAdapter(context, activity, list));

            GoodsDetailPagerFourAdapter adapter = new GoodsDetailPagerFourAdapter(getSupportFragmentManager(),
                    new String[]{"商品描述", "评论(18)"});
            viewPager.setAdapter(adapter);
        }

        indicator.setViewPager(autoViewPager);
        autoViewPager.setInterval(4000);
        autoViewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);

        tabLayout.setupWithViewPager(viewPager);
    }
}
