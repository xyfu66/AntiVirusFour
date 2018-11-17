package com.jinyun.antivirusfour.telephoneList.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.telephoneList.entity.ContactInfo;

import java.util.List;

/**
 * 填充手机联系人信息的数据适配器
 */

public class ContactAdapter extends BaseAdapter {
    private Context context;
    private List<ContactInfo> contactInfos;

    public ContactAdapter(List<ContactInfo> contactInfos, Context context){
        super();
        this.contactInfos=contactInfos;
        this.context=context;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate (context, R.layout.item_list_contact_select,null);
            holder = new ViewHolder();
            holder.mNameTV = (TextView) convertView.findViewById(R.id.tv_name) ;
            holder.mPhoneTV = (TextView) convertView.findViewById(R.id.tv_phone) ;
            holder.mContactImgv = convertView.findViewById(R.id.view1) ;
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mNameTV.setText (contactInfos.get(position).getName());//contactName?name?
        holder.mPhoneTV.setText (contactInfos.get(position).getNumber());//phone?phoneNumber?
        holder.mNameTV.setTextColor(context.getResources().getColor(
                R.color.purple) );
        holder.mPhoneTV.setTextColor (context.getResources ().getColor (
                R.color.purple) );
        holder.mContactImgv.setBackgroundResource (
                R.drawable.sgame0);

        return convertView;
    }

    static class ViewHolder{
        TextView mNameTV;
        TextView mPhoneTV;
        View mContactImgv;
    }

}
