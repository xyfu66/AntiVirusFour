package com.jinyun.antivirusfour;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


import com.jinyun.antivirusfour.health.lesson.view.VideoPlayer;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Application
 */

public class Application extends android.app.Application {
    private final String TAG = this.getClass().toString();
    FaceDB mFaceDB;
    Uri mImage;

    /**
     * onCreate是一个回调接口，android系统会在应用程序启动的时候，在任何应用程序组件（activity、服务、
     * 广播接收器和内容提供者）被创建之前调用这个接口。
     * 需要注意的是，这个方法的执行效率会直接影响到启动Activity等的性能，因此此方法应尽快完成。
     * 最后在该方法中，一定要记得调用super.onCreate()，否则应用程序将会报错。
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mFaceDB = new FaceDB(this.getExternalCacheDir().getPath());
        mImage = null;

        //初始化XUtils
        x.Ext.init(this);
        //设置debug模式
        x.Ext.setDebug(true);//是否输出Debug日志    发布的时候 设置为false

        correctSIM();
        //注册自己的Activity的生命周期回调接口。
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    public void setCaptureImage(Uri uri) {
        mImage = uri;
    }

    public Uri getCaptureImage() {
        return mImage;
    }

    /**
     * @param path
     * @return
     */
    public static Bitmap decodeImage(String path) {
        Bitmap res;
        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inSampleSize = 1;
            op.inJustDecodeBounds = false;
            //op.inMutable = true;
            res = BitmapFactory.decodeFile(path, op);
            //rotate and scale.
            Matrix matrix = new Matrix();

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.postRotate(90);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.postRotate(180);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                matrix.postRotate(270);
            }

            Bitmap temp = Bitmap.createBitmap(res, 0, 0, res.getWidth(), res.getHeight(), matrix, true);
            Log.d("com.arcsoft", "check target Image:" + temp.getWidth() + "X" + temp.getHeight());

            if (!temp.equals(res)) {
                res.recycle();
            }
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void correctSIM() {
        // 检查sim卡是否发生变化
        SharedPreferences sp = getSharedPreferences("config",
                Context.MODE_PRIVATE);
        // 获取防盗保护的状态
        boolean protecting = sp.getBoolean("protecting", true);
        if (protecting) {
            // 得到绑定的sim卡串号
            String bindsim = sp.getString("sim", "");
            // 得到手机现在的sim卡串号
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            // 为了测试在手机序列号上dafa 已模拟SIM卡被更换的情况

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            String realsim = tm.getSimSerialNumber();
            if (bindsim.equals(realsim)) {
                Log.i("", "sim卡未发生变化，还是您的手机");
            } else {
                Log.i("", "SIM卡变化了");
                // 由于系统版本的原因，这里的发短信可能与其他手机版本不兼容
                String safenumber = sp.getString("safephone", "");
                if (!TextUtils.isEmpty(safenumber)) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(safenumber, null,
                            "你的亲友手机的SIM卡已经被更换！", null, null);
                }
            }
        }
    }

    //然后写一个基类Activity，在构造构造方法调用Application.addActivity(this);就可以实现。
    private static List<Activity> lists = new ArrayList<>();
    public static void addActivity(Activity activity) {
        lists.add(activity);
    }

    public static void clearActivity() {
        if (lists != null) {
            for (Activity activity : lists) {
                activity.finish();
            }

            lists.clear();
        }
    }

    //注意单位均为毫秒
    private double the_period_of_an_activity = 0;
    private long an_activity_is_created =0;
    private long an_activity_is_destroyed =0;
    private List<Double> mActivityTimeList = new ArrayList<Double>();

    //声明一个监听Activity们生命周期的接口
    private ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        /**
         * application下的每个Activity声明周期改变时，都会触发以下的函数。
         */
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            //如何区别参数中activity代表你写的哪个activity。
            if (activity.getClass() == MainActivity.class){
                Log.d(TAG, "MainActivityCreated.");
            }else if(activity.getClass()== VideoPlayer.class){
                Log.d(TAG, "VideoPlayerCreated.");
                an_activity_is_created = System.currentTimeMillis();
            }

        }

        @Override
        public void onActivityStarted(Activity activity) {}

        @Override
        public void onActivityResumed(Activity activity) {}

        @Override
        public void onActivityPaused(Activity activity) {}

        @Override
        public void onActivityStopped(Activity activity) {}

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.d(TAG, "onActivityDestroyed.");
            if (activity.getClass() == VideoPlayer.class){
                Log.d(TAG, "VideoPlayerDestroyed.");
                an_activity_is_destroyed = System.currentTimeMillis();
                the_period_of_an_activity = an_activity_is_destroyed - an_activity_is_created;
//                mActivityTimeList.add(resultThePeriod);
//                for(Double finResult : mActivityTimeList) {
//                    the_period_of_an_activity += finResult;
//                }
            }

        }
    };

    //提供 某一个activity运行的周期
    public double getThe_period_of_an_activity() {
        return the_period_of_an_activity;
    }

    @Override
    public void onTerminate() {
        //注销这个接口。
        unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
        super.onTerminate();
    }

}
