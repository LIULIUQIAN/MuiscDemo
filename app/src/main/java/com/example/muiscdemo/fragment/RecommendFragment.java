package com.example.muiscdemo.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muiscdemo.R;
import com.example.muiscdemo.adapter.RecommendAdapter;
import com.example.muiscdemo.api.Api;
import com.example.muiscdemo.domain.Song;
import com.example.muiscdemo.domain.playlist;
import com.example.muiscdemo.domain.response.ListResponse;
import com.example.muiscdemo.reactivex.HttpListener;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends BaseCommonFragment {


    private RecyclerView rv;
    private RecommendAdapter adapter;
    private GridLayoutManager layoutManager;


    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommend, container, false);
    }

    @Override
    protected void initViews() {
        super.initViews();
        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                int viewType = adapter.getItemViewType(position);

                if (viewType == adapter.TYPE_LIST){
                    return 1;
                }else {
                    return 3;
                }
            }
        });
        rv.setLayoutManager(layoutManager);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        adapter = new RecommendAdapter(getActivity());
        rv.setAdapter(adapter);

        fetchData();
    }

    private void fetchData() {

        Observable<ListResponse<playlist>> list = Api.getInstance().lists();
        final Observable<ListResponse<Song>> songs = Api.getInstance().songs();

        final ArrayList<Object> datas = new ArrayList<>();
        datas.add("推荐歌单");

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
                                        datas.add("推荐单曲");
                                        datas.addAll(data.getData());

                                        adapter.setData(datas);

                                    }
                                });
                    }
                });



    }
}
