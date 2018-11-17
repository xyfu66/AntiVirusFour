package com.jinyun.antivirusfour.emptyFolderCleanup;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jinyun.antivirusfour.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
    {

        private Context context;
        private List<DataBean> emptyFiles;
        private OnItemclickListener onItemclickListener;

        public RecyclerViewAdapter(Context context, List<DataBean> emFiles) {
            this.context = context;
            this.emptyFiles = emFiles;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_empty_recyclerview,null));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            DataBean dataBean = emptyFiles.get(position);
            holder.textView.setText(dataBean.getFile().getPath());
            if(dataBean.isSelected())
            {
                holder.textView.setBackgroundColor(Color.parseColor("#cccccc"));
            }
            else
            {
                holder.textView.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        public void setOnItemclickListener(OnItemclickListener onItemclickListener) {
            this.onItemclickListener = onItemclickListener;
        }

        @Override
        public int getItemCount() {
            return emptyFiles.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
               textView = (TextView) itemView.findViewById(R.id.textView);
                if(onItemclickListener!=null)
                {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemclickListener.onItemClickListener(v,getLayoutPosition());
                        }
                    });
                }
            }
            
        }

        interface OnItemclickListener
        {
            void onItemClickListener(View view, int position);
        }
    }