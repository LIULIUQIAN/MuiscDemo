package com.example.muiscdemo.activity;

import com.example.muiscdemo.util.OrmUtil;
import com.example.muiscdemo.util.SharedPreferencesUtil;

public class BaseCommonActivity extends BaseActivity {

    protected SharedPreferencesUtil sp;
    protected OrmUtil orm;
//    private FloatingLayoutManager floatingLayoutManager;

    @Override
    protected void initViews() {
        super.initViews();
//        ButterKnife.bind(this);


        sp = SharedPreferencesUtil.getInstance(getApplicationContext());
        orm = OrmUtil.getInstance(getApplicationContext());
//        floatingLayoutManager = MusicPlayerService.getFloatingLayoutManager(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (!ServiceUtil.isBackgroundRunning(getApplicationContext())) {
//            //如果当前程序在前台，就尝试隐藏桌面歌词
//            floatingLayoutManager.tryHide();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (ServiceUtil.isBackgroundRunning(getApplicationContext())) {
//            //如果当前程序在后台，就显示桌面歌词
//            floatingLayoutManager.tryShow();
//        }
    }

}
