package com.example.muiscdemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.muiscdemo.R;
import com.example.muiscdemo.util.Consts;

public class BaseWebViewActivity extends BaseTitleActivity {

    private WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_web_view);
    }

    public static void start(Activity activity,String title, String url){
        Intent intent = new Intent(activity,BaseWebViewActivity.class);
        intent.putExtra(Consts.TITLE,title);
        intent.putExtra(Consts.URL,url);
        activity.startActivity(intent);
    }

    @Override
    protected void initViews() {
        super.initViews();
        wv = findViewById(R.id.wv);

        WebSettings webSettings = wv.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccessFromFileURLs(true);

        webSettings.setDomStorageEnabled(true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        String title = getIntent().getStringExtra(Consts.TITLE);
        String url = getIntent().getStringExtra(Consts.URL);

        setTitle(title);

        if (!TextUtils.isEmpty(url)) {
            wv.loadUrl("https://www.baidu.com");
        }  else {
            finish();
        }
    }
}
