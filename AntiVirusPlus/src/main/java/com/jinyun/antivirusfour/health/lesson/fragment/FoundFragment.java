package com.jinyun.antivirusfour.health.lesson.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.health.lesson.adapter.FoundNewsAdapter;
import com.jinyun.antivirusfour.health.lesson.entity.NewsListForFound;
import com.jinyun.antivirusfour.health.lesson.utils.Constants;
import com.jinyun.antivirusfour.health.lesson.view.NewsDetailActivity;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;



public class FoundFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView foundList;
    private FoundNewsAdapter adapter;
    private Context mContext;

    private List<NewsListForFound> mList;

    private String titleStr = "第一天";
    private String contentStr = null;
    private String img_path=null;
    private int flag = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_found, null);
        findViewById(v);

        // 步骤1:通过getArgments()获取从Activity传过来的全部值
        Bundle bundle_flag = this.getArguments();
        // 步骤2:获取某一值
        flag = bundle_flag.getInt("flag");

        initView();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();


        if (flag==1){
            reLoadNews(1);
        }
    }

    private void reLoadNews(int id) {
        try {

            mList = new LinkedList<NewsListForFound>();
            mList.add(new NewsListForFound(1, titleStr,"无笔健康" ,1));
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            mList = null;
        }


        switch (id) {
            case 1:
                if (mList != null && mList.size() > 0) {
                    adapter = new FoundNewsAdapter(mContext, mList);
                    foundList.setAdapter(adapter);
                }
                break;
            default:
                Toast.makeText(mContext, "what！", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    public void findViewById(View v) {
        foundList = (ListView) v.findViewById(R.id.found_list);
    }

    public void initView() {
        mContext = getActivity();
        foundList.setOnItemClickListener(this);

        if (flag ==1){
            // 步骤1:通过getArgments()获取从Activity传过来的全部值
            Bundle bundle = this.getArguments();
            // 步骤2:获取某一值
            titleStr = bundle.getString("titleStr");
            contentStr = bundle.getString("contentStr");
            img_path = bundle.getString("img_path");
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), NewsDetailActivity.class);
        intent.putExtra("newsId", mList.get(position).getNewsId());
        startActivity(intent);
    }


}
