package com.example.muiscdemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.muiscdemo.R;

public class MusicPlayerActivity extends BaseTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
    }

    @Override
    protected void initViews() {
        super.initViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //状态栏颜色设置为透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            //去除半透明状态栏(如果有)
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：让内容显示到状态栏
            //SYSTEM_UI_FLAG_LAYOUT_STABLE：状态栏文字显示白色
            //SYSTEM_UI_FLAG_LIGHT_STATUS_BAR：状态栏文字显示黑色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
}
