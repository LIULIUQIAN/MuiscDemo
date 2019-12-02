package com.example.muiscdemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.palette.graphics.Palette;

import com.example.muiscdemo.R;
import com.example.muiscdemo.domain.Song;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SongMoreDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private OnMoreListener onMoreListener;

    private Song song;
    private TextView tv_song;
    private TextView tv_album;
    private TextView tv_artist;
    private LinearLayout ll_collection;
    private LinearLayout ll_download;
    private LinearLayout ll_delete;
    private boolean isShowDelete;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_song_more, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_song = view.findViewById(R.id.tv_song);
        ll_collection = view.findViewById(R.id.ll_collection);
        ll_download = view.findViewById(R.id.ll_download);
        tv_album = view.findViewById(R.id.tv_album);
        tv_artist = view.findViewById(R.id.tv_artist);
        ll_delete = view.findViewById(R.id.ll_delete);

        if (isShowDelete) {
            ll_delete.setVisibility(View.VISIBLE);
        }

        initData();
        initListener();

    }

    private void initListener() {
        ll_collection.setOnClickListener(this);
        ll_download.setOnClickListener(this);
        ll_delete.setOnClickListener(this);
    }

    private void initData() {
        tv_song.setText(getContext().getResources().getString(R.string.song_detail, song.getTitle()));
        tv_album.setText(getContext().getResources().getString(R.string.album, song.getAlbum().getTitle()));
        tv_artist.setText(getContext().getResources().getString(R.string.artist, song.getArtist().getNickname()));
    }

    public static void show(FragmentManager fragmentManager, Song song, OnMoreListener listener) {
        SongMoreDialogFragment.show(fragmentManager, song, false, listener);
    }

    public static void show(FragmentManager fragmentManager, Song song, boolean isShowDelete, OnMoreListener listener) {
        SongMoreDialogFragment fragment = new SongMoreDialogFragment();
        fragment.setOnMoreListener(listener);
        fragment.setSong(song);
        fragment.setShowDelete(isShowDelete);
        fragment.show(fragmentManager, "SongMoreDialogFragment");
    }

    public void setOnMoreListener(OnMoreListener listener) {
        this.onMoreListener = listener;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void setShowDelete(boolean showDelete) {
        isShowDelete = showDelete;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_collection:
                this.dismiss();
                if (onMoreListener != null) {
                    onMoreListener.onCollectionClick(song);
                }
                break;
            case R.id.ll_download:
                this.dismiss();
                if (onMoreListener != null) {
                    onMoreListener.onDownloadClick(song);
                }
                break;
            case R.id.ll_delete:
                this.dismiss();
                if (onMoreListener != null) {
                    onMoreListener.onDeleteClick(song);
                }
                break;
        }
    }

    public interface OnMoreListener {

        void onCollectionClick(Song song);

        void onDownloadClick(Song song);

        void onDeleteClick(Song song);
    }
}
