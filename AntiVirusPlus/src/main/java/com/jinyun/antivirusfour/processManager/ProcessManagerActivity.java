package com.jinyun.antivirusfour.processManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.processManager.adapter.ProcessManagerAdapter;
import com.jinyun.antivirusfour.processManager.entity.TaskInfo;
import com.jinyun.antivirusfour.processManager.utils.SystemInfoUtils;
import com.jinyun.antivirusfour.processManager.utils.TaskInfoParser;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class ProcessManagerActivity extends Activity implements View.OnClickListener{

    private TextView mRunProcessNum;//private TextView tv_task_manager_task_count;
    private TextView mMemoryTV;//private TextView tv_task_manager_task_memory;
    private TextView mProcessNumTV;
    private ListView mListView;
    private ProcessManagerAdapter adapter;
    private List<TaskInfo> runningTaskInfos;
    private List<TaskInfo> userTaskInfos=new ArrayList<TaskInfo>();
    private List<TaskInfo> sysTaskInfo=new ArrayList<TaskInfo>();
    private ActivityManager manager;
    private int runningPocessCount;
    private long totalMem;

    protected static final int SUCCESS_GET_TASKINFO = 0;

    private RelativeLayout rl_loading;
    private ListView lv_taskmanager;
//
//    private boolean isChecked;
    private ActivityManager am;
//    private List<TaskInfo> taskInfos;
//
//    private SharedPreferences sp;
//
    //private LinearLayout task_manager;

//    private Handler mHandler = new Handler(){
//        public void handleMessage(android.os.Message msg) {
//            switch (msg.what) {
//                case SUCCESS_GET_TASKINFO:
//
//                    //可用内存
//                    String availMemStr = TextFormat.formatByte(TaskUtil.getAvailMem(getApplication()));
//                    //总内存
//                    String totalMemStr = TextFormat.formatByte(TaskUtil.getTotalMemSize());
//                    tv_task_manager_task_memory.setText("可用/总内存:" + availMemStr +"/" + totalMemStr);
//
//
//                    mAdapter = new TaskManagerAdapter();
//                    rl_loading.setVisibility(View.GONE);
//                    lv_taskmanager.setAdapter(mAdapter);
//                    break;
//
//                default:
//                    break;
//            }
//        };
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_processmanager);

        initView();
        fillData();
    }

    /**
     * 当Activity 可见时，如果ListV iew 的适配器对象存在，
     * 则直接调用Adapter的notifyDataSetChanged方法更新已生成的数据列表(适配器中的数据)。
     */
    @Override
    protected void onResume(){
        if (adapter!=null) {
        adapter.notifyDataSetChanged();
        }
        super.onResume();
    }

    private void initView() {

        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.returns_arrow02);
        ImageView mRightImgv = (ImageView) findViewById(R.id.imgv_rightbtn);
        mRightImgv.setImageResource(R.drawable.settings03);
        mRightImgv.setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("进程管理");

        mRunProcessNum = (TextView) findViewById(R.id.tv_runningprocess_num);//tv_runningprocess_num//tv_task_manager_task_count = (TextView) findViewById(R.id.tv_task_manager_task_count);
        mMemoryTV = (TextView) findViewById(R.id.tv_memory_processmanager);//tvmemoryprocessmanager//tv_task_manager_task_memory = (TextView) findViewById(R.id.tv_task_manager_task_memory);
        mProcessNumTV= (TextView) findViewById (R.id.tv_user_runningprocess) ;
        runningPocessCount = SystemInfoUtils.getRunningPocessCount(ProcessManagerActivity.this);
        mRunProcessNum.setText("进程数: " + runningPocessCount + "个");
        long totalAvailMem = SystemInfoUtils.getAvailMem(this);
        totalMem = SystemInfoUtils.getTotalMem();
        mMemoryTV.setText("可用/总内存: " +
                Formatter.formatFileSize(this, totalAvailMem) + "/" +
                Formatter.formatFileSize(this, totalMem));
        mListView = (ListView) findViewById(R.id.lv_taskmanager);
        //lv_taskmanager = (ListView) findViewById(R.id.lv_taskmanager);
        initListener();

        //rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        //task_manager = (LinearLayout) findViewById(R.id.task_manager);
        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    }

    /**
     * 设置监听
     * initListener()方法用于初始化界面，给按钮添加点击监听，
     * 以及设置ListView 的条目点击事件和滚动事件在ListView的条目点击监听的onltemClick()方法中，
     * 循环遍历所有正在运行的程序，然后通过TaskInfo 的isChecked 属性标记是否被选中，并更新数据。
     * 在:ListView 滚动事件监听的onScroll0方法中，用于判断程序在滚动的过程中mProcessNumTV文本显示的内容，
     * 当滚动的数目超出用户进程个数时，显示系统进程的个数，否则显示用户进程个数。
     */
    private void initListener() {
        findViewById(R.id.bt_task_manager_selectAll).setOnClickListener(this) ;
        findViewById(R.id.bt_task_manager_noSelectAll).setOnClickListener(this) ;
        findViewById(R.id.bt_task_manager_noSelect).setOnClickListener (this) ;

        mListView.setOnItemClickListener (new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = mListView.getItemAtPosition(position);
                if (object != null & object instanceof TaskInfo) {
                    TaskInfo info = (TaskInfo) object;
                    if (info.getTask_name().equals(getPackageName())){
                        //当前点击的条目是本应用程序
                        return;
                    }
                    info.isChecked = !info.isChecked;
                    adapter.notifyDataSetChanged();
                }
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem>=userTaskInfos.size()+1) {
                    mProcessNumTV.setText("系统进程: "+sysTaskInfo.size() +"个");
                }else{
                    mProcessNumTV.setText("用户进程: "+userTaskInfos.size () +"个") ;
                }
            }
        });
    }

    /**
     * fllData0方法用于填充数据，在子线程中通过TaskInfoParser类的getRunningTaskInfosO
     * 方法获取所有正在运行的程序，并且将用户程序和系统程序分别存储在userTaskInfos和sysTaskInfo 集合中。
     * 然后判断Adapter 是否为null,如果为null,则创建适配器对象填充ListView 中的数据，
     * 否则直接调用notifyDataSetChanged方法刷新ListView 界面。
     * 最后通过if 语句判断mProcessNumTV 文本标签的显示内容，如果用户集合大于0,则显示用户进程个数，
     * 否则显示系统进程个数。
     */
    private void fillData() {
        userTaskInfos.clear();
        sysTaskInfo.clear();
        new Thread(){
            public void run(){
                runningTaskInfos= TaskInfoParser.getRunningTaskInfos(getApplicationContext());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (TaskInfo taskInfo : runningTaskInfos) {
                            if (taskInfo.isUserApp) {
                                userTaskInfos.add(taskInfo);
                            } else {
                                sysTaskInfo.add(taskInfo);
                            }
                        }
                        if (adapter == null) {
                            adapter = new ProcessManagerAdapter(getApplicationContext(), userTaskInfos, sysTaskInfo);
                            mListView.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        if (userTaskInfos.size() > 0) {
                            mProcessNumTV.setText("用户进程: " + userTaskInfos.size() + "个");
                        } else {
                            mProcessNumTV.setText("系统进程: " + sysTaskInfo.size() + "个");
                        }
                    }
                });
            };
        }.start();
    }

    @Override
    public void onClick(View v) {
            switch (v.getId() ){
                case R.id.imgv_leftbtn:
                    finish();
                    break;
                case R.id.imgv_rightbtn:
                    //跳转至进程管理设置页面
                    startActivity(new Intent(this,ProcessManagerSettingActivity.class ));
                    break;
                case R.id.bt_task_manager_selectAll:
                    selectAll() ;
                    break;
                case R.id.bt_task_manager_noSelectAll:
                    inverse () ;
                    break;
                case R.id.bt_task_manager_noSelect:
                    cleanProcess() ;
                    break;
            }
    }
    /**
     * 清理进程
     */
    @SuppressLint("ServiceCast")//可能不要?
    private void cleanProcess () {
        manager = (ActivityManager) getSystemService(ACCESSIBILITY_SERVICE);
        int count = 0;
        long saveMemory = 0;
        List<TaskInfo> killedtaskInfos = new ArrayList<TaskInfo>();
        //注意，遍历集合时不能改变集合大小
        for (TaskInfo info : userTaskInfos) {
            if (info.isChecked) {
                count++;
                saveMemory += info.getTask_memory();
                manager.killBackgroundProcesses(info.getTask_name());
                killedtaskInfos.add(info);
            }
        }

        for (TaskInfo info : sysTaskInfo) {
            if (info.isChecked){
                count++;
                saveMemory += info.getTask_memory();
                manager.killBackgroundProcesses(info.getTask_name());
                killedtaskInfos.add(info);
            }
        }

        for(TaskInfo info : killedtaskInfos){
            if (info.isUserApp) {
                userTaskInfos.remove(info);
            } else {
                sysTaskInfo.remove(info);
            }
        }
        runningPocessCount-=count;
        mRunProcessNum.setText("运行中的进程: "+runningPocessCount+"个") ;
        mMemoryTV.setText (
                "可用/总内存: "+ Formatter.formatFileSize(this,
                SystemInfoUtils.getAvailMem(this ))+"/"+
                        Formatter.formatFileSize (this,totalMem));
        Toast.makeText (this,"清理了"+count + "个进程，释放了"+
                Formatter.formatFileSize(this,saveMemory)+"内存", Toast.LENGTH_LONG).show();
        mProcessNumTV.setText ("用户进程: "+userTaskInfos.size() +"个") ;
        adapter.notifyDataSetChanged (); ;

        }


        /**
         反选
         */
        private void inverse () {
            for (TaskInfo taskInfo : userTaskInfos) {
                //就是本应用程序
                if (taskInfo.getTask_name().equals (getPackageName() )) {
                    continue;
                }

                boolean checked = taskInfo.isChecked;
                taskInfo.isChecked = !checked;
            }
            for (TaskInfo taskInfo : sysTaskInfo) {
                boolean checked = taskInfo.isChecked;
                taskInfo.isChecked = !checked;
            }
            adapter.notifyDataSetChanged();
        }

    /**
     * 全选
     */
    private void selectAll () {
        for(TaskInfo taskInfo :userTaskInfos){
            //就是本应用程序
                if (taskInfo.getTask_name().equals(getPackageName())){
                    continue;
                }
            taskInfo.isChecked=true;
        }
        for(TaskInfo taskInfo:sysTaskInfo){
            taskInfo.isChecked=true;
        }
        adapter.notifyDataSetChanged();
    }

}
