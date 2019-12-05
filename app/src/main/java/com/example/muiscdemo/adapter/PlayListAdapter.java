package com.example.muiscdemo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.muiscdemo.R;
import com.example.muiscdemo.domain.Song;

import java.util.List;

public class PlayListAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {

    private Song currentSong;
    private Context context;

    public PlayListAdapter(int layoutResId,Context context) {
        super(layoutResId);
        this.context = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song item) {

        helper.setText(R.id.tv_title, String.format("%s - %s", item.getTitle(), item.getArtist().getNickname()));
        helper.addOnClickListener(R.id.iv_remove);

        if (item.equals(currentSong)){

            helper.setTextColor(R.id.tv_title,context.getResources().getColor(R.color.main_color));
        }else {
            helper.setTextColor(R.id.tv_title,context.getResources().getColor(R.color.text));
        }
    }

    public void setCurrentSong(Song currentSong) {
        this.currentSong = currentSong;
        notifyDataSetChanged();
    }


}
