package com.jinyun.antivirusfour.health.cook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 *
 * 闪屏页
 */

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Observable.timer(1, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
//            @Override
//            public void call(Long aLong) {
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
//            }
//        });
    }
}
