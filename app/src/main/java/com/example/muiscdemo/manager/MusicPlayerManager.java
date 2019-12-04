package com.example.muiscdemo.manager;

import com.example.muiscdemo.domain.Song;
import com.example.muiscdemo.listener.OnMusicPlayerListener;

public interface MusicPlayerManager {

    /**
     * 播放
     * @param data
     */
    void play(String uri, Song data);

    /**
     * 是否在播放
     * @return
     */
    boolean isPlaying();

    /**
     * 暂停
     */
    void pause();

    /**
     * 继续播放
     */
    void resume();

    /**
     * 移动到指定位置播放
     * @param progress 播放进度
     */
    void seekTo(int progress);

    /**
     * 添加播放状态监听
     * @param listener
     */
    void addOnMusicPlayerListener(OnMusicPlayerListener listener);

    /**
     * 移除播放状态监听
     * @param listener
     */
    void removeOnMusicPlayerListener(OnMusicPlayerListener listener);

    void destroy();

    /**
     * 设置循环模式
     * @param looping
     */
    void setLooping(boolean looping);
}
