package com.jinyun.antivirusfour.health.foodsDetailOne.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jinyun.antivirusfour.health.foodsDetailOne.base.Type;
import com.jinyun.antivirusfour.health.foodsDetailOne.model.entity.CommentBase;
import com.jinyun.antivirusfour.health.foodsDetailOne.model.entity.DescBase;
import com.jinyun.antivirusfour.health.foodsDetailOne.ui.fragment.GoodsCommentFragment;
import com.jinyun.antivirusfour.health.foodsDetailOne.ui.fragment.GoodsDescFragment;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GoodsDetailPagerFourAdapter extends FragmentPagerAdapter {
    private String[] title;

    public GoodsDetailPagerFourAdapter(FragmentManager fm, String[] title) {
        super(fm);
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                List<DescBase> list = new ArrayList<>();
                DescBase bean = new DescBase(DescBase.TYPE_DESC,
                        "好吃。效果应该要几瓶后才有吧。\n" +
                        "东西没破碎 等吃了再评价\n" +
                        "想常年喝，不知道会不会有效果。不知道保质期多久，分量小，常年喝挺贵\n" +
                        "剛剛收到，包裝非常結實，第一次購入的\n" +
                        "可能我一直喜欢吃黑芝麻，我觉得蛮好吃的，很香\n" +
                        "看了下保质期18个月。味道浓浓的芝麻味，不太甜，口感不错，好吃，回回购。\n" +
                        "味道很好，可以代餐，确实可以补脑，\n" +
                        "作为会计工作者，吃了有效果。乌发还没有看出来～\n" +
                        "", "");
                list.add(bean);
                list.add(new DescBase(DescBase.TYPE_PICTURE, "", "https://img.alicdn.com/imgextra/i4/399037970/TB2_9mqx4GYBuNjy0FnXXX5lpXa_!!399037970.jpg"));
                list.add(new DescBase(DescBase.TYPE_PICTURE, "", "https://img.alicdn.com/imgextra/i1/399037970/TB2Syt5x7OWBuNjSsppXXXPgpXa_!!399037970.jpg"));
                list.add(new DescBase(DescBase.TYPE_PICTURE, "", "https://img.alicdn.com/imgextra/i2/399037970/TB2hjtLx1OSBuNjy0FdXXbDnVXa_!!399037970.jpg"));
                list.add(new DescBase(DescBase.TYPE_PICTURE, "", "https://img.alicdn.com/imgextra/i4/399037970/TB2qXK7pyCYBuNkHFCcXXcHtVXa_!!399037970.jpg"));
                list.add(new DescBase(DescBase.TYPE_PICTURE, "", "https://img.alicdn.com/imgextra/i4/399037970/TB2amKqx4GYBuNjy0FnXXX5lpXa_!!399037970.jpg"));
                return GoodsDescFragment.newInstance(list);
            }
            default: {
                List<CommentBase> list = new ArrayList<>();
                for (int i = 0; i < 18; i++) {
                    CommentBase bean = new CommentBase();
                    bean.setContent("送家人的，很不错！");
                    bean.setPicture("https://cdn.56come.cn/upload/6571/2016/0428/8uxd63yvy4umfwuprwhsuqb5g.thumb.jpg");
                    bean.setProduct_score(5);
                    bean.setType(Type.TYPE_SHOW);
                    bean.setCreate_date(System.currentTimeMillis() - 10000000 * i);
                    bean.setUser_name("WuBiFour" + i);
                    list.add(bean);
                }
                return GoodsCommentFragment.newInstance(list);
            }
        }
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
