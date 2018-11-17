package com.jinyun.antivirusfour.health.cook.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.databinding.FragmentDiscoveryBinding;
import com.jinyun.antivirusfour.health.cook.activity.CookDetailsActivity;
import com.jinyun.antivirusfour.health.cook.activity.CookListActivity;
import com.jinyun.antivirusfour.health.cook.adapter.CookListAdapter;
import com.jinyun.antivirusfour.health.cook.entity.CookDetail;
import com.jinyun.antivirusfour.health.cook.entity.CookList;
import com.jinyun.antivirusfour.health.cook.http.HttpMethod;
import com.jinyun.antivirusfour.health.cook.http.HttpSubscribe;
import com.jinyun.antivirusfour.health.cook.util.CommonUtils;
import com.jinyun.antivirusfour.health.cook.view.ItemDivider;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.jinyun.antivirusfour.health.cook.activity.CookListActivity.DETAIL;


/**
 *
 * 主界面下的发现Fragment 使用懒加载进行网络请求，减少启动时间
 */
public class DiscoveryFragment extends LazyFragment implements CookListAdapter.OnItemClickListener {
    private CookListAdapter mAdapter;
    private List<CookDetail> mListCooks;
    private Intent mIntent;
    private int mFecthedCount;
    private int mFecthPage = 1;
    private int mCookSize;
    private boolean isFetchingMore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentDiscoveryBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discovery, null, false);
        mIntent = new Intent(getActivity(), CookDetailsActivity.class);
        mListCooks = new ArrayList<>();
        mAdapter = new CookListAdapter(getActivity(), mListCooks);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.rvList.setLayoutManager(layoutManager);
        binding.rvList.addItemDecoration(new ItemDivider(getActivity(), LinearLayoutManager.VERTICAL));
        binding.rvList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        binding.rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mFecthedCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!isFetchingMore && mFecthedCount < mCookSize && lastVisibleItem + 3 > mFecthedCount) {
                    Log.i(TAG, "滑动到底部");
                    isFetchingMore = true;
                    //加载下一页
                    fetchCookList(++mFecthPage);
                }
            }
        });
        return binding.getRoot();
    }

    /**
     * 懒加载
     */
    @Override
    protected void lazyLoad() {
        fetchCookList(mFecthPage);
    }

    /**
     * 进行网络请求获取菜谱数据列表
     *
     * @param page 请求页码
     */
    private void fetchCookList(final int page) {
        HttpSubscribe<CookList> subscribe = new HttpSubscribe<CookList>() {
            @Override
            public void onNext(CookList cookList) {
                isFetchingMore = false;
                Log.i(TAG, "网络请求成功");
                mListCooks.addAll(cookList.getTngou());
                Log.i(TAG, mCookSize + "");
                mAdapter.notifyItemInserted(mFecthedCount);
                mCookSize = cookList.getTotal();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                isFetchingMore = false;
                mFecthPage--;//请求失败，页码恢复
            }
        };
        HttpMethod.getInstance().fetchCookList(subscribe, 0, page);
    }

    @Override
    public void onItemClick(View v, ImageView iv, Bitmap bitmap, int position) {
        byte[] bitmapByte = CommonUtils.getInstance().bitmap2byte(bitmap);
        mIntent.putExtra(CookListActivity.BITMAP, bitmapByte);
        mIntent.putExtra(DETAIL, mListCooks.get(position));
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), iv,
                getResources().getString(R.string.share_name));
        startActivity(mIntent, options.toBundle());
    }
}
