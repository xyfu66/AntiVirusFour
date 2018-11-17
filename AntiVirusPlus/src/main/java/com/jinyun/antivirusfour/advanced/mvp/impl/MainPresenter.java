package com.jinyun.antivirusfour.advanced.mvp.impl;

import com.google.gson.Gson;
import com.jinyun.antivirusfour.advanced.model.Phone;
import com.jinyun.antivirusfour.advanced.mvp.MvpMainView;
import com.jinyun.antivirusfour.advanced.utils.HttpUtil;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainPresenter extends BasePresenter{

    MvpMainView mvpMainView;
    String mUrl = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
    Phone mPhone;

    public MainPresenter(MvpMainView mainView){
        mvpMainView=mainView;
    }

    public Phone getPhoneInfo(){
        return mPhone;
    }

    public void sarchPhoneInfo(String phone){
        if(phone.length()!=11){
            mvpMainView.showToast("请输入正确的手机号");
            return;
        }
        mvpMainView.showLoading();
        sendHttp(phone);
    }

    private void sendHttp(String phone){
        final Map<String,String> map = new HashMap<String,String>();
        map.put("tel",phone);
        HttpUtil httpUtil = new HttpUtil(new HttpUtil.HttpRespone() {
            @Override
            public void onSuccess(Object object) {
                String json = object.toString();
                int index = json.indexOf("{");
                json=json.substring(index,json.length());
                //JSONOBJECT
                mPhone = parseModelWithOrgJson(json);
                //GSON
                mPhone = parseModelWithGson(json);
                //FastJson
                mPhone = parseModelWithFastJson(json);

                mvpMainView.hidenLoading();
                mvpMainView.updateView();
            }

            @Override
            public void onFail(String error) {
                mvpMainView.showToast(error);
                mvpMainView.hidenLoading();
            }
        });
        httpUtil.sendGetHttp(mUrl,map);
    }
    private Phone parseModelWithOrgJson(String json){
        Phone phone=new Phone();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String value = jsonObject.getString("telString");
            phone.setTelString(value);

            value = jsonObject.getString("province");
            phone.setProvince(value);

            value = jsonObject.getString("catName");
            phone.setCatName(value);

            value = jsonObject.getString("carrier");
            phone.setCarrier(value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return phone;
    }
    private Phone parseModelWithGson(String json){
        Gson gson = new Gson();
        Phone phone = gson.fromJson(json,Phone.class);
        return phone;
    }
    private Phone parseModelWithFastJson(String json){
        Phone phone = com.alibaba.fastjson.JSONObject.parseObject(json,Phone.class);
        return phone;
    }
}
