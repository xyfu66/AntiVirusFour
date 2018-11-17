package com.keyboard3;


public interface RulerCallback {
    //选取刻度变化的时候回调
    void onScaleChanging(float scale);
    //选取刻度变化完成的时候回调
    void afterScaleChanged(float scale);
}
