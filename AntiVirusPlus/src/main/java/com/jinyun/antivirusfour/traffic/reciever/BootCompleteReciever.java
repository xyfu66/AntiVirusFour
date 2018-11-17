package com.jinyun.antivirusfour.traffic.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jinyun.antivirusfour.traffic.service.TrafficMonitoringService;
import com.jinyun.antivirusfour.traffic.utils.SystemInfoUtils;

/**
 *  广播服务
 */

public class BootCompleteReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        //开机广播，判断流量监控服务是否开启，如果没开启则开启
        if(!SystemInfoUtils.isServiceRunning(context,"com.jinyun.antivirus.traffic.service.TrafficMonitoringService")){
            //开启服务
            context.startService(new Intent(context,TrafficMonitoringService.class));
        }
    }
}
