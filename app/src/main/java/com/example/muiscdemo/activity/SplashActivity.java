package com.example.muiscdemo.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.muiscdemo.MainActivity;
import com.example.muiscdemo.R;
import com.example.muiscdemo.util.PackageUtil;

public class SplashActivity extends BaseCommonActivity {

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            next();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    @Override
    protected void initDatas() {
        super.initDatas();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(-1);
            }
        },2000);
    }

    private void next(){

        if (isShowGuide()){
            startActivityAfterFinishThis(GuideActivity.class);
            sp.putBoolean(String.valueOf(PackageUtil.getVersionCode(getApplicationContext())),false);
        }else if(sp.isLogin()){
            startActivityAfterFinishThis(MainActivity.class);
        }else {
            startActivityAfterFinishThis(LoginActivity.class);
        }

    }

    /*根据当前版本号判断是否需要引导页*/
    private boolean isShowGuide(){
       return sp.getBoolean(String.valueOf(PackageUtil.getVersionCode(getApplicationContext())),true);
    }
}
