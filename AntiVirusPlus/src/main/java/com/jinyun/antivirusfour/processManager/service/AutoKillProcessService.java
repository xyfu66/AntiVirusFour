package com.jinyun.antivirusfour.processManager.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 *当设置界面中的锁屏清理进程按钮开启时，就会打开进程清理的服务，
 * 在该服务中注册监听屏幕锁屏的广播接收者，
 * 当屏幕锁屏时该广播接收到屏幕锁屏的消息后会自动清理进程
 */

public class AutoKillProcessService extends Service {
    private ScreenLockReceiver receiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate () {
        super.onCreate();
        receiver = new ScreenLockReceiver();
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }
    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        receiver =null;
        super.onDestroy();
    }
    class ScreenLockReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                String packname = info.processName;
                am.killBackgroundProcesses(packname);
            }
        }
    }
}
