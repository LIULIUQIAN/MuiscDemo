package com.example.muiscdemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.muiscdemo.R;
import com.example.muiscdemo.domain.event.LogoutSuccessEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;

public class SettingsActivity extends BaseTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @OnClick(R.id.bt_logout)
    public void onLogout(){
        sp.logout();

        EventBus.getDefault().post(new LogoutSuccessEvent());
        finish();

    }
}
