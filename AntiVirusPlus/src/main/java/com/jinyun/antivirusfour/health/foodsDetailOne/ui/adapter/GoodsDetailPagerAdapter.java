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
public class GoodsDetailPagerAdapter extends FragmentPagerAdapter {
    private String[] title;

    public GoodsDetailPagerAdapter(FragmentManager fm, String[] title) {
        super(fm);
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                List<DescBase> list = new ArrayList<>();
                DescBase bean = new DescBase(DescBase.TYPE_DESC,
                        "好吃，已经吃了三包了，爸妈也很喜欢，还会继续回购\n" +
                        "收到货立马打开吃了一块，味道很赞，微甜，\n" +
                        "沙沙的有点类似口酥，很好吃，俩孩子也一人吃一块，\n" +
                        "吃完还要，怕吃饱了不吃饭，没敢让她们多吃，我买这个是为了代餐减肥，\n" +
                        "因为味道很赞又立马下单了三盒，希望这个饼干能代替吃零食的欲望，\n" +
                        "管住嘴，要是能减肥我会一直吃下去的\n" +
                        "因为昨天回老家了没在店里，昨天隔壁邻居就帮忙给签收了,\n" +
                        "今天一回来就迫不及待打来吃了，口感不错，\n" +
                        "有一点点的甜味，但不是放糖的那种甜，还香香的，\n" +
                        "物流也挺快的，吃完了还会回来回购的，\n" +
                        "", "");
                list.add(bean);
                list.add(new DescBase(DescBase.TYPE_PICTURE, "", "https://img.alicdn.com/imgextra/i3/735580970/TB2Oxk3mnnI8KJjSszgXXc8ApXa_!!735580970.jpg"));
                list.add(new DescBase(DescBase.TYPE_PICTURE, "", "https://img.alicdn.com/imgextra/i1/735580970/TB28eFdmtrJ8KJjSspaXXXuKpXa_!!735580970.jpg"));
                list.add(new DescBase(DescBase.TYPE_PICTURE, "", "https://img.alicdn.com/imgextra/i1/735580970/TB2wdIRmgvD8KJjy0FlXXagBFXa_!!735580970.jpg"));
                list.add(new DescBase(DescBase.TYPE_PICTURE, "", "https://img.alicdn.com/imgextra/i1/735580970/TB2R57hmnnI8KJjSszgXXc8ApXa_!!735580970.jpg"));
                list.add(new DescBase(DescBase.TYPE_PICTURE, "", "https://img.alicdn.com/imgextra/i4/735580970/TB2VIVsmrYI8KJjy0FaXXbAiVXa_!!735580970.jpg"));
                return GoodsDescFragment.newInstance(list);
            }
            default: {
                List<CommentBase> list = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    CommentBase bean = new CommentBase();
                    bean.setContent("废话不说 明晚来抢");
                    bean.setPicture("https://cdn.56come.cn/upload/6571/2016/0428/8uxd63yvy4umfwuprwhsuqb5g.thumb.jpg");
                    bean.setProduct_score(5);
                    bean.setType(Type.TYPE_SHOW);
                    bean.setCreate_date(System.currentTimeMillis() - 10000000 * i);
                    bean.setUser_name("WuBi" + i);
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
