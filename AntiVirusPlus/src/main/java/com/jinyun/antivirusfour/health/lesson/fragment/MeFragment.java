package com.jinyun.antivirusfour.health.lesson.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jinyun.antivirusfour.Application;
import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.health.lesson.utils.AppManager;



public class MeFragment extends Fragment implements View.OnClickListener {

    private LinearLayout homepage;
    private LinearLayout comment;
    private LinearLayout record;
    private LinearLayout favor;

    private TextView usernameTV;
    private TextView exerciseTimeTextView;
    private TextView recordDaysTextView;
    private TextView exit;

    private String TrainTotalTime="0";

    //更新完成的周数
    private Handler mTrainHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 59:
                    String resultTotalTime = (String)msg.obj;

                    sendEmptyMessageDelayed(0, 100);
                    myDismiss(resultTotalTime+":0",1);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_me, null);
        findViewById(v);
        initView();

        return v;
    }


    public void findViewById(View v) {
        homepage = (LinearLayout) v.findViewById(R.id.me_homepage);
        comment = (LinearLayout) v.findViewById(R.id.me_item_comment);
        record = (LinearLayout) v.findViewById(R.id.me_item_reord);
        favor = (LinearLayout) v.findViewById(R.id.me_item_favor);

        usernameTV = (TextView) v.findViewById(R.id.me_homepage_username);
        exerciseTimeTextView = (TextView) v.findViewById(R.id.me_exercise_time);
        recordDaysTextView = (TextView) v.findViewById(R.id.me_record_days);
        exit = (TextView) v.findViewById(R.id.me_item_exit_ok);
    }

    public void initView() {
        homepage.setOnClickListener(this);
        comment.setOnClickListener(this);
        record.setOnClickListener(this);
        favor.setOnClickListener(this);

        exit.setOnClickListener(this);

        echo();
        getRecords();
    }

    //这里判断视频有没有看，看了的话锻炼时间是多少
    private void getRecords() {
        //获取视频结束时的时间
        final Application application = (Application) getActivity().getApplication();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){

                    double the_period_of_an_activity = application.getThe_period_of_an_activity();
                    TrainTotalTime = Math.round(the_period_of_an_activity/6000)/10.0 +"";//保留一位小数

                    Message msg = new Message();
                    msg.what = 59;
                    msg.obj = TrainTotalTime;
                    mTrainHandler.sendMessage(msg);

                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();


    }

    /**
     * 回显
     */
    private void echo() {
        usernameTV.setText(R.string.StringHealth);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_homepage:
//                startActivity(new Intent(getActivity(), HomepageActivity.class));
                break;
            case R.id.me_item_comment:
//                startActivity(new Intent(getActivity(), CommentsL0istActivity.class));
                break;
            case R.id.me_item_favor:
//                startActivity(new Intent(getActivity(), FavorsListActivity.class));
                break;
            case R.id.me_item_reord:
//                startActivity(new Intent(getActivity(), BeforeDateCheckActivity.class));
                break;
            case R.id.me_item_exit_ok:
                SystemClock.sleep(500);
                AppManager.getInstance().killAllActivity();
//                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
        }
    }

    private void myDismiss(String response, int id){
        switch (id) {
            case 1:
                String[] items = response.split(":");
                exerciseTimeTextView.setText(items[0]);
                recordDaysTextView.setText(items[1]);
                break;
            case 2:

                break;
            default:
                Toast.makeText(getActivity(), "what?", Toast.LENGTH_SHORT).show();
                break;
        }
    }




}
