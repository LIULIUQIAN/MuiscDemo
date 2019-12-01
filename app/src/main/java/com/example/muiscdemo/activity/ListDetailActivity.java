package com.example.muiscdemo.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.muiscdemo.R;
import com.example.muiscdemo.adapter.SongAdapter;
import com.example.muiscdemo.api.Api;
import com.example.muiscdemo.domain.playlist;
import com.example.muiscdemo.domain.response.DetailResponse;
import com.example.muiscdemo.reactivex.HttpListener;
import com.example.muiscdemo.util.Consts;
import com.example.muiscdemo.util.ImageUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ListDetailActivity extends BaseTitleActivity {

    private RecyclerView rv;
    private LinearLayout header_container;
    private Button bt_collection;
    private ImageView iv_icon;
    private TextView tv_title;
    private TextView tv_nickname;
    private TextView tv_count;
    private SongAdapter adapter;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail);
    }

    @Override
    protected void initViews() {
        super.initViews();
        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);

        adapter = new SongAdapter(new ArrayList<>());
        rv.setAdapter(adapter);

        createHeaderView();
    }

    private void createHeaderView() {
        View headerView = getLayoutInflater().inflate(R.layout.header_song_detail, (ViewGroup) rv.getParent(),false);
        header_container = headerView.findViewById(R.id.header_container);
        bt_collection = headerView.findViewById(R.id.bt_collection);
        iv_icon = headerView.findViewById(R.id.iv_icon);
        tv_title = headerView.findViewById(R.id.tv_title);
        tv_nickname = headerView.findViewById(R.id.tv_nickname);
        tv_count = headerView.findViewById(R.id.tvm_count);
        adapter.setHeaderView(headerView);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        id = getIntent().getStringExtra(Consts.ID);


        fetchData();
    }

    private void fetchData() {

        Api.getInstance().listDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<DetailResponse<playlist>>(getActivity()){
                    @Override
                    public void onSucceeded(DetailResponse<playlist> data) {
                        super.onSucceeded(data);
                        next(data.getData());
                    }
                });
    }

    private void next(playlist data) {
        adapter.setNewData(data.getSongs());

        RequestBuilder<Bitmap> bitmapRequestBuilder = null;
        if (StringUtils.isBlank(data.getBanner())) {
            bitmapRequestBuilder = Glide.with(this).asBitmap().load(R.drawable.cd_bg);
        } else {
            bitmapRequestBuilder = Glide.with(this).asBitmap().load(ImageUtil.getImageURI(data.getBanner()));
        }

        bitmapRequestBuilder.into(new CustomTarget<Bitmap>(){

            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                iv_icon.setImageBitmap(resource);
                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@Nullable Palette palette) {
                        Palette.Swatch swatch = palette.getVibrantSwatch();
                        if (swatch != null){
                            int rgb = swatch.getRgb();
                            header_container.setBackgroundColor(rgb);
                            toolbar.setBackgroundColor(rgb);

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

        tv_title.setText(data.getTitle());
        tv_nickname.setText(data.getUser().getNickname());
        tv_count.setText("共"+data.getSongs().size()+"首");

    }
}
