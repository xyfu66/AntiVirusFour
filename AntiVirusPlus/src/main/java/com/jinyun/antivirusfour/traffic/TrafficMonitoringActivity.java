package com.jinyun.antivirusfour.traffic;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.traffic.db.dao.TrafficDao;
import com.jinyun.antivirusfour.traffic.reciever.BootCompleteReciever;
import com.jinyun.antivirusfour.traffic.service.TrafficMonitoringService;
import com.jinyun.antivirusfour.traffic.utils.SystemInfoUtils;
import com.jinyun.antivirusfour.trafficApps.activity.Showmain;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;

/**
 *  点击 矫正流量 按钮时，会自动向运营商发送一条短信息，获取当前流量使用情况
 *  并显示在界面上。
 */

public class TrafficMonitoringActivity extends Activity implements View.OnClickListener{
    private SharedPreferences mSP;
    private Button mCorrectFlowBtn;
    private TextView mTotalTV;
    private TextView mUsedTV;
    private TextView mToDayTV;
    private TrafficDao dao;
    private ImageView mRemindIMGV;
    private TextView mRemindTV;
    private CorrectFlowReceiver receiver;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_trafficmonitoring);

        // >= 0 : 启动画面是会提示是否允许该App拥有发送短信的权限。
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS} , 0);

        //对象存储的是key为isset—operat0or
        mSP=getSharedPreferences("config",MODE_PRIVATE);
        boolean flag = mSP.getBoolean("isset_operator",false);

        //如果，没有设置运营商则进入信息设置页面
        if(!flag){
            startActivity(new Intent(this,OperatorSetActivity.class));
            finish();
        }
        if(!SystemInfoUtils.isServiceRunning(this,"com.jinyun.antivirusfour.traffic.service.TrafficMonitoringService")){
            startService(new Intent(this,TrafficMonitoringService.class));
        }

        IntentFilter filter = new IntentFilter();//代码中注册
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new BootCompleteReciever(), filter);

        initView();
        registReceiver();
        initData();

    }

    private void initView(){

        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.returns_arrow02);
        ImageView mRightImgv = (ImageView) findViewById(R.id.imgv_rightbtn);
        mRightImgv.setImageResource(R.drawable.photo_more);
        mRightImgv.setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("流量监控");

        mCorrectFlowBtn=(Button) findViewById(R.id.btn_correction_flow);
        mCorrectFlowBtn.setOnClickListener(this);
        mTotalTV=(TextView) findViewById(R.id.tv_month_totalgprs);
        mUsedTV=(TextView) findViewById(R.id.tv_month_usedgprs);
        mToDayTV=(TextView) findViewById(R.id.tv_today_gprs);
        mRemindIMGV=(ImageView) findViewById(R.id.imgv_traffic_remind);
        mRemindTV=(TextView) findViewById(R.id.tv_traffic_remind);
    }

    /**
     * 获取流量使用的具体情况并显示在界面中
     */
    private void initData(){
        long totalflow=mSP.getLong("totalflow",0);
        long usedflow=mSP.getLong("usedflow",0);

        if(totalflow>0 & usedflow>=0){
            float scale=usedflow/totalflow;
            if(scale>0.9){
                mRemindIMGV.setEnabled(false);
                mRemindTV.setText("您的套餐流量即将用完");
            }else{
                mRemindIMGV.setEnabled(true);
                mRemindTV.setText("本月流量充足请放心使用");
            }
        }
        mTotalTV.setText("本月流量："+ Formatter.formatFileSize(this,totalflow));
        mUsedTV.setText("本月已用："+ Formatter.formatFileSize(this,usedflow));
        dao=new TrafficDao(this);
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String dataString=sdf.format(date);
        long moblieGPRS=dao.getMoblieGPRS(dataString);
        if(moblieGPRS<0){
            moblieGPRS=0;
        }
        mToDayTV.setText("本日已用："+ Formatter.formatFileSize(this,moblieGPRS));
    }

    /**
     * 为注册的自定义广播接受者
     *  action 注册信息接收活动
     */
    private void registReceiver(){
        receiver=new CorrectFlowReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiver,filter);
    }

    //代码中主动引导用户开启权限这里没有说明READ_PHONE_STATE的主动获取,大家根据自己的targetSdkVersion设置
    private boolean hasPermissionToReadNetworkStats() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        final AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        requestReadNetworkStats();
        return false;
    }
    // 打开“有权查看使用情况的应用”页面
    private void requestReadNetworkStats() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }



    /**
     * 监控矫正流量按钮的点击事件，按下按钮后首先判断手机号码属于哪一个运营商，
     * 如果没有设置运营商信息，则提示进行设置。
     * @param v
     */
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.imgv_rightbtn:
                //跳转至进程管理设置页面
                startActivity(new Intent(this,Showmain.class ));
                break;
            case R.id.btn_correction_flow:
                //首先判断是那个运营商
                int i=mSP.getInt("operator",0);
                SmsManager smsManger= SmsManager.getDefault();
                switch(i){
                    case 0:
                    //没有设置运营商
                        Toast.makeText(this,"您还没有设置运营商信息",LENGTH_SHORT).show();
                        break;
                    case 1:
                        //中国移动
                        smsManger.sendTextMessage("10086",null,"cxll",null,null);
                        break;
                    case 2:
                        //中国联通，发送LLCK至10010
                        //获取系统默认的短信管理器
                        smsManger.sendTextMessage("10010",null,"LLCX",null,null);
                        break;
                    case 3:
                        //中国电信
                        smsManger.sendTextMessage("10001",null,"1081",null,null);
                        break;
                }
        }
    }

    /**
     * 有短信来时执行onReceive方法
     * 获取短信内容和电话号码
     */
    class CorrectFlowReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            Object[] objs=(Object[]) intent.getExtras().get("pdus");
            for(Object obj:objs){
                SmsMessage smsMessage= SmsMessage.createFromPdu((byte[]) obj);
                String body=smsMessage.getMessageBody();
                String address=smsMessage.getOriginatingAddress();
                //短信分割 流量用户
                if(address.equals("10010") || address.equals("10086") || address.equals("10001")){
                    String[] split=body.split(", ");
                    //本月剩余流量
                    long left=0;
                    //本月已用流量
                    long used=0;
                    //本月超出流量
                    long beyond=0;
                    for(int i=0;i<split.length;i++){
                        if(split[i].contains("本月流量已使用")){
                            //套餐总量
                            String usedflow = split[i].substring(7,split[i].length());
                            used=getStringTofloat(usedflow);
                        }else if(split[i].contains("剩余流量")){
                            String leftflow = split[i].substring(4, split[i].length());
                            left=getStringTofloat(leftflow);
                        }else if(split[i].contains("套餐外流量")){
                            String beyondflow = split[i].substring(5, split[i].length());
                            beyond=getStringTofloat(beyondflow);
                        }
                    }

                    //通过SharedPreferences获取到通过流量校验和在服务中获取到的流量数据，并计算出剩余的流量比例
                    //通过比例判断，已用流量比例小于或者大于90%，则显示不同的文字提示
                    SharedPreferences.Editor edit=mSP.edit();
                    edit.putLong("totalflow",used+left);
                    edit.putLong("usedflow",used+beyond);
                    edit.commit();
                    mTotalTV.setText("本月流量： "+ Formatter.formatFileSize(context,(used+left)));
                    mUsedTV.setText("本月已用： "+ Formatter.formatFileSize(context,(used+beyond)));
                }
            }
        }
    }



    /** 将字符串转化成Float类型数据 */
    private long getStringTofloat(String str){
        long flow=0;
        if(!TextUtils.isEmpty(str)){
            if(str.contains("KB")){
                String[] split=str.split("KB");
                float m= Float.parseFloat(split[0]);
                flow=(long)(m*1024);
            }else if(str.contains("MB")){
                String[] split=str.split("MB");
                float m= Float.parseFloat(split[0]);
                flow=(long)(m*1024*1024);
            }
        }
        return flow;
    }
    @Override
    public void onDestroy(){
        if(receiver!=null){
            unregisterReceiver(receiver);
            receiver=null;
        }
        super.onDestroy();
    }
}
