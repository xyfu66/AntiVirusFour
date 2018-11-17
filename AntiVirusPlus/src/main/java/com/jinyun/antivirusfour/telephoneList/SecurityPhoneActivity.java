package com.jinyun.antivirusfour.telephoneList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.telephoneList.adapter.BlackContactAdapter;
import com.jinyun.antivirusfour.telephoneList.db.dao.BlackNumberDao;
import com.jinyun.antivirusfour.telephoneList.entity.BlackContactInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示黑名单信息界面
 */

public class SecurityPhoneActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 有黑名单时，显示的帧布局
     */
    private FrameLayout mHaveBlackNumber;

    /**
     * 没有黑名单时，显示的帧布局
     */
    private FrameLayout mNoBlackNumber;
    private BlackNumberDao dao;
    private ListView mListView;
    private int pagenumber = 0;
    private int pagesize = 15;
    private int totalNumber;
    private List<BlackContactInfo> pageBlackNumber = new ArrayList<BlackContactInfo>();
    private BlackContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_securityphone);
        initView();
        fillData();
    }


    /**
     * 当Activity回到前台时调用
     * 判断数据库总条目是否发生变化，如果变化则将页码置为0，清空黑名单中数据重写添加，并且更新数据
     *
     */
    @Override
    protected void onResume(){
        super.onResume();
        if (totalNumber != dao.getTotalNumber()) {
            //数据发生变化
            if (dao.getTotalNumber() > 0) {
                mHaveBlackNumber.setVisibility(View.GONE);
                mNoBlackNumber.setVisibility(View.VISIBLE);
            } else {
                mHaveBlackNumber.setVisibility(View.GONE);
                mNoBlackNumber.setVisibility(View.VISIBLE);
            }
            pagenumber=0;
            pageBlackNumber.clear();
            pageBlackNumber.addAll(dao.getPageBlackNumber(pagenumber,pagesize));
            if (adapter != null){
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 用于填充数据，重新刷新界面
     */
    private void fillData() {
        dao = new BlackNumberDao(SecurityPhoneActivity.this );
        totalNumber=dao.getTotalNumber() ;
        if(totalNumber==0) {
            //数据库中没有黑名单数据
            mHaveBlackNumber.setVisibility(View.GONE);
            mNoBlackNumber.setVisibility(View.VISIBLE);
        }else if (totalNumber>0){
            //数据库中含有黑名单数据
            mHaveBlackNumber.setVisibility(View.VISIBLE);
            mNoBlackNumber.setVisibility(View.GONE);
            pagenumber=0;
            if (pageBlackNumber.size() >0) {
                pageBlackNumber.clear();
            }
            pageBlackNumber.addAll(dao.getPageBlackNumber(pagenumber,pagesize));
            if (adapter==null) {
                adapter = new BlackContactAdapter(pageBlackNumber,SecurityPhoneActivity.this);
                adapter.setCallBack(new BlackContactAdapter.BlackConactCallBack(){
                    @Override
                    public void DataSizeChanged() {
                        fillData();
                    }
                });
                mListView.setAdapter(adapter);
            }else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 对控件进行初始化
     */
    private void initView(){
        //findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.lightBlue));

        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title) ).setText("通讯卫士");
        mLeftImgv.setOnClickListener (this) ;
        mLeftImgv.setImageResource(R.drawable.returns_arrow02) ;
        mHaveBlackNumber= (FrameLayout) findViewById (R.id.fl_haveblacknumber) ;
        mNoBlackNumber=(FrameLayout)findViewById(R.id.fl_noblacknumber);
        findViewById(R.id.btn_addblacknumber).setOnClickListener(this);
        //滑动监听事件，获取数据库中的数据分页显示在界面上，向下滑动时再次加载相同的条目数据
        mListView= (ListView) findViewById(R.id.lv_blacknumbers) ;
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged (AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: //没有滑动的状态
                        //获取最后一个可见条目
                        int lastVisiblePosition = mListView.getLastVisiblePosition();
                        //如果当前条目是最后一个，则查询更多的数据
                        if (lastVisiblePosition == pageBlackNumber.size() - 1) {
                            pagenumber++;
                            if (pagenumber * pagesize >= totalNumber) {
                                Toast.makeText(SecurityPhoneActivity.this,
                                        "没有更多的数据了", Toast.LENGTH_SHORT).show();
                            } else {
                                pageBlackNumber.addAll(dao.getPageBlackNumber(
                                        pagenumber, pagesize));
                                adapter.notifyDataSetChanged();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount){
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.btn_addblacknumber:
                startActivity(new Intent(this,AddBlackNumberActivity.class));
                break;
        }
    }
}
