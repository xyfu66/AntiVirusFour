package com.jinyun.antivirusfour.health.foodsDetailOne.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.jinyun.antivirusfour.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Smile Wei
 * @since 2016.08.11
 */
public class FooterLoading extends RelativeLayout {

    @Bind(R.id.loading_progress_bar)
    ProgressBar loadingProgressBar;
    @Bind(R.id.load_full)
    TextView loadFull;

    public FooterLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FooterLoading(Context context) {
        super(context);
        initView(context);
    }

    public FooterLoading(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_more, this, true);

        ButterKnife.bind(view);
    }

    public void onLoad(boolean isFull) {
        loadingProgressBar.setVisibility(isFull ? GONE : VISIBLE);
        loadFull.setVisibility(isFull ? VISIBLE : GONE);
    }
}
