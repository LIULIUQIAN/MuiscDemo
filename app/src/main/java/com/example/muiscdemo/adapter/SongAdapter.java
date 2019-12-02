package com.example.muiscdemo.adapter;

import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.muiscdemo.R;
import com.example.muiscdemo.domain.Song;
import com.example.muiscdemo.fragment.SongMoreDialogFragment;

import java.util.List;

public class SongAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {


    private final FragmentManager fragmentManager;

    public SongAdapter(@Nullable List<Song> data,FragmentManager fragmentManager) {
        super(R.layout.item_song_list_detail, data);
        this.fragmentManager = fragmentManager;
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song item) {

        helper.setText(R.id.tv_position, String.valueOf(helper.getLayoutPosition() + 1));
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_info, item.getArtist().getNickname() + " - " + item.getAlbum().getTitle());
        helper.addOnClickListener(R.id.iv_more);
    }
}
