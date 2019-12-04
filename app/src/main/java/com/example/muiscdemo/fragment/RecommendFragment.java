package com.example.muiscdemo.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.example.muiscdemo.R;
import com.example.muiscdemo.activity.BaseWebViewActivity;
import com.example.muiscdemo.activity.ListDetailActivity;
import com.example.muiscdemo.adapter.RecommendAdapter;
import com.example.muiscdemo.api.Api;
import com.example.muiscdemo.domain.Advertisement;
import com.example.muiscdemo.domain.RecommendedTitle;
import com.example.muiscdemo.domain.Song;
import com.example.muiscdemo.domain.playlist;
import com.example.muiscdemo.domain.response.ListResponse;
import com.example.muiscdemo.reactivex.HttpListener;
import com.example.muiscdemo.util.ImageUtil;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends BaseCommonFragment implements OnBannerListener {

    private RecyclerView rv;
    private Banner banner;
    private RecommendAdapter mAdapter;
    private GridLayoutManager layoutManager;
    private ArrayList<MultiItemEntity> datas = new ArrayList<>();
    private List<Advertisement> bannerData;


    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommend, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    @Override
    protected void initViews() {
        super.initViews();
        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(getActivity(), 3);
        rv.setLayoutManager(layoutManager);

        mAdapter = new RecommendAdapter(new ArrayList(), getContext());
        rv.setAdapter(mAdapter);
        mAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                int viewType = datas.get(position).getItemType();

                if (viewType == mAdapter.TYPE_TITLE || viewType == mAdapter.TYPE_SONG) {
                    return 3;
                }
                return 1;
            }
        });

        mAdapter.setHeaderView(createHeaderView());

        rv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                int viewType = datas.get(position).getItemType();
                String id = "";
                if (viewType == mAdapter.TYPE_SONG) {
                    Song song = (Song) datas.get(position);
                    id = song.getId();
                } else if (viewType == mAdapter.TYPE_LIST) {
                    playlist md = (playlist) datas.get(position);
                    id = md.getId();
                }
                startActivityExtraId(ListDetailActivity.class, id);
            }
        });
    }

    private View createHeaderView() {
        View headView = getLayoutInflater().inflate(R.layout.header_music_recommend, (ViewGroup) rv.getParent(), false);
        banner = headView.findViewById(R.id.banner);
        banner.setOnBannerListener(this);
        return headView;
    }

    @Override
    protected void initDatas() {
        super.initDatas();

        fetchData();
        banner.setImageLoader(new GlideImageLoader());
        fetchBannerData();
    }

    private void fetchData() {

        Observable<ListResponse<playlist>> list = Api.getInstance().lists();
        final Observable<ListResponse<Song>> songs = Api.getInstance().songs();

        datas.add(new RecommendedTitle("推荐歌单"));

        list.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<ListResponse<playlist>>(getMainActivity()) {

                    @Override
                    public void onSucceeded(ListResponse<playlist> data) {
                        super.onSucceeded(data);
                        datas.addAll(data.getData());

                        songs.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new HttpListener<ListResponse<Song>>(getMainActivity()) {
                                    @Override
                                    public void onSucceeded(ListResponse<Song> data) {
                                        super.onSucceeded(data);
                                        datas.add(new RecommendedTitle("推荐单曲"));

                                        datas.addAll(data.getData());

                                        mAdapter.setNewData(datas);

                                    }
                                });
                    }
                });


    }

    private void fetchBannerData() {

        Api.getInstance()
                .advertisements()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<ListResponse<Advertisement>>(getMainActivity()) {
                    @Override
                    public void onSucceeded(ListResponse<Advertisement> data) {
                        super.onSucceeded(data);
                        bannerData = data.getData();
                        banner.setImages(bannerData);
                        banner.start();
                    }
                });
    }

    @Override
    public void OnBannerClick(int position) {

        Advertisement advertisement = bannerData.get(position);
        BaseWebViewActivity.start(getMainActivity(), "活动详情", advertisement.getUri());

    }

    public class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {

            Advertisement banner = (Advertisement) path;
            ImageUtil.show(getMainActivity(), imageView, banner.getBanner());
        }
    }
}
