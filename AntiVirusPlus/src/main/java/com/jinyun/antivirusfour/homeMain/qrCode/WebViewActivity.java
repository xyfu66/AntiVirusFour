package com.jinyun.antivirusfour.homeMain.qrCode;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.jinyun.antivirusfour.R;

/**
 * WebView
 */

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    public static final String RESULT = "result_data";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initViews();
    }
    private void initViews(){
        webView = (WebView) findViewById(R.id.webView);
        Intent intent = getIntent();
        String result = intent.getStringExtra(RESULT);
        webView.loadUrl(result);
    }
}
