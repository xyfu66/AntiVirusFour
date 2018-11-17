package com.jinyun.antivirusfour.health.adapter;


import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinyun.antivirusfour.R;

/**
 * 无笔健康主界面的适配器
 */
public class HealthAdapter extends BaseAdapter {

    //图片数组
    int[] imgId={R.drawable.icon_health_report,R.drawable.icon_health_running,R.drawable.icon_health_news,
            R.drawable.icon_health_device,R.drawable.icon_health_food,R.drawable.icon_health_lession};
    String[] names={"健康报告","运动健康","健康新闻", "智能设备","健康菜谱","课程"};

    private Context context;

    public HealthAdapter(Context context) {
        this.context=context;
    }

    //设置GridView一共有多少个条目
    @Override
    public int getCount() {
        return 6;
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
        View view= View.inflate(context,R.layout.item_health,null);
        ImageView iv_icon= (ImageView) view.findViewById(R.id.iv_ivon_health);
        TextView tv_name= (TextView) view.findViewById(R.id.tv_name_health);
        iv_icon.setImageResource(imgId[position]);
//        iv_icon.getBackground().setAlpha(100);//0~255透明度值 （只是每个条目的背景透明而已）

        tv_name.setText(names[position]);
        tv_name.setTextColor(Color.rgb(0, 0, 0));//设置黑色
        TextPaint paint = tv_name.getPaint();//设置字体加粗
        paint.setFakeBoldText(true);
        return view;
    }
}

