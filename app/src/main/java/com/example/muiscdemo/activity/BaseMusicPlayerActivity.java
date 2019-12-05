package com.example.muiscdemo.activity;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.muiscdemo.R;
import com.example.muiscdemo.domain.Song;
import com.example.muiscdemo.fragment.PlayListDialogFragment;
import com.example.muiscdemo.listener.OnMusicPlayerListener;
import com.example.muiscdemo.listener.PlayListListener;
import com.example.muiscdemo.manager.MusicPlayerManager;
import com.example.muiscdemo.manager.PlayListManager;
import com.example.muiscdemo.parser.LyricsParser;
import com.example.muiscdemo.parser.domain.Line;
import com.example.muiscdemo.parser.domain.Lyric;
import com.example.muiscdemo.service.MusicPlayerService;
import com.example.muiscdemo.util.ImageUtil;

import java.util.TreeMap;

public class BaseMusicPlayerActivity extends BaseTitleActivity implements OnMusicPlayerListener, PlayListListener, View.OnClickListener {

    protected LinearLayout ll_play_small_container;
    protected ImageView iv_icon_small_controller;
    protected ImageView iv_play_small_controller;
    protected ImageView iv_play_list_small_controller;
    protected ImageView iv_next_small_controller;
    protected TextView tv_title_small_controller;
    protected TextView tv_info_small_controller;
    protected ProgressBar pb_progress_small;

    protected PlayListManager playListManager;
    protected MusicPlayerManager musicPlayerManager;
    /**
     * 歌词
     */
    private Lyric lyric;

    /**
     * 每一行歌词
     */
    private TreeMap<Integer, Line> lyricsLines;

    @Override
    protected void initViews() {
        super.initViews();

        ll_play_small_container = findViewById(R.id.ll_play_small_container);
        iv_icon_small_controller = findViewById(R.id.iv_icon_small_controller);
        tv_title_small_controller = findViewById(R.id.tv_title_small_controller);
        tv_info_small_controller = findViewById(R.id.tv_info_small_controller);
        iv_play_small_controller = findViewById(R.id.iv_play_small_controller);
        iv_play_list_small_controller = findViewById(R.id.iv_play_list_small_controller);
        iv_next_small_controller = findViewById(R.id.iv_next_small_controller);
        pb_progress_small = findViewById(R.id.pb_progress_small);

    }

    @Override
    protected void initDatas() {
        super.initDatas();

        playListManager = MusicPlayerService.getPlayListManager(getApplicationContext());
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (playListManager.getPlayList() != null && playListManager.getPlayList().size() > 0) {
            ll_play_small_container.setVisibility(View.VISIBLE);
            Song song = playListManager.getPlayData();
            if (song != null) {
                setFirstData(song);
            }
        } else {
            ll_play_small_container.setVisibility(View.GONE);
        }
        playListManager.addPlayListListener(this);
        musicPlayerManager.addOnMusicPlayerListener(this);
    }

    private void setFirstData(Song song) {
        pb_progress_small.setMax((int) song.getDuration());
        pb_progress_small.setProgress(sp.getLastSongProgress());
        ImageUtil.show(getActivity(), iv_icon_small_controller, song.getBanner());
        tv_title_small_controller.setText(song.getTitle());
    }

    @Override
    protected void onStop() {
        super.onStop();
        musicPlayerManager.removeOnMusicPlayerListener(this);
        playListManager.removePlayListListener(this);
    }

    @Override
    protected void initListener() {
        super.initListener();
        ll_play_small_container.setOnClickListener(this);
        iv_play_small_controller.setOnClickListener(this);
        iv_play_list_small_controller.setOnClickListener(this);
        iv_next_small_controller.setOnClickListener(this);
    }

    @Override
    public void onProgress(long progress, long total) {
        pb_progress_small.setProgress((int) progress);

        if (lyricsLines != null) {
            int lineNumber = lyric.getLineNumber(progress);
            Line line = lyricsLines.get(lineNumber);
            tv_info_small_controller.setText(line.getLineLyrics());
        }
    }

    @Override
    public void onPaused(Song data) {
        iv_play_small_controller.setSelected(false);
    }

    @Override
    public void onPlaying(Song data) {
        iv_play_small_controller.setSelected(true);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer, Song data) {
        setFirstData(data);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onError(MediaPlayer mp, int what, int extra) {

    }

    @Override
    public void onDataReady(Song song) {

        LyricsParser parser = LyricsParser.parse(song.getLyric().getStyle(), song.getLyric().getContent());
        parser.parse();
        lyric = parser.getLyric();
        if (lyric != null) {
            lyricsLines = parser.getLyric().getLyrics();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_play_small_container:
                startActivity(MusicPlayerActivity.class);
                break;
            case R.id.iv_play_small_controller:
                playOrPause();
                break;
            case R.id.iv_play_list_small_controller:
                showPlayListDialog();
                break;
            case R.id.iv_next_small_controller:
                Song songNext = playListManager.next();
                playListManager.play(songNext);
                break;
        }
    }

    private void playOrPause() {
        if (musicPlayerManager.isPlaying()) {
            playListManager.pause();
        } else {
            playListManager.resume();
        }
    }

    private void showPlayListDialog() {

        if (playListManager.getPlayList()==null || playListManager.getPlayList().size() <1){
            return;
        }
        PlayListDialogFragment playListDialog = new PlayListDialogFragment();
        playListDialog.setCurrentSong(playListManager.getPlayData());
        playListDialog.setData(playListManager.getPlayList());

        playListDialog.setListener(new PlayListDialogFragment.OnPlayListDialogListener() {
            @Override
            public void onRemoveClick(int position) {
                Song currentSong = playListManager.getPlayList().get(position);
                playListManager.delete(currentSong);
                currentSong = playListManager.getPlayData();
                if (currentSong == null) {
                    playListDialog.dismiss();
                    ll_play_small_container.setVisibility(View.GONE);
                } else {
                    playListDialog.setCurrentSong(currentSong);
                    playListDialog.setData(playListManager.getPlayList());
                }
            }

            @Override
            public void onItemClick(int position) {

                playListManager.play(playListManager.getPlayList().get(position));
                playListDialog.setCurrentSong(playListManager.getPlayData());
            }
        });

        playListDialog.show(getSupportFragmentManager(), "PlayListDialogFragment");

    }
}
