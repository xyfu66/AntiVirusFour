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
public class GoodsDetailPagerTwoAdapter extends FragmentPagerAdapter {
    private String[] title;

    public GoodsDetailPagerTwoAdapter(FragmentManager fm, String[] title) {
        super(fm);
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                List<DescBase> list = new ArrayList<>();
                DescBase bean = new DescBase(DescBase.TYPE_DESC,
                        "好吃，已经吃了三包了，爸妈也很喜欢，\n" +
                        "还会继续回购超值，三只松鼠一直很好吃，\n" +
                        "吃好还会再来的，三只松鼠家的零食真的是百吃不厌，\n" +
                        "总要多买点囤在家里，每一样都好吃比超市还优惠很多，\n" +
                        "不管是自己吃还是送人都很不错，礼盒更是高大尚，\n" +
                        "太棒了零食种类超级多的，各色各样的，\n" +
                        "我都感觉要挑花眼了，最后就不纠结了，\n" +
                        "生产日也是最新的，收到就迫不及待的打开一袋吃起来了，\n" +
                        "味道一如既往的好吃，零食就是好！\n" +
                        "朋友们，喜欢的话，赶紧下单呢！\n" +
                        "", "");
                list.add(bean);
                list.add(new DescBase(DescBase.TYPE_PICTURE, "", "https://img.alicdn.com/imgextra/i1/880734502/TB2ZGMdhk7mBKNjSZFyXXbydFXa-880734502.jpg"));
                list.add(new DescBase(DescBase.TYPE_PICTURE, "", "https://img.alicdn.com/imgextra/i1/880734502/TB2pZL7a.D.BuNjt_h7XXaNDVXa-880734502.jpg"));
                list.add(new DescBase(DescBase.TYPE_PICTURE, "", "https://img.alicdn.com/imgextra/i4/880734502/TB2JdMghljTBKNjSZFuXXb0HFXa-880734502.jpg"));
                list.add(new DescBase(DescBase.TYPE_PICTURE, "", "https://img.alicdn.com/imgextra/i2/880734502/TB22gD9a8cXBuNjt_XoXXXIwFXa-880734502.jpg"));
                list.add(new DescBase(DescBase.TYPE_PICTURE, "", "https://img.alicdn.com/imgextra/i3/880734502/TB2XUvhu1OSBuNjy0FdXXbDnVXa-880734502.jpg"));
                return GoodsDescFragment.newInstance(list);
            }
            default: {
                List<CommentBase> list = new ArrayList<>();
                for (int i = 0; i < 25; i++) {
                    CommentBase bean = new CommentBase();
                    bean.setContent("赶紧下单呢！");
                    bean.setPicture("https://cdn.56come.cn/upload/6571/2016/0428/8uxd63yvy4umfwuprwhsuqb5g.thumb.jpg");
                    bean.setProduct_score(5);
                    bean.setType(Type.TYPE_SHOW);
                    bean.setCreate_date(System.currentTimeMillis() - 10000000 * i);
                    bean.setUser_name("WuBiTwo" + i);
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
