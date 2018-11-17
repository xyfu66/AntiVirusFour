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
 * 第一列商品的适配器
 */

public class HealthGoodsOneAdapter extends BaseAdapter {

    //图片数组
    private  int[] imgId={R.drawable.food01, R.drawable.food02,R.drawable.food03};
    private String[] describes={"【薏米红豆燕麦全麦饼干】","【三只松鼠_零食大礼包】休闲网红小吃货一箱整箱批发端午节礼包","【螺旋藻全麦饼干】"};
    private String[] cost={"¥ 24.00","¥ 49.90","¥ 29.90"};

    private Context context;

    public HealthGoodsOneAdapter(Context context) {
        this.context=context;
    }

    //设置GridView一共有多少个条目
    @Override
    public int getCount() {
        return 3;
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
        View view= View.inflate(context,R.layout.item_health_goods_one,null);
        ImageView iv_health_goods_one= (ImageView) view.findViewById(R.id.iv_health_goods_one);
        iv_health_goods_one.setImageResource(imgId[position]);

        //描述的文字介绍
        TextView tv_health_goods_one_describe= (TextView) view.findViewById(R.id.tv_health_goods_one_describe);
        tv_health_goods_one_describe.setText(describes[position]);
        TextPaint paint_describe = tv_health_goods_one_describe.getPaint();//设置字体加粗
        paint_describe.setFakeBoldText(true);

        //价格
        TextView tv_health_goods_one_name= (TextView) view.findViewById(R.id.tv_health_goods_one_name);
        tv_health_goods_one_name.setText(cost[position]);
        TextPaint paint = tv_health_goods_one_name.getPaint();//设置字体加粗
        paint.setFakeBoldText(true);
        return view;

    }

}
