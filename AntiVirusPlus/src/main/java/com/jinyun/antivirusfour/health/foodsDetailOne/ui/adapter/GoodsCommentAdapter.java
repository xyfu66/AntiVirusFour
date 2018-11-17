package com.jinyun.antivirusfour.health.foodsDetailOne.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.health.foodsDetailOne.base.Type;
import com.jinyun.antivirusfour.health.foodsDetailOne.model.entity.CommentBase;
import com.jinyun.antivirusfour.health.foodsDetailOne.utils.DateFormatUtil;
import com.jinyun.antivirusfour.health.foodsDetailOne.utils.GlideUtil;
import com.jinyun.antivirusfour.health.foodsDetailOne.widget.FooterLoading;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Smile Wei
 * @since 2016/8/12.
 */
public class GoodsCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private Activity activity;
    private List<CommentBase> list;
    private LayoutInflater inflate;

    public GoodsCommentAdapter(Context context, Activity activity, List<CommentBase> list) {
        this.context = context;
        this.activity = activity;
        this.list = list;
        inflate = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case Type.TYPE_SHOW:
                view = inflate.inflate(R.layout.item_goods_comment, parent, false);
                return new ViewHolder(view);
            default:
                view = inflate.inflate(R.layout.item_footer_loading, parent, false);
                return new FooterHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            CommentBase bean = list.get(position);
            GlideUtil.load(context, activity, bean.getPicture(), holder.ivUser);
            holder.tvName.setText(bean.getUser_name());
            holder.tvTime.setText(DateFormatUtil.formatDate(DateFormatUtil.FORMAT_yyyyMMdd_HHmm, bean.getCreate_date()));
            holder.ratingBar.setNumStars(bean.getProduct_score());
            holder.ratingBar.setRating(bean.getProduct_score());
            holder.tvComment.setText(bean.getContent());
        } else if (viewHolder instanceof FooterHolder) {
            FooterHolder holder = (FooterHolder) viewHolder;
            holder.footerLoading.onLoad(Type.TYPE_FOOTER_FULL == list.get(position).getType());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_user)
        ImageView ivUser;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.rating_bar)
        RatingBar ratingBar;
        @Bind(R.id.tv_comment)
        TextView tvComment;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.footer)
        FooterLoading footerLoading;

        FooterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
