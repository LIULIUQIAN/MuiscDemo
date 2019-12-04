package com.example.muiscdemo.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.muiscdemo.R;
import com.example.muiscdemo.adapter.MusicPlayerAdapter;
import com.example.muiscdemo.domain.Lyric;
import com.example.muiscdemo.domain.Song;
import com.example.muiscdemo.listener.OnMusicPlayerListener;
import com.example.muiscdemo.listener.PlayListListener;
import com.example.muiscdemo.manager.MusicPlayerManager;
import com.example.muiscdemo.manager.PlayListManager;
import com.example.muiscdemo.manager.impl.PlayListManagerImpl;
import com.example.muiscdemo.parser.LyricsParser;
import com.example.muiscdemo.service.MusicPlayerService;
import com.example.muiscdemo.util.AlbumDrawableUtil;
import com.example.muiscdemo.util.ImageUtil;
import com.example.muiscdemo.util.TimeUtil;
import com.example.muiscdemo.view.ListLyricView;
import com.example.muiscdemo.view.RecordThumbView;
import com.example.muiscdemo.view.RecordView;

import org.apache.commons.lang3.StringUtils;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class MusicPlayerActivity extends BaseTitleActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, OnMusicPlayerListener, PlayListListener, ViewPager.OnPageChangeListener {

    private ImageView iv_loop_model;
    private ImageView iv_album_bg;
    private ImageView iv_play_control;
    private ImageView iv_play_list;
    private ImageView iv_previous;
    private ImageView iv_next;
    private TextView tv_start_time;
    private TextView tv_end_time;
    private SeekBar sb_progress;
    private RecordThumbView rt;
    private ImageView iv_download;
    private RecordView rv;
    private LinearLayout lyric_container;
    private RelativeLayout rl_player_container;
    private SeekBar sb_volume;
    private ListLyricView lv;
    private ViewPager vp;

    private MusicPlayerManager musicPlayerManager;
    private AudioManager audioManager;


    private MusicPlayerAdapter adapter;
    private PlayListManager playListManager;
    private Song currentSong;

    private LyricsParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
    }

    @Override
    protected void onResume() {
        super.onResume();
        musicPlayerManager.addOnMusicPlayerListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        musicPlayerManager.removeOnMusicPlayerListener(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //状态栏颜色设置为透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            //去除半透明状态栏(如果有)
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：让内容显示到状态栏
            //SYSTEM_UI_FLAG_LAYOUT_STABLE：状态栏文字显示白色
            //SYSTEM_UI_FLAG_LIGHT_STATUS_BAR：状态栏文字显示黑色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        iv_download = findViewById(R.id.iv_download);
        iv_album_bg = findViewById(R.id.iv_album_bg);
        iv_loop_model = findViewById(R.id.iv_loop_model);
        iv_play_control = findViewById(R.id.iv_play_control);
        rt = findViewById(R.id.rt);
        tv_start_time = findViewById(R.id.tv_start_time);
        tv_end_time = findViewById(R.id.tv_end_time);
        sb_progress = findViewById(R.id.sb_progress);
        iv_next = findViewById(R.id.iv_next);
        iv_previous = findViewById(R.id.iv_previous);
        iv_play_list = findViewById(R.id.iv_play_list);
        rv = findViewById(R.id.mrv);
        lyric_container = findViewById(R.id.lyric_container);
        rl_player_container = findViewById(R.id.rl_player_container);
        sb_volume = findViewById(R.id.sb_volume);
        lv = findViewById(R.id.lv);

        vp = findViewById(R.id.vp);

        //缓存3个页面
        vp.setOffscreenPageLimit(3);
    }

    @Override
    protected void initDatas() {
        super.initDatas();

        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getApplicationContext());
        playListManager = MusicPlayerService.getPlayListManager(getApplicationContext());

        //音量
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        setVolume();

        currentSong = this.playListManager.getPlayData();

        adapter = new MusicPlayerAdapter(getActivity(), getSupportFragmentManager(), 0);
        vp.setAdapter(adapter);

        setInitData(currentSong);

    }

    private void setLyric(Lyric lyric) {
        parser = LyricsParser.parse(lyric.getStyle(),lyric.getContent());
        parser.parse();

        if (parser.getLyric() != null){
            lv.setData(parser.getLyric());
        }

    }

    private void setVolume() {
        int max = this.audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = this.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        sb_volume.setMax(max);
        sb_volume.setProgress(current);
    }

    private void showLoopModel(int model) {
        switch (model) {
            case PlayListManagerImpl.MODEL_LOOP_RANDOM:
                iv_loop_model.setImageResource(R.drawable.ic_music_play_random);
                break;
            case PlayListManagerImpl.MODEL_LOOP_LIST:
                iv_loop_model.setImageResource(R.drawable.ic_music_play_list);
                break;
            case PlayListManagerImpl.MODEL_LOOP_ONE:
                iv_loop_model.setImageResource(R.drawable.ic_music_play_repleat_one);
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();

        iv_download.setOnClickListener(this);
        iv_play_control.setOnClickListener(this);
        iv_play_list.setOnClickListener(this);
        iv_loop_model.setOnClickListener(this);
        iv_previous.setOnClickListener(this);
        iv_next.setOnClickListener(this);
        sb_progress.setOnSeekBarChangeListener(this);

        //由于歌词控件内部使用了RecyclerView
        //直接给ListLyricView设置点击，长按
        //事件是无效的，因为内部的RecyclerView拦截了
        //解决方法是监听Item点击，然后通过接口回调（当然也可以使用EventBus）回来
        rv.setOnClickListener(this);
        lv.setOnClickListener(this);
        sb_volume.setOnSeekBarChangeListener(this);

//
//        lv.setOnLyricClickListener(this);
        playListManager.addPlayListListener(this);

        vp.addOnPageChangeListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_play_control:
                playOrPause();
                break;
            case R.id.iv_play_list:
                showPlayListDialog();
                break;
            case R.id.iv_previous:
                Song song = playListManager.previous();
                playListManager.play(song);
                break;
            case R.id.iv_next:
                Song songNext = playListManager.next();
                playListManager.play(songNext);
                break;
            case R.id.iv_loop_model:
                int loopModel = playListManager.changeLoopModel();
                showLoopModel(loopModel);
                break;
            case R.id.iv_download:
                download();
                break;
            case R.id.mrv:
                showLyricView();
                break;
            case R.id.lv:
                showRecordView();
                break;
        }

    }

    /*SeekBar*/
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (fromUser) {
            if (seekBar.getId() == R.id.sb_volume) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            } else {
                musicPlayerManager.seekTo(progress);
                if (!musicPlayerManager.isPlaying()) {
                    musicPlayerManager.resume();
                }
            }
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    /**/
    @Override
    public void onDataReady(Song song) {
        setLyric(song.getLyric());
    }

    /*viewpage*/
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /*OnMusicPlayerListener*/
    @Override
    public void onProgress(long progress, long total) {
        tv_start_time.setText(TimeUtil.formatMSTime((int) progress));
        sb_progress.setProgress((int) progress);
        lv.show(progress);
    }

    @Override
    public void onPaused(Song data) {
        iv_play_control.setImageResource(R.drawable.selector_music_play);
        stopRecordRotate();
    }

    @Override
    public void onPlaying(Song data) {
        iv_play_control.setImageResource(R.drawable.selector_music_pause);
        currentSong = data;
        startRecordRotate();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer, Song data) {
        setInitData(data);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onError(MediaPlayer mp, int what, int extra) {

    }

    private void stopRecordRotate() {
        rv.stopAlbumRotate();
//        EventBus.getDefault().post(new OnStopRecordEvent(currentSong));
        rt.stopThumbAnimation();
    }

    private void startRecordRotate() {
        rv.startAlbumRotate();
//        EventBus.getDefault().post(new OnStartRecordEvent(currentSong));
        rt.startThumbAnimation();
    }

    private void playOrPause() {
        if (musicPlayerManager.isPlaying()) {
            pause();
        } else {
            play();
        }
    }

    private void play() {
        musicPlayerManager.resume();
    }

    private void pause() {
        musicPlayerManager.pause();
    }

    private void showPlayListDialog() {

    }

    private void download() {

    }

    public void setInitData(Song data) {

        sb_progress.setMax((int) data.getDuration());
        sb_progress.setProgress(sp.getLastSongProgress());
        tv_start_time.setText(TimeUtil.formatMSTime((int) sp.getLastSongProgress()));
        tv_end_time.setText(TimeUtil.formatMSTime((int) data.getDuration()));

        getActivity().setTitle(data.getTitle());
        getActivity().getSupportActionBar().setSubtitle(data.getArtist().getNickname());

        if (StringUtils.isNotBlank(data.getBanner())) {
            rv.setAlbumUri(data.getBanner());
            RequestOptions requestOptions = bitmapTransform(new BlurTransformation(50, 5));
            Glide.with(this)
                    .asDrawable()
                    .load(ImageUtil.getImageURI(data.getBanner()))
                    .apply(requestOptions)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            AlbumDrawableUtil albumDrawableUtil = new AlbumDrawableUtil(iv_album_bg.getDrawable(), resource);
                            iv_album_bg.setImageDrawable(albumDrawableUtil.getDrawable());
                            albumDrawableUtil.start();
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

        }

    }

    private void showRecordView() {
        lyric_container.setVisibility(View.GONE);
        rl_player_container.setVisibility(View.VISIBLE);
    }

    private void showLyricView() {
        lyric_container.setVisibility(View.VISIBLE);
        rl_player_container.setVisibility(View.GONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (KeyEvent.KEYCODE_VOLUME_UP == keyCode || KeyEvent.KEYCODE_VOLUME_DOWN == keyCode){
            setVolume();
        }
        return super.onKeyDown(keyCode, event);
    }
}
