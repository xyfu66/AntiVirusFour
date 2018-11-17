package com.jinyun.antivirusfour.advanced.utils;


import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yangyouwang on 2018/3/12.
 */

public class HttpUtil {

    String mUrl;
    Map<String,String> mParam;
    HttpRespone mHttpResponse;

    Handler myHandler = new Handler(Looper.getMainLooper());

    private final OkHttpClient client = new OkHttpClient();

    public interface HttpRespone{
        void onSuccess(Object object);
        void onFail(String error);
    }

    public HttpUtil(HttpRespone respone){
        mHttpResponse=respone;
    }

    public void sendPostHttp(String url, Map<String,String> param){
        sendHttp(url,param,true);
    }

    public void sendGetHttp(String url, Map<String,String> param){
        sendHttp(url,param,false);
    }

    private void sendHttp(String url, Map<String,String> param, boolean isPost){
        mUrl = url;
        mParam = param;
        run(isPost);
    }
    private void run(boolean isPost){
        //request请求创建
        Request request = createRequest(isPost);
        //创建请求队列
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(mHttpResponse!=null){
                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mHttpResponse.onFail("请求错误");
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                    if (mHttpResponse == null)return;
                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!response.isSuccessful()){
                                mHttpResponse.onFail("请求失败:"+response);
                            }else{
                                try {
                                    mHttpResponse.onSuccess(response.body().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    mHttpResponse.onFail("结果转换失败");
                                }
                            }
                        }
                    });
            }
        });
    }
    private Request createRequest(boolean isPost){
        Request request;
        if (isPost){
            MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder();
            requestBodyBuilder.setType(MultipartBody.FORM);
            //遍历map请求参数
            Iterator<Map.Entry<String,String>> iterator = mParam.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String,String> entry = iterator.next();
                requestBodyBuilder.addFormDataPart(entry.getKey(),entry.getValue());
            }
            request=new okhttp3.Request.Builder().url(mUrl).post(requestBodyBuilder.build()).build();
        }else{
            String urlStr = mUrl+"?"+MapParamToString(mParam);
            request = new Request.Builder().url(urlStr).build();
        }
        return request;
    }
    private String MapParamToString(Map<String,String> param){
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Map.Entry<String,String>> iterator = param.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String> entry = iterator.next();
            stringBuilder.append(entry.getKey()+"="+entry.getValue()+"&");
        }
        String str = stringBuilder.toString().substring(0,stringBuilder.length()-1);
        return  str;
    }
}