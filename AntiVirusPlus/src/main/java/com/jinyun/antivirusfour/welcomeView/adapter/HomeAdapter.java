package com.jinyun.antivirusfour.welcomeView.adapter;


import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinyun.antivirusfour.R;


public class HomeAdapter extends BaseAdapter {

    //图片数组
    int[] imgId={R.drawable.safe02,R.drawable.callmsgsafe04,R.drawable.app03,
            R.drawable.trojan03,R.drawable.sysoptimize03,R.drawable.taskmanager02,
            R.drawable.traffic_icon,R.drawable.empty_icon,R.drawable.atools02};
    String[] names={"手机防盗","通讯卫士","软件管家","手机杀毒","缓存清理","进程管理","流量统计","空文件夹整理","高级工具"};

    private Context context;

    public HomeAdapter(Context context) {
        this.context=context;
    }

    //设置GridView一共有多少个条目
    @Override
    public int getCount() {
        return 9;
    }


    //后面两个方法暂时不需要设置
    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    //设置每个条目的界面
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= View.inflate(context,R.layout.item_home,null);
        ImageView iv_icon= (ImageView) view.findViewById(R.id.iv_ivon);
        TextView tv_name= (TextView) view.findViewById(R.id.tv_name);
        iv_icon.setImageResource(imgId[position]);
        iv_icon.getBackground().setAlpha(100);//0~255透明度值 （只是每个条目的背景透明而已）

        tv_name.setText(names[position]);
        tv_name.setTextColor(Color.rgb(5, 39, 175));//设置科幻蓝
        TextPaint paint = tv_name.getPaint();//设置字体加粗
        paint.setFakeBoldText(true);
        return view;
    }
}

