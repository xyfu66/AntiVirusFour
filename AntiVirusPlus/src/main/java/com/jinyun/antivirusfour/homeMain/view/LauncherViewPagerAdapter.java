package com.jinyun.antivirusfour.homeMain.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.jinyun.antivirusfour.R;

import java.util.ArrayList;

/**
 * 轮播图的适配器
 */
public class LauncherViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<Integer> pagesArrayList;
    private View itemView;

    public LauncherViewPagerAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * 设置ViewPager将要显示的数据.
     * 当图片数量小于三张的时候,通过复制组拼数据
     */
    public void setAdapterData(ArrayList<Integer> arrayList){
        pagesArrayList=arrayList;
        if (pagesArrayList.size()<1) {
            Toast.makeText(mContext, "ViewPager item size=0", Toast.LENGTH_LONG).show();
        }else if(pagesArrayList.size()<2){
            pagesArrayList.add(pagesArrayList.get(0));
            pagesArrayList.add(pagesArrayList.get(0));
            pagesArrayList.add(pagesArrayList.get(0));
        }else if(pagesArrayList.size()<3){
            pagesArrayList.add(pagesArrayList.get(0));
            pagesArrayList.add(pagesArrayList.get(1));
        }
        System.out.println("-----> PagerAdapter中item的个数="+pagesArrayList.size());
    }


    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (pagesArrayList.size() > 0) {
            itemView= LayoutInflater.from(mContext).inflate(R.layout.guide_pager_adapter, null);
            itemView.setFocusable(true);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setBackgroundResource(pagesArrayList.get(position%pagesArrayList.size()));
            container.addView(itemView);
            return itemView;
        }
        return null;

    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
