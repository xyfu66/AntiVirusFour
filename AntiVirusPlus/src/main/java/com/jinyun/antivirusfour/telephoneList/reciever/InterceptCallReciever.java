package com.jinyun.antivirusfour.telephoneList.reciever;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.jinyun.antivirusfour.telephoneList.db.dao.BlackNumberDao;

import java.lang.reflect.Method;

/**
 * 拦截电话的广播接受者
 * 响铃时自动挂断 其不显示在界面上
 * 出于安全考虑，工程师隐藏了挂断电话的服务方法，需要通过反射获取底层服务
 */

public class InterceptCallReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences mSP = context.getSharedPreferences("config", Context.MODE_PRIVATE);


        boolean BlackNumStatus = mSP.getBoolean("BlackNumStatus", true);
        //String serialNumber = PrefUtils.getString(BaseActivity.PREF_BIND_SIM, "", context);

        //如果 不等于数据库里黑名单资料，则黑名单拦截关闭
        if (!BlackNumStatus){
            return;
        }

        BlackNumberDao dao = new BlackNumberDao(context);
        if(!intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String mIncomingNumber = "";
            //如果是来电
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);//Context?

            switch (tm.getCallState()){
                case TelephonyManager.CALL_STATE_RINGING:
                    mIncomingNumber = intent.getStringExtra("incoming_number");
                    int blackContactMode = dao.getBlackContactMode(mIncomingNumber);

                    if (blackContactMode == 1 || blackContactMode == 3){
                        //观察呼叫记录的变化，如果生成记录就把它删掉
                        Uri uri = Uri.parse("content://call_log/calls");
                        context.getContentResolver().registerContentObserver(
                                uri,true,new CallLogObserver(
                                        new Handler(),mIncomingNumber,context));
                        endCall(context);
                    }
                    break;
            }
        }
    }

    /**
     *  内容观察者，观察数据库变化
     */
    private class CallLogObserver extends ContentObserver {
        private String incomingNumber;
        private Context context;

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public CallLogObserver(Handler handler, String incomingNumber, Context context) {
            super(handler);

            this.incomingNumber = incomingNumber;
            this.context = context;
        }
            //观察到数据库内容变化调用的方法
            @Override
            public void onChange (boolean selfChange){
                Log.i("CallLogObserver", "呼叫记录数据库的内容变化了。");
                context.getContentResolver().unregisterContentObserver(this);
                deleteCalllog(incomingNumber, context);
                super.onChange(selfChange);
            }
        }


    /**
     * 清除历史记录，当黑名单中的电话呼人时，手
     * 机系统通话记录中会显示该条记录，因此需要把通话记录中的黑名单通话记录删除。
     * 手机上拨打电话、接听电话等产生的记录都在系统联系人应用下的contacts2.db 数据库中，
     * 使用ContentResolver 对象查询并删除数据库中黑名单号码所产生的记录即可。
     * @param incomingNumber
     * @param context
     */
    public void deleteCalllog (String incomingNumber, Context context) {
        ContentResolver resolver=context.getContentResolver();
        Uri uri= Uri.parse("content://call_log/calls") ;
        Cursor cursor = resolver.query(uri,new String[]{"_id" },"number=?",
        new String[]{ incomingNumber },"_id desc limit 1");
        if (cursor.moveToNext ()){
            String id = cursor.getString(0);
            resolver.delete(uri,"_id=?",new String[]{id});
        }
    }

    /**
    * 挂断电话，需要复制两个aidl
     * 用于挂断黑名单的呼人电话,首先通过反射获取到ServiceManager字节码，
     * 然后通过该字节码获取getService()方法，该方法接收一个String 类型的参数，然后通过invoke()执行getService0方法。
     * 由于getService()方法是静态的,因此invoke0的第一个参数可以为null,第二个参数是TELEPHONY_SERVICE。
     * 由于getService(方法的返回值是一个IBinder 对象(远程服务的代理类)，因此需要使
     * 用AIDL 的规则将其转化为接口类型，由于操作是挂断电话，因此需要使用与电话相关的ITelephony.aidl,
     * 然后调用接口中的endCall0方法将电话挂断即可。
     */
    public void endCall(Context context) {
        try{
            Class clazz = context.getClassLoader().loadClass
                    ("android.os.Service Manager");
            Method method = clazz.getDeclaredMethod("getervice",String.class) ;
            IBinder iBinder=(IBinder) method.invoke(null, Context.TELECOM_SERVICE);
            ITelephony itelephony= ITelephony.Stub.asInterface(iBinder);
            itelephony.endCall();

        }catch (Exception e){
            e.printStackTrace () ;
        }
    }

}

