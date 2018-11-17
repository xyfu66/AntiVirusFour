package com.jinyun.antivirusfour.telephoneList.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsMessage;

import com.jinyun.antivirusfour.telephoneList.db.dao.BlackNumberDao;

/**
 * 拦截短信的广播接受者
 * 当电话或者短信到来时都会产生广播，当黑名单用户打电话发短信，就终止，不让其在界面中产生
 */

public class InterceptSmsReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences mSP = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean BlackNumStatus = mSP.getBoolean("BlackNumStatus",true);
        //黑名单拦截关闭,也用来判断黑名单拦截功能是否开启
        if (!BlackNumStatus){
            return;
        }
        //如果是黑名单则终止广播
        BlackNumberDao dao = new BlackNumberDao(context);
        Object[] objs = (Object[]) intent.getExtras().get("pdus");//用于获取接收到的信息
        for (Object obj : objs) {
            SmsMessage message = SmsMessage.createFromPdu((byte[]) obj);
            String address = message.getOriginatingAddress();
            String msg = message.getMessageBody();

            //对+86的号码进行截取
            if (address.startsWith("+86")){
                address = address.substring(3,address.length());
            }

            //需要拦截短信，拦截广播 通过号码查询数据库，看为哪一种拦截模式
            int mode = dao.getBlackContactMode(address);
            if (mode==2||mode==3){
                abortBroadcast();
            }
        }
    }
}
