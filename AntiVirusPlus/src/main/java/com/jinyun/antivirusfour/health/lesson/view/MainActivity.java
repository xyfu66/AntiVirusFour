package com.jinyun.antivirusfour.health.lesson.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinyun.antivirusfour.Application;
import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.health.lesson.fragment.FoundFragment;
import com.jinyun.antivirusfour.health.lesson.fragment.MeFragment;
import com.jinyun.antivirusfour.health.lesson.fragment.TrainingFragment;
import com.jinyun.antivirusfour.health.lesson.view.base.BaseActivity;

/**
 * 主界面
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout tabTrain;
    private LinearLayout tabFound;
    private LinearLayout tabMe;

    private ImageView btnCheck;
    private ImageView btnAddNews;
    private ImageView icoTrain;
    private ImageView icoFound;
    private ImageView icoMe;

    private TextView txtTrain;
    private TextView txtFound;
    private TextView txtMe;
    private TextView txtTitle;

    private TrainingFragment trainingFragment;
    private FoundFragment foundFragment;
    private MeFragment meFragment;
    private long mExitTime;

    private final static int REQUESTCODE = 1909; // 返回的结果码
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lession);
        findViewById();
        initView();
        btnCheck.setVisibility(View.GONE);//设置锻炼页面右上角图标不可见

        initFlag();
    }

    @Override
    protected void findViewById() {
        tabTrain = $(R.id.bottom_train);
        tabFound = $(R.id.bottom_found);
        tabMe = $(R.id.bottom_me);
        btnCheck = $(R.id.up_btn_check);
        btnAddNews = $(R.id.found_new_add);

        icoTrain = $(R.id.bottom_ico_train);
        icoFound = $(R.id.bottom_ico_found);
        icoMe = $(R.id.bottom_ico_me);

        txtTrain = $(R.id.bottom_txt_train);
        txtFound = $(R.id.bottom_txt_found);
        txtMe = $(R.id.bottom_txt_me);
        txtTitle = $(R.id.titleText);
    }

    @Override
    protected void initView() {
        tabTrain.setOnClickListener(this);
        tabFound.setOnClickListener(this);
        tabMe.setOnClickListener(this);
        btnCheck.setOnClickListener(this);
        btnAddNews.setOnClickListener(this);

        trainingFragment = new TrainingFragment();
        foundFragment = new FoundFragment();
        meFragment = new MeFragment();

        refreashFragment(R.id.bottom_train);
    }

    private void initFlag(){
        FragmentManager manager_ff_flag = getFragmentManager();
        FragmentTransaction transaction_ff_flag = manager_ff_flag.beginTransaction();
        foundFragment =new FoundFragment();
        // 作用:存储数据，并传递到Fragment中
        Bundle bundle = new Bundle();
        // 往bundle中添加数据
        bundle.putInt("flag", flag);
        // 把数据设置到Fragment中
        foundFragment.setArguments(bundle);
        transaction_ff_flag.commit();

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_train:
                changeTabState(R.id.bottom_train);
                changeTitle(R.string.title_train);
                topButtonChange(R.id.up_btn_check);
                refreashFragment(R.id.bottom_train);
                break;
            case R.id.bottom_found:
                topButtonChange(R.id.found_new_add);
                changeTabState(R.id.bottom_found);
                changeTitle(R.string.title_found);
                refreashFragment(R.id.bottom_found);
                break;
            case R.id.bottom_me:
                topButtonChange(0);
                changeTabState(R.id.bottom_me);
                changeTitle(R.string.title_me);
                refreashFragment(R.id.bottom_me);
                break;

            case R.id.found_new_add:
                Intent localIntent = new Intent(this, ReleaseNewsActivity.class);
                startActivityForResult(localIntent,REQUESTCODE);
                break;
        }
    }

    public void topButtonChange(int id) {
        if (id == R.id.up_btn_check) {
            btnCheck.setVisibility(View.GONE);
            btnAddNews.setVisibility(View.GONE);
        } else if (id == R.id.found_new_add) {
            btnCheck.setVisibility(View.GONE);
            btnAddNews.setVisibility(View.VISIBLE);
        } else {
            btnCheck.setVisibility(View.GONE);
            btnAddNews.setVisibility(View.GONE);
        }
    }

    private void changeTitle(int stringId) {
        // txtTitle.setText(getResources().getString(stringId));
    }

    /**
     * 切换Fragment
     * @param btnId
     */
    private void refreashFragment(int btnId) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (btnId) {
            case R.id.bottom_train:
                transaction.replace(R.id.fragment_container, trainingFragment);
                break;
            case R.id.bottom_found:
                transaction.replace(R.id.fragment_container, foundFragment);
                break;
            case R.id.bottom_me:
                transaction.replace(R.id.fragment_container, meFragment);
                break;
        }

        transaction.commit();
    }

    private void changeTabState(int tabId) {
        if (tabId == R.id.bottom_train) {
            icoTrain.setImageResource(R.drawable.icon_train_pressed);
            txtTrain.setTextColor(getResources().getColor(R.color.bottom_tab_pressed));
        } else {
            icoTrain.setImageResource(R.drawable.icon_train_unpressed);
            txtTrain.setTextColor(getResources().getColor(R.color.bottom_tab_normal));
        }
        if (tabId == R.id.bottom_found) {
            icoFound.setImageResource(R.drawable.icon_found_pressed);
            txtFound.setTextColor(getResources().getColor(R.color.bottom_tab_pressed));
        } else {
            icoFound.setImageResource(R.drawable.icon_found_unpressed);
            txtFound.setTextColor(getResources().getColor(R.color.bottom_tab_normal));
        }
        if (tabId == R.id.bottom_me) {
            icoMe.setImageResource(R.drawable.icon_me_pressed);
            txtMe.setTextColor(getResources().getColor(R.color.bottom_tab_pressed));
        } else {
            icoMe.setImageResource(R.drawable.icon_me_unpressed);
            txtMe.setTextColor(getResources().getColor(R.color.bottom_tab_normal));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case REQUESTCODE:
                    //如果启动添加笔记页面，并且有返回了
                    flag = data.getIntExtra("flag", 0);

                    // 动态添加fragment
                    // 即将创建的fragment添加到Activity布局文件中定义的占位符中（FrameLayout）
                    FragmentManager manager_ff_flag = getFragmentManager();
                    FragmentTransaction transaction_ff_flag = manager_ff_flag.beginTransaction();
                    final  FoundFragment foundFragment_ff_flag = new FoundFragment();
                    // 作用:存储数据，并传递到Fragment中
                    Bundle bundle = new Bundle();
                    // 往bundle中添加数据
                    bundle.putInt("flag", flag);
                    // 把数据设置到Fragment中
                    foundFragment_ff_flag.setArguments(bundle);
                    transaction_ff_flag.commit();

                    break;
                default:
                    break;
            }
        }

    }
}
