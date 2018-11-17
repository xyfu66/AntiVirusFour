package com.jinyun.antivirusfour.welcomeView.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.jinyun.antivirusfour.homeMain.HomeMainActivity;
import com.jinyun.antivirusfour.welcomeView.entity.VersionEntity;


/**更新提醒工具类
 *
 */
 public class VersionUpdateUtils{
 private static final int MESSAGE_NET_EEOR=101;
 private static final int MESSAGE_IO_EEOR=102;
 private static final int MESSAGE_JSON_EEOR = 103;
 private static final int MESSAGE_SHOEW_DIALOG =104;
 protected static final int MESSAGE_ENTERHOME =105 ;


    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MESSAGE_IO_EEOR :
                    Toast.makeText(context, "IO 异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_JSON_EEOR :
                    Toast.makeText(context, "JSON 解析异常",  Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_NET_EEOR :
                    Toast.makeText(context, "网络异常",  Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_SHOEW_DIALOG:
                    showUpdateDialog(versionEntity);
                    break;
                case MESSAGE_ENTERHOME:
                    Intent intent = new Intent(context, HomeMainActivity.class);
                    context.startActivity(intent);
                    context.finish();
                    break;
            }
        };
    };

    /**
     * 本地版本号
     */
    private String mVersion;
    private Activity context;
    private ProgressDialog mProgressDialog;
    private VersionEntity versionEntity;
    public VersionUpdateUtils (String Version, Activity activity) {
        mVersion = Version;
        context = activity;
    }

/**
 *  获取服务器版本号
 */
 public void getCloudVersion () {
//     try {
//         HttpClient client = new DefaultHttpClient();
//        /*连接超时*/
//         HttpConnectionParams.setConnectionTimeout(client.getParams(), 5000);
//        /*请求超时*/
//         HttpConnectionParams.setSoTimeout(client.getParams(), 5000);
//         HttpGet httpGet = new HttpGet("http: //172.16.25.14 :8080/updateinfo.htm1");
//         HttpResponse execute = client.execute(httpGet);
//         if (execute, getStatusLine().getStatusCode() == 200){
//             //请求和响应都成功了
//             HttpEntity entity = execute.getEntity();
//             String result = EntityUtils.toString(entity, "gbk");
//             //创建jsonObject 对象
//             JSONObject jsonObject = new JSONObject(result);
//             versionEntity = new VersionEntity();
//             String code = jsonObject.getString("code");
//             versionEntity.versioncode = code;
//             String des = jsonObject.getString("des");
//             versionEntity.description = des;
//             String apkurl = jsonObject.getString("apkurl");
//             versionEntity.apkurl = apkurl;
//             if (!mVersion.equals(versionEntity.versioncode)) {
//                 //版本号不一致
//                 handler.sendEmptyMessage(MESSAGE_SHOEW_DIALOG);
//             }
//         }
//     } catch (ClientProtocolException e) {
//         handler.sendEmptyMessage(MESSAGE_NET_EEOR);
//         e.printStackTrace();
//     } catch (IOException e) {
//         handler.sendEmptyMessage(MESSAGE_IO_EEOR);
//         e.printStackTrace();
//     } catch (JSONException e) {
//         handler.sendEmptyMessage(MESSAGE_JSON_EEOR);
//         e.printStackTrace();
//     }
 }
    /**
    * 弹出更新提示对话框
    * @param versionEntity
    */
    private void showUpdateDialog (final VersionEntity versionEntity) {
//        //创建dialog
//        AlertDialog.Builder builder = new Builder(context);
//        builder.setTitle("检查到新版本: " + versionEntity.versioncode);//设置标题
//        builder.setMessage(versionEntity.description);
//        //根据服务器返回描述,设置升级描述信息
//        builder.setCancelable(false); //设置不能点击手机返回按钮隐藏对话框
//        builder.setIcon(R.drawable.icon);//设置对话框图标
//        //设置立即升级按钮点击事件
//        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                initProgressDialog();
//                downloadNewApk(versionEntity.apkur1);
//            }
//        });
//        // 设置暂不升级按钮点击事件
//        builder.setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                enterHome();
//            }
//        });
//        builder.show(); //对话框必须调用show 方法否则不显示
//    }

    /**
    初始化进度条对话框
    */
//    private void initProgressDialog(){
//        mProgressDialog = new ProgressDialog(context);
//        mProgressDialog.setMessage("准备下载...");
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        mProgressDialog.show();
//    }

    /**
     * 下载新版本
     * @param apkurl
     */
//        protected void downloadNewApk(String apkurl) {
//            DownLoadUtils downLoadUtils=new DownLoadUtils () ;
//            downLoadUtils.downapk (apkurl,"/mnt/sdcard/mobilesafe2.0.apk",new DownLoadUtils.MyCallBack(){
//                @Override
//                public void onSuccess (ResponseInfo<File> arg0) {
//                    //TODO Auto-generated method stub
//                    mProgressDialog.dismiss();
//                    MyUtils.installApk(context);
//                }
//                @Override
//                public void onLoadding(long total,long current,boolean isUploading) {
//                    //TODO Auto-generated method stub
//                    mProgressDialog.setMax((int) total);
//                    mProgressDialog.setMessage("正在下载...");
//                    mProgressDialog.setProgress((int) current);
//                }
//                @Override
//                public void onFailure (HttpException arg0, String arg1)
//                //TODO Auto-generated method stub
//                mProgressDialog.setMessage("下载失败");
//                mProgressDialog.dismiss();
//                enterHome();
//            }
//        });
     }

    private void enterHome () {
        handler.sendEmptyMessageDelayed(MESSAGE_ENTERHOME, 2000);
    }
}

