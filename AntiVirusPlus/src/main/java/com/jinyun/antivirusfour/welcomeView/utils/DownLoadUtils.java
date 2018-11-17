package com.jinyun.antivirusfour.welcomeView.utils;


/**
 * xUtil3 里面没有HttpUtils
 * 以后看找资料解决一下app更新问题
 */
public class DownLoadUtils {
    public void downapk(String url, String targerFile, final MyCallBack myCallBack){
//        //创建HttpUtils
//        HttpUtils httpUtils = new HttpUtils();
//        //调用HttpUtils 下载的方法下载指定文件
//        httpUtils.download(url,targerFile,new RequestCallBack<File> () {
//                @Override
//                public void onSuccess (ResponseInfo<File> argO) {
//                myCallBack.onSuccess(arg0);
//                }
//                @Override
//                public void onFailure(HttpException argO, String arg1){
//                    myCallBack.onFailure(arg0, arg1);
//                }
//                @Override
//                public void onLoading(long total,long current,boolean isUploading){
//                    super.onLoading(total,current,isUploading) ;
//                    myCallBack.onLoadding(total,current,isUploading) ;
//            }
//        });
    }
     /**
     * 接口，用于监听下载状态的接口
     */
     interface MyCallBack{
//        /**下载成功时调用*/
//        void onSuccess (ResponseInfo<File> arg0);
//       /**下载失败时调用*/
//       void onFailure (HttpException arg0,String arg1);
//       /**下载中调用*/
//        void onLoadding (long total,long current,boolean isUploading);

     }
}
