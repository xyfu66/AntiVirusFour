package com.jinyun.antivirusfour.telephoneList.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.telephoneList.db.dao.BlackNumberDao;
import com.jinyun.antivirusfour.telephoneList.entity.BlackContactInfo;

import java.util.List;

/**
 * 填充黑名单的数据填充器
 */

public class BlackContactAdapter extends BaseAdapter {
    private BlackConactCallBack callBack;
    private Context context;
    private BlackNumberDao dao;
    private List<BlackContactInfo> contactInfos;

    public void setCallBack(BlackConactCallBack callBack){
        this.callBack = callBack;
    }

    /**
     * 是Adapter的构造方法，接收两个参数
     * List是从主界面传递的黑名单数据集合，加载到页面上的数据都应该从该集合中取出
     * @param systemContacts
     * @param context
     */
    public BlackContactAdapter(List<BlackContactInfo> systemContacts, Context context) {
        super();
        this.contactInfos = systemContacts;
        this.context = context;
        dao = new BlackNumberDao(context);

    }

    public void notifyDataSetChanged() {
    }

    @Override
    public int getCount() {
        return contactInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return contactInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 定义了每个条目中的删除按钮的点击事件，当点击删除按钮后，
     * 当前条目在数据库中被删除，并刷新当前界面
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context,R.layout.item_list_blackcontact,null);
            holder = new ViewHolder();
            holder.mNameTV = (TextView) convertView.findViewById(R.id.tv_black_name) ;
            holder.mModeTV = (TextView) convertView.findViewById(R.id.tv_black_mode) ;
            holder.mContactImgv = convertView.findViewById(R.id.view_black_icon) ;
            holder.mDeleteView = convertView.findViewById(R.id.view_black_delete);
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }
            holder.mNameTV.setText (contactInfos.get (position).contactName+ "("+
                    contactInfos.get (position).phoneNumber+")") ;
            holder.mModeTV.setText (contactInfos.get (position).getModeString(
                    contactInfos.get (position).mode) );
            holder.mNameTV.setTextColor(context.getResources().getColor(
                    R.color.purple) );
            holder.mModeTV.setTextColor (context.getResources ().getColor (
                    R.color.purple) );
            holder.mContactImgv.setBackgroundResource (
                    R.drawable.sgame0);
            holder.mDeleteView.setOnClickListener (new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean datele=dao.delete (contactInfos.get (position) );
                    if(datele) {
                        contactInfos.remove(contactInfos.get(position));
                        BlackContactAdapter.this.notifyDataSetChanged();
                        //如果数据库中没有数据了，则执行回调函数
                        if (dao.getTotalNumber() == 0) {
                            callBack.DataSizeChanged();
                        }
                    } else {
                        Toast.makeText(context, "删除失败! ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return convertView;

    }

    /**
     * 静态内部类
     * 使ListView中的控件只加载一次，优化加载速度以及内存消耗
     */
    static class ViewHolder{
        TextView mNameTV;
        TextView mModeTV;
        View mContactImgv;
        View mDeleteView;

    }

    /**
     * 回调接口
     * 点击删除按钮后数据库中没有数据调用
     * 数据库中的数据完全删除之后才会调用
     * 删除最后一条数据时会显示默认布局，将ListView隐藏
     */
    public interface BlackConactCallBack{
        void DataSizeChanged();
    }
}
