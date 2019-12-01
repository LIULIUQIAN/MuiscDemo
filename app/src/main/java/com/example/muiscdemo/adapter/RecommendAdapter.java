package com.example.muiscdemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.muiscdemo.R;
import com.example.muiscdemo.domain.RecommendedTitle;
import com.example.muiscdemo.domain.Song;
import com.example.muiscdemo.domain.playlist;
import com.example.muiscdemo.util.ImageUtil;


import java.util.List;


public class RecommendAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity,RecommendAdapter.ViewHolder> {

    public static final int TYPE_TITLE = 3;
    public static final int TYPE_LIST = 1;
    public static final int TYPE_SONG = 2;
    private Context context;

    public RecommendAdapter(List data,Context context) {
        super(data);
        this.context = context;
        addItemType(TYPE_TITLE, R.layout.item_title);
        addItemType(TYPE_LIST, R.layout.item_list);
        addItemType(TYPE_SONG, R.layout.item_song);
    }

    @Override
    protected void convert(@NonNull ViewHolder helper, MultiItemEntity item) {

        switch (helper.getItemViewType()){
            case TYPE_TITLE:
                RecommendedTitle titleModel = (RecommendedTitle) item;
                helper.setText(R.id.tv_title,titleModel.getTitle());
                break;
            case TYPE_LIST:
                playlist md = (playlist) item;
                helper.setText(R.id.tv_count,String.valueOf(md.getClicks_count()));
                helper.setText(R.id.tv_title,md.getTitle());
                helper.setImageUrl(md.getBanner());
                break;
            case TYPE_SONG:
                Song song = (Song) item;
                helper.setText(R.id.tv_title,song.getTitle());
                helper.setText(R.id.tv_nickname,song.getArtist().getNickname());
                helper.setText(R.id.tv_comment_count,String.valueOf(song.getComments_count()));
                helper.setImageUrl(song.getBanner());
                break;
        }

    }

    class ViewHolder extends BaseViewHolder {

        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_banner);
        }

        public void setImageUrl(String url){
            ImageUtil.show((Activity) context, imageView, url);
        }

    }

}
