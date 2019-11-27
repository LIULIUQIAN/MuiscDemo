package com.example.muiscdemo.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.muiscdemo.R;
import com.example.muiscdemo.adapter.UserDetailAdapter;
import com.example.muiscdemo.api.Api;
import com.example.muiscdemo.domain.User;
import com.example.muiscdemo.domain.response.DetailResponse;
import com.example.muiscdemo.fragment.AboutUserFragment;
import com.example.muiscdemo.fragment.FeedFragment;
import com.example.muiscdemo.fragment.UserDetailMusicFragment;
import com.example.muiscdemo.reactivex.HttpListener;
import com.example.muiscdemo.util.Consts;
import com.google.android.material.appbar.AppBarLayout;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserDetailActivity extends BaseTitleActivity {

    @BindView(R.id.tabs)
    MagicIndicator tabs;

    @BindView(R.id.vp)
    ViewPager vp;

    @BindView(R.id.abl)
    AppBarLayout abl;

    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;

    @BindView(R.id.tv_nickname)
    TextView tv_nickname;

    @BindView(R.id.tv_info)
    TextView tv_info;

    @BindView(R.id.bt_follow)
    Button bt_follow;

    @BindView(R.id.bt_send_message)
    Button bt_send_message;
    private String nickname;
    private String id;
    private User user;

    private UserDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
    }

    @Override
    protected void initDatas() {
        super.initDatas();

        nickname = getIntent().getStringExtra(Consts.NICKNAME);
        id = getIntent().getStringExtra(Consts.ID);
        if (StringUtils.isNotEmpty(id)) {
            fetchDataById(id);
        } else if (StringUtils.isNotEmpty(nickname)) {
            fetchDataByNickname(nickname);
        } else {
            finish();
        }
    }

    private void fetchDataByNickname(String nickname) {
        Api.getInstance().userDetailByNickname(nickname).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<DetailResponse<User>>(getActivity()) {
                    @Override
                    public void onSucceeded(DetailResponse<User> data) {
                        super.onSucceeded(data);
                        next(data.getData());
                    }
                });
    }

    private void fetchDataById(String id) {
        Api.getInstance().userDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<DetailResponse<User>>(getActivity()) {
                    @Override
                    public void onSucceeded(DetailResponse<User> data) {
                        super.onSucceeded(data);
                        next(data.getData());
                    }
                });
    }

    public void next(User user) {
        this.user = user;

        RequestOptions options = new RequestOptions();
        options.circleCrop();
        Glide.with(this)
                .asBitmap()
                .load("http://b-ssl.duitang.com/uploads/item/201205/03/20120503151606_fuXRt.png")
                .into(new CustomTarget<Bitmap>(){

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        iv_avatar.setImageBitmap(resource);
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {
                                Palette.Swatch swatch = palette.getVibrantSwatch();
                                if (swatch != null){
                                    int rgb = swatch.getRgb();
                                    abl.setBackgroundColor(rgb);


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        Window window = getWindow();
                                        window.setStatusBarColor(rgb);
                                        window.setNavigationBarColor(rgb);
                                    }

                                }
                            }
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

        tv_nickname.setText(user.getNickname());
        tv_info.setText(getResources().getString(R.string.user_detail_count_info,user.getFollowings_count(),user.getFollowers_count()));

        showFollowStatus();
        setupUI();
    }

    private void setupUI() {

        List<Fragment> datas = new ArrayList<>();
        datas.add(new UserDetailMusicFragment());
        datas.add(new FeedFragment());
        datas.add(new AboutUserFragment());

        adapter = new UserDetailAdapter(this,getSupportFragmentManager(),0);
        adapter.setDatas(datas);
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(3);

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return datas.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(getResources().getColor(R.color.text_white));
                colorTransitionPagerTitleView.setSelectedColor(Color.WHITE);
                colorTransitionPagerTitleView.setText(adapter.getPageTitle(index));
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        vp.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setColors(Color.WHITE);
                return indicator;
            }
        });
        commonNavigator.setAdjustMode(true);
        tabs.setNavigator(commonNavigator);
        ViewPagerHelper.bind(tabs,vp);
    }

    private void showFollowStatus() {
        if (user.getId().equals(sp.getUserId())){
            bt_follow.setVisibility(View.GONE);
            bt_send_message.setVisibility(View.GONE);
        }else {
            bt_follow.setVisibility(View.VISIBLE);
            if (user.isFollowing()){
                bt_follow.setText("取消关注");
                bt_send_message.setVisibility(View.VISIBLE);
            }else {
                bt_follow.setText("关注");
                bt_send_message.setVisibility(View.GONE);
            }
        }
    }


}
