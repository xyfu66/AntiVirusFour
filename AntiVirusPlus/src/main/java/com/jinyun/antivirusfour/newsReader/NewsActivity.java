package com.jinyun.antivirusfour.newsReader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.newsReader.adapter.NewsAdapter;
import com.jinyun.antivirusfour.newsReader.bean.NewsItemModel;
import com.jinyun.antivirusfour.newsReader.function.Function;
import com.jinyun.antivirusfour.newsReader.tool.CommonTool;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻阅读器
 */

public class NewsActivity extends Activity implements View.OnClickListener{
    private ListView mListView;
    private List<NewsItemModel> list;
    private NewsAdapter adapter;
    //获取数据成功
    private final static int GET_DATA_SUCCEED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        //初始化视图
        initView();
        //初始化数据
        initData();
    }

    public void initView() {
        list = new ArrayList<NewsItemModel>();
        mListView = (ListView) findViewById(R.id.list_view);

        ((TextView) findViewById(R.id.tv_title)).setText("新闻");
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.returns_arrow02);

    }


    public void initData() {
        //开启一个线程执行耗时操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取网络数据
                String result = CommonTool.getRequest("http://news.qq.com/china_index.shtml", "gbk");
                Log.e("结果------------->", result);
                //解析新闻数据
                List<NewsItemModel> list = Function.parseHtmlData(result);

                for (int i = 0; i < list.size(); i++) {
                    NewsItemModel model = list.get(i);
                    //获取新闻图片
                    Bitmap bitmap = BitmapFactory.decodeStream(CommonTool.getImgInputStream(list.get(i).getUrlImgAddress()));

                    model.setNewsBitmap(bitmap);
                }
                mHandler.sendMessage(mHandler.obtainMessage(GET_DATA_SUCCEED, list));
            }
        }).start();
    }


    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_DATA_SUCCEED:
                    List<NewsItemModel> list = (List<NewsItemModel>) msg.obj;
                    //新闻列表适配器
                    adapter = new NewsAdapter(NewsActivity.this, list, R.layout.item_adapter_news);
                    mListView.setAdapter(adapter);
                    //设置点击事件
                    mListView.setOnItemClickListener(new ItemClickListener());
                    Toast.makeText(getApplicationContext(), String.valueOf(list.size()), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }

    /**
     * 新闻列表点击事件
     */
    public class ItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            NewsItemModel temp =(NewsItemModel) adapter.getItem(i);
            Toast.makeText(getApplicationContext(), temp.getNewsTitle(), Toast.LENGTH_SHORT).show();
        }
    }
}
