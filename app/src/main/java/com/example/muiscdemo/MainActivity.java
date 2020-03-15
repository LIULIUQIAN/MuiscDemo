package com.example.muiscdemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.muiscdemo.activity.BaseMusicPlayerActivity;
import com.example.muiscdemo.activity.BaseTitleActivity;
import com.example.muiscdemo.activity.LoginActivity;
import com.example.muiscdemo.activity.SettingsActivity;
import com.example.muiscdemo.activity.UserDetailActivity;
import com.example.muiscdemo.adapter.HomeAdapter;
import com.example.muiscdemo.api.Api;
import com.example.muiscdemo.domain.Session;
import com.example.muiscdemo.domain.User;
import com.example.muiscdemo.domain.event.LoginSuccessEvent;
import com.example.muiscdemo.domain.event.LogoutSuccessEvent;
import com.example.muiscdemo.domain.response.DetailResponse;
import com.example.muiscdemo.fragment.MeFragment;
import com.example.muiscdemo.fragment.MusicFragment;
import com.example.muiscdemo.fragment.VideoFragment;
import com.example.muiscdemo.reactivex.HttpListener;
import com.example.muiscdemo.util.ImageUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseMusicPlayerActivity implements View.OnClickListener {

    private DrawerLayout drawer_layout;
    ImageView iv_avatar;
    TextView tv_nickname;
    TextView tv_description;
    private ViewPager vp;
    private HomeAdapter adapter;
    private ImageView iv_music;
    private ImageView iv_recommend;
    private ImageView iv_video;
    private LinearLayout ll_settings;
    private LinearLayout ll_my_friend;
    private LinearLayout ll_message_container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

        drawer_layout = findViewById(R.id.drawer_layout);

        iv_avatar = findViewById(R.id.iv_avatar);
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_description = findViewById(R.id.tv_description);

        iv_music = findViewById(R.id.iv_music);
        iv_recommend = findViewById(R.id.iv_recommend);
        iv_video = findViewById(R.id.iv_video);

        ll_settings = findViewById(R.id.ll_settings);
//        ll_my_friend = findViewById(R.id.ll_my_friend);
//        ll_message_container = findViewById(R.id.ll_message_container);

        vp = findViewById(R.id.vp);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void initDatas() {
        super.initDatas();

        EventBus.getDefault().register(this);

        List<Fragment> list = new ArrayList<>();
        list.add(new MeFragment());
        list.add(new MusicFragment());
        list.add(new VideoFragment());

        adapter = new HomeAdapter(this, getSupportFragmentManager(), 0);
        adapter.setDatas(list);
        vp.setAdapter(adapter);

        getUserInfo();
    }

    private void getUserInfo() {

        if (sp.isLogin()) {
            Api.getInstance().userDetail(sp.getUserId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new HttpListener<DetailResponse<User>>(getActivity()) {
                        @Override
                        public void onSucceeded(DetailResponse<User> data) {
                            super.onSucceeded(data);
                            ImageUtil.showCircle(MainActivity.this, iv_avatar, data.getData().getAvatar());
                            tv_nickname.setText(data.getData().getNickname());
                            tv_description.setText(data.getData().getDescription());

                        }
                    });
        } else {
            iv_avatar.setImageResource(R.drawable.default_avatar);
            tv_nickname.setText("请登录");
            tv_description.setText("");

        }

    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_music.setOnClickListener(this);
        iv_recommend.setOnClickListener(this);
        iv_video.setOnClickListener(this);
        ll_settings.setOnClickListener(this);
        iv_avatar.setOnClickListener(this);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                iv_music.setImageResource(R.drawable.ic_play);
                iv_recommend.setImageResource(R.drawable.ic_music);
                iv_video.setImageResource(R.drawable.ic_video);
                if (position == 0) {
                    iv_music.setImageResource(R.drawable.ic_play_selected);
                } else if (position == 1) {
                    iv_recommend.setImageResource(R.drawable.ic_music_selected);
                } else if (position == 2) {
                    iv_video.setImageResource(R.drawable.ic_video_selected);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp.setCurrentItem(1);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_music:
                vp.setCurrentItem(0);
                break;
            case R.id.iv_recommend:
                vp.setCurrentItem(1);
                break;
            case R.id.iv_video:
                vp.setCurrentItem(2);
                break;
            case R.id.ll_settings:
                closeDrawer();
                startActivity(SettingsActivity.class);
                break;
            case R.id.iv_avatar:
                avatarClick();
                break;
            default:
                super.onClick(v);
                break;

        }

    }

    @SuppressLint("WrongConstant")
    private void closeDrawer() {
        drawer_layout.closeDrawer(Gravity.START);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void logoutSuccessEvent(LogoutSuccessEvent event) {
        getUserInfo();
    }

    private void avatarClick() {
        closeDrawer();
        if (sp.isLogin()) {
            startActivityExtraId(UserDetailActivity.class, sp.getUserId());
        } else {
            startActivity(LoginActivity.class);
        }
    }

}
