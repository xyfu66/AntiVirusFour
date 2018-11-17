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
public class GoodsDetailPagerThreeAdapter extends FragmentPagerAdapter {
    private String[] title;

    public GoodsDetailPagerThreeAdapter(FragmentManager fm, String[] title) {
        super(fm);
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                List<DescBase> list = new ArrayList<>();
                DescBase bean = new DescBase(DescBase.TYPE_DESC,
                        "时下，吾已浪迹淘宝数年，但觉世风日下，\n" +
                        "深知各店之猫腻甚多，不乏其闻。\n" +
                        "然，唯此店这宝物与众皆不同，为出淤泥之清莲。\n" +
                        "使吾为之动容，心驰神往。乃至饭不能食，寝则不安，\n" +
                        "辗转反侧无法忘怀。于是乎紧衣缩食，\n" +
                        "凑齐银两，倾吾所能而买。\n" +
                        "掌柜之热心与小二之殷切让人感染，感激怜涕。\n" +
                        "打开包裹之时，顿时金光四射，屋内升起七彩祥云，\n" +
                        "处处都是祥和之气。吾惊讶之余便是欣喜若狂，\n" +
                        "呜呼哀哉！此宝乃是天上物，人间又得几回求！\n" +
                        "遂沐浴更衣，焚香祷告后与家人共赏此宝。\n" +
                        "夫则赞叹不已，不仅赞叹此宝物款型及做工，超高性价比！\n" +
                        "且赞吾独具慧眼与时尚品位，更予唇相赠。\n" +
                        "店主果然句句实言，毫无夸大欺瞒之嫌。\n" +
                        "此属大家风范，忠义之商贾，更无愧于皇冠之衔。\n" +
                        "吾不敢独享此宝，唯恐天谴。\n" +
                        "便有感而出此文，句句真言，字字肺腑。\n" +
                        "", "");
                list.add(bean);
                list.add(new DescBase(DescBase.TYPE_PICTURE, "", "https://img.alicdn.com/imgextra/i1/735580970/TB2cK_AdPgy_uJjSZKPXXaGlFXa_!!735580970.jpg"));
                list.add(new DescBase(DescBase.TYPE_PICTURE, "", "https://img.alicdn.com/imgextra/i1/735580970/TB2o3f3dQfb_uJkHFJHXXb4vFXa_!!735580970.jpg"));
                list.add(new DescBase(DescBase.TYPE_PICTURE, "", "https://img.alicdn.com/imgextra/i2/735580970/TB2IzP1dPgy_uJjSZTEXXcYkFXa_!!735580970.jpg"));
                list.add(new DescBase(DescBase.TYPE_PICTURE, "", "https://img.alicdn.com/imgextra/i4/735580970/TB2NSh.dZic_eJjSZFnXXXVwVXa_!!735580970.jpg"));
                list.add(new DescBase(DescBase.TYPE_PICTURE, "", "https://img.alicdn.com/imgextra/i2/735580970/TB2.1T3dPgy_uJjSZJnXXbuOXXa_!!735580970.jpg"));
                return GoodsDescFragment.newInstance(list);
            }
            default: {
                List<CommentBase> list = new ArrayList<>();
                for (int i = 0; i < 28; i++) {
                    CommentBase bean = new CommentBase();
                    bean.setContent("买买买！");
                    bean.setPicture("https://cdn.56come.cn/upload/6571/2016/0428/8uxd63yvy4umfwuprwhsuqb5g.thumb.jpg");
                    bean.setProduct_score(5);
                    bean.setType(Type.TYPE_SHOW);
                    bean.setCreate_date(System.currentTimeMillis() - 10000000 * i);
                    bean.setUser_name("WuBiThree" + i);
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
