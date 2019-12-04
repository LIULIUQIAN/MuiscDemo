package com.example.muiscdemo.listener;

import com.example.muiscdemo.domain.Song;

public interface PlayListListener {
    /**
     * 数据准备好了(歌词)，后面可能会用到其他数据
     */
    void onDataReady(Song song);

}
