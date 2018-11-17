package com.jinyun.antivirusfour.processManager.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.processManager.entity.TaskInfo;

import java.util.List;

/**
 * 数据配器
 * 为ListView控件填充数据
 */

public class ProcessManagerAdapter  extends BaseAdapter {
    private Context context;
    private List<TaskInfo> mUsertaskInfos;
    private List<TaskInfo> mSystaskInfos;
    private SharedPreferences mSP;


    public ProcessManagerAdapter(Context context, List<TaskInfo> userTaskInfos, List<TaskInfo> sysTaskInfo) {
        // TODO Auto-generated constructor stub
        //mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        super();
        this.context = context;
        this.mUsertaskInfos = userTaskInfos;
        this.mSystaskInfos = sysTaskInfo;
        mSP = context.getSharedPreferences("config", Context.MODE_PRIVATE);

    }

    /**
     * 用于返回总条目数，如果系统程序大于0并且SharedPreferences中存储的key
     * 为showSystemProcess 的值为true 时，会返回“用户程序+系统程序+2”，
     *这个2 表示的是mProcessNumTV 的数量( 展示是用户程序还是系统程序的文本标签)。
     *否则返回“用户程序+1”，这个1表示展示用户程序个数的mProcessNumTV 文本标签。
     * @return
     */
    @Override
    public int getCount() {
        if (mSystaskInfos.size() > 0 & mSP.getBoolean("showSystemProcess", true)) {
            return mUsertaskInfos.size() + mSystaskInfos.size() + 2;
        } else {
            return mUsertaskInfos.size() + 1;
        }
    }

    /**
     * 返可当前进程对象，首先判断当前位置是否为用户程序个数和系统程序个数的文本标签，
     * 如果不是则判断是否为用户进程，如果是则返回用户进程对象( mUsertasklnfos.get(positi )将用户进程标签减掉)，
     * 否则返回系统进程对象( mSystasklnfos.get(posit2将用户进程个数和两个文本标签都减掉)。
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        if (position == 0 || position == mUsertaskInfos.size() + 1) {
            return null;
        } else if (position <= mUsertaskInfos.size() + 1) {
            //用户进程
            return mUsertaskInfos.get(position - 1);
        } else {
            //系统进程
            return mSystaskInfos.get(position - mUsertaskInfos.size() - 2);
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            TextView tv = getTextView();
            tv.setText("用户进程: " + mUsertaskInfos.size() + "个");
            return tv;
        } else if (position == mUsertaskInfos.size()) {
            TextView tv = getTextView();
            if (mSystaskInfos.size() > 0) {
                tv.setText("系统进程: " + mSystaskInfos.size() + "个");
                return tv;
            }
        }

        //获取TaskInfo对象
        TaskInfo taskInfo = null;
        if (position <= mUsertaskInfos.size()) {
            taskInfo = mUsertaskInfos.get(position - 1);
        } else if (mSystaskInfos.size() > 0) {
            taskInfo = mSystaskInfos.get(position - mUsertaskInfos.size() - 2);
        }
        ViewHolder holder = null;
        if (convertView != null && convertView instanceof RelativeLayout) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = View.inflate(context, R.layout.item_processmanager_list, null);
            holder = new ViewHolder();
            holder.mAppIconImgv = (ImageView) convertView.findViewById(R.id.imgv_appicon_processmana);
            holder.mAppMemoryTV = (TextView) convertView.findViewById(R.id.tv_appmemory_processmana);
            holder.mAppNameTV = (TextView) convertView.findViewById(R.id.tv_appname_processmana);
            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);

        }
        if (taskInfo != null) {
            holder.mAppNameTV.setText(taskInfo.getTask_name());
            holder.mAppMemoryTV.setText("占用内存:" + Formatter.formatFileSize(context, taskInfo.getTask_memory()));
            holder.mAppIconImgv.setImageDrawable(taskInfo.getTask_icon());
            if (taskInfo.getPackageName().equals(context.getPackageName())) {
                holder.mCheckBox.setVisibility(View.GONE);
            } else {
                holder.mCheckBox.setVisibility(View.VISIBLE);
            }
            holder.mCheckBox.setChecked(taskInfo.isChecked);
        }
    return convertView;
}

    /**
     * 用于创建一个TextView,分别用于显示用户进程个数、系统进程个数。
     * 其中用到是一个单位转换的工具类.
     * @return
     */
        private TextView getTextView(){
            TextView tv=new TextView(context) ;
            tv.setBackgroundColor (context.getResources ().getColor(R.color.grey));
            tv.setPadding(dip2px(context, 5),
                    dip2px(context,5),
                    dip2px(context, 5),
                    dip2px(context, 5));
            tv.setTextColor(context.getResources().getColor(R.color.lightBlue));
            return tv;
        }

    /**
     * dp转px
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = getScreenDendity(context);
        return (int)((dpValue * scale) + 0.5f);
    }

    /**屏幕密度比例*/
    public static float getScreenDendity(Context context){
        return context.getResources().getDisplayMetrics().density;//3
    }



    static class ViewHolder{
        ImageView mAppIconImgv;
        TextView mAppNameTV;
        TextView mAppMemoryTV;
        CheckBox mCheckBox;

    }
}