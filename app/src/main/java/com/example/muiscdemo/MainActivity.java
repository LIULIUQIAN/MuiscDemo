package com.example.muiscdemo;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.muiscdemo.activity.BaseTitleActivity;
import com.example.muiscdemo.adapter.HomeAdapter;
import com.example.muiscdemo.fragment.MeFragment;
import com.example.muiscdemo.fragment.MusicFragment;
import com.example.muiscdemo.fragment.VideoFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseTitleActivity implements View.OnClickListener {

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

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer_layout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void initDatas() {
        super.initDatas();

        List<Fragment> list = new ArrayList<>();
        list.add(new MeFragment());
        list.add(new MusicFragment());
        list.add(new VideoFragment());

        adapter = new HomeAdapter(this,getSupportFragmentManager(),0);
        adapter.setDatas(list);
        vp.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_music.setOnClickListener(this);
        iv_recommend.setOnClickListener(this);
        iv_video.setOnClickListener(this);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                iv_music.setImageResource(R.drawable.ic_play);
                iv_recommend.setImageResource(R.drawable.ic_music);
                iv_video.setImageResource(R.drawable.ic_video);
                if (position == 0){
                    iv_music.setImageResource(R.drawable.ic_play_selected);
                }else if(position == 1){
                    iv_recommend.setImageResource(R.drawable.ic_music_selected);
                }else if(position ==2){
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

        switch (v.getId()){
            case R.id.iv_music:
                vp.setCurrentItem(0);
                break;
            case R.id.iv_recommend:
                vp.setCurrentItem(1);
                break;
            case R.id.iv_video:
                vp.setCurrentItem(2);
                break;
        }

    }
}
