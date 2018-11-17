package com.jinyun.antivirusfour.health.smartDevices;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.health.PageFragment;


import java.util.ArrayList;
import java.util.List;

/**
 * 智能设备分页展示页面
 */

public class SmartDevicesActivity extends AppCompatActivity implements View.OnClickListener {

    TabLayout tabLayout;
    ViewPager pager;
    List<PageModel> pageModels = new ArrayList<>();

    {
        pageModels.add(new PageModel(R.layout.devices_one, R.string.devices_one));
        pageModels.add(new PageModel(R.layout.devices_two, R.string.devices_two));
        pageModels.add(new PageModel(R.layout.devices_three, R.string.devices_three));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_devices);

        ((TextView) findViewById(R.id.tv_title)).setText("智能设备");
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.returns_arrow02);


        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                PageModel pageModel = pageModels.get(position);
                return PageFragment.newInstance(pageModel.productLayoutRes);
            }

            @Override
            public int getCount() {
                return pageModels.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getString(pageModels.get(position).titleRes);
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }



    private class PageModel {
        @LayoutRes
        int productLayoutRes;
        @StringRes
        int titleRes;


        PageModel(@LayoutRes int sampleLayoutRes, @StringRes int titleRes) {
            this.productLayoutRes = sampleLayoutRes;
            this.titleRes = titleRes;
        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;             //回退上一级按钮

            default:break;
        }
    }
}
