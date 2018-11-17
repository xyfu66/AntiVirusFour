package com.jinyun.antivirusfour.advanced;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.advanced.Fragment.AppLockFragment;
import com.jinyun.antivirusfour.advanced.Fragment.AppUnLockFragment;

import java.util.ArrayList;
import java.util.List;

/** 程序锁 */
public class AppLockActivity extends FragmentActivity implements View.OnClickListener {
    private ViewPager mAppViewPager;
    List<Fragment> mFragments=new ArrayList<Fragment>();
    private TextView mLockTV;
    private TextView mUnLockTV;
    private View slideLockView;
    private View slideUnLockView;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_applock);
        initView();
        initListener();
    }
    private void initListener(){
        mAppViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
           @Override
            public void onPageSelected(int arg0){
               if(arg0==0){
                   slideUnLockView.setBackgroundResource(R.drawable.transparent_up);
                   slideLockView.setBackgroundColor(getResources().getColor(R.color.green));
                   //未加锁
                   mLockTV.setTextColor(getResources().getColor(R.color.blace30));
                   mUnLockTV.setTextColor(getResources().getColor(R.color.purple));
               }else{
                   slideLockView.setBackgroundResource(R.drawable.transparent_up);
                   slideUnLockView.setBackgroundColor(getResources().getColor(R.color.green));
                   //已加锁
                   mLockTV.setTextColor(getResources().getColor(R.color.purple));
                   mUnLockTV.setTextColor(getResources().getColor(R.color.blace30));
               }
           }
           @Override
            public void onPageScrolled(int arg0,float argl,int arg2){
           }
           @Override
            public void onPageScrollStateChanged(int arg0){
           }
        });
    }
    private void initView(){
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.returns_arrow02);
        ((TextView) findViewById(R.id.tv_title)).setText("程序锁");

        mAppViewPager=(ViewPager) findViewById(R.id.vp_applock);
        mLockTV=(TextView) findViewById(R.id.tv_lock);
        mUnLockTV=(TextView) findViewById(R.id.tv_unlock);
        mLockTV.setOnClickListener(this);
        mUnLockTV.setOnClickListener(this);
        slideLockView=findViewById(R.id.view_slide_lock);
        slideUnLockView=findViewById(R.id.view_slide_unlock);
        AppUnLockFragment unLock=new AppUnLockFragment();
        AppLockFragment lock=new AppLockFragment();
        mFragments.add(unLock);
        mFragments.add(lock);
        mAppViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.tv_lock:
                mAppViewPager.setCurrentItem(1);
                break;
            case R.id.tv_unlock:
                mAppViewPager.setCurrentItem(0);
                break;
        }
    }
    class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm){
            super(fm);
        }
        @Override
        public Fragment getItem(int arg0){
            return mFragments.get(arg0);
        }
        @Override
        public int getCount(){
            return mFragments.size();
        }
    }
}
