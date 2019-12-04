package com.example.muiscdemo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muiscdemo.R;
import com.example.muiscdemo.adapter.LyricAdapter;
import com.example.muiscdemo.listener.OnLyricClickListener;
import com.example.muiscdemo.parser.domain.Line;
import com.example.muiscdemo.parser.domain.Lyric;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

public class ListLyricView extends LinearLayout implements ViewTreeObserver.OnGlobalLayoutListener {

    /**
     * 事件类型
     */
    private static final int MSG_HIDE_TIME_LINE = 0;

    /**
     * 拖拽后，多少秒继续滚动歌词
     */
    private static final long DEFAULT_HIDE_DRAG_TIME = 4000;

    /**
     * 点击了歌词旁边的播放
     */
    private OnLyricClickListener onLyricClickListener;

    /**
     * 歌词
     */
    private Lyric lyric;

    /**
     * 歌词列表View
     */
    private RecyclerView rv;

    /**
     * 是否是拖拽模式
     */
    private boolean isDrag;

    /**
     * 当前所在歌词的行号
     */
    private int lineNumber = 0;

    /**
     * 歌词滚动偏移
     * 会在运行的时候动态计算
     */
    private int lyricItemOffset;

    /**
     * 默认填充数据行
     */
    private static final int DEFAULT_FILL_LYRIC_COUNT = 5;

    /**
     * 歌词监听器
     */
    private LyricListener lyricListener;

    private LinearLayoutManager layoutManager;
    private LyricAdapter adapter;
    private LinearLayout ll_lyric_drag_container;
    private ImageButton ib_lyric_play;
    private TextView tv_lyric_time;
    private TimerTask timerTask;
    private Timer timer;
    private Line scrollSelectedLyricLine;


    public ListLyricView(Context context) {
        super(context);
        init();
    }

    public ListLyricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListLyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public ListLyricView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        initViews();
        initListeners();
    }

    private void initListeners() {

    }

    private void initViews() {
        View.inflate(getContext(), R.layout.layout_list_lyric_view, this);

        ll_lyric_drag_container = findViewById(R.id.ll_lyric_drag_container);
        ib_lyric_play=findViewById(R.id.ib_lyric_play);
        tv_lyric_time=findViewById(R.id.tv_lyric_time);
        rv=findViewById(R.id.rv);

        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        rv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        rv.getViewTreeObserver().addOnGlobalLayoutListener(this);

        adapter = new LyricAdapter(R.layout.item_lyric_line);
        rv.setAdapter(adapter);

    }

    @Override
    public void onGlobalLayout() {

    }

    /* 设置歌词数据*/
    public void setData(Lyric lyric){
        this.lyric = lyric;

        Collection<Line> values = lyric.getLyrics().values();
        ArrayList<Object> valueList = new ArrayList<>();

        for (int i = 0; i < DEFAULT_FILL_LYRIC_COUNT; i++) {
            valueList.add("fill");
        }

        //添加歌词数据
        valueList.addAll(values);

        for (int i = 0; i < DEFAULT_FILL_LYRIC_COUNT; i++) {
            valueList.add("fill");
        }

        adapter.setAccurate(lyric.isAccurate());
        adapter.setNewData(valueList);
    }



    /**
     * 歌词View监听器
     */
    public interface LyricListener {
        /**
         * 当前歌词点击回调
         *
         * @param position
         */
        void onLyricItemClick(int position);

        /**
         * 当歌词长按回调
         *
         * @param position
         */
        void onLyricItemLongClick(int position);
    }
}
