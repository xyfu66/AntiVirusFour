package com.jinyun.antivirusfour.health.cook.fragment;

import android.support.v4.app.Fragment;

/**
 *
 * 懒加载Fragment
 */
public abstract class LazyFragment extends Fragment {

//    protected boolean isVisibleToUser;

    /**
     * 在这里实现Fragment数据的缓加载.
     *
     * @param isVisibleToUser 当前的Fragment是否显示在屏幕中
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
//            this.isVisibleToUser = true;
            onVisible();
        } else {
//            this.isVisibleToUser = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected abstract void lazyLoad();

    protected void onInvisible() {}


}
