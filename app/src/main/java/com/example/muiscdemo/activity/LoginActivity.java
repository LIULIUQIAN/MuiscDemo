package com.example.muiscdemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.muiscdemo.MainActivity;
import com.example.muiscdemo.R;
import com.example.muiscdemo.domain.event.LoginSuccessEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.OnClick;

public class LoginActivity extends BaseCommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.bt_login)
    public void onLogin() {
        startActivity(LoginPhoneActivity.class);
    }

    @OnClick(R.id.bt_register)
    public void onRegister() {
        startActivity(RegisterActivity.class);
    }

    @OnClick(R.id.tv_enter)
    public void onEnter() {
        startActivityAfterFinishThis(MainActivity.class);
    }

    @OnClick(R.id.iv_login_qq)
    public void onLoginQQ() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginSuccessEvent(LoginSuccessEvent event){
        finish();
    }
}
