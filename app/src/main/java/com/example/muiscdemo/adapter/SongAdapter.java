package com.example.muiscdemo.adapter;

import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.muiscdemo.R;
import com.example.muiscdemo.domain.Song;

import java.util.List;

public class SongAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {


    public SongAdapter(@Nullable List<Song> data) {
        super(R.layout.item_song_list_detail, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song item) {

        helper.setText(R.id.tv_position, String.valueOf(helper.getLayoutPosition() + 1));
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_info, item.getArtist().getNickname() + " - " + item.getAlbum().getTitle());
        helper.addOnClickListener(R.id.iv_more);
    }
}
