package com.example.muiscdemo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.example.muiscdemo.R;
import com.example.muiscdemo.adapter.LyricAdapter;
import com.example.muiscdemo.listener.OnLyricClickListener;
import com.example.muiscdemo.parser.domain.Line;
import com.example.muiscdemo.parser.domain.Lyric;
import com.example.muiscdemo.util.TimeUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_DRAGGING;
import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_IDLE;

public class ListLyricView extends LinearLayout implements ViewTreeObserver.OnGlobalLayoutListener, View.OnClickListener {

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

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_DRAGGING) {
                    //拖拽状态
                    showDragView();
                } else if (newState == SCROLL_STATE_IDLE) {
                    //空闲状态
                    prepareShowScrollLyricView();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()+DEFAULT_FILL_LYRIC_COUNT-1;
                if (isDrag){
                    Object data = adapter.getItem(firstVisibleItemPosition);
                    if (data instanceof String){
                        if (firstVisibleItemPosition < DEFAULT_FILL_LYRIC_COUNT){
                            scrollSelectedLyricLine = (Line) adapter.getItem(DEFAULT_FILL_LYRIC_COUNT);
                        }else {
                            scrollSelectedLyricLine = (Line) adapter.getItem(adapter.getItemCount() - DEFAULT_FILL_LYRIC_COUNT - 1);
                        }
                    }else {
                        scrollSelectedLyricLine = (Line) data;
                    }

                    tv_lyric_time.setText(TimeUtil.parseString((int) scrollSelectedLyricLine.getStartTime()));
                }

            }
        });

        ib_lyric_play.setOnClickListener(this);

        rv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (lyricListener != null){
                    lyricListener.onLyricItemClick(position);
                }
            }
        });

        rv.addOnItemTouchListener(new OnItemLongClickListener() {
            @Override
            public void onSimpleItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (lyricListener != null){
                    lyricListener.onLyricItemLongClick(position);
                }
            }
        });


    }

    private void showDragView() {
        isDrag = true;
        ll_lyric_drag_container.setVisibility(View.VISIBLE);
    }

    private void prepareShowScrollLyricView() {
        cancelTask();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.obtainMessage(MSG_HIDE_TIME_LINE).sendToTarget();
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, DEFAULT_HIDE_DRAG_TIME);

    }

    private void cancelTask() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void initViews() {
        View.inflate(getContext(), R.layout.layout_list_lyric_view, this);

        ll_lyric_drag_container = findViewById(R.id.ll_lyric_drag_container);
        ib_lyric_play = findViewById(R.id.ib_lyric_play);
        tv_lyric_time = findViewById(R.id.tv_lyric_time);
        rv = findViewById(R.id.rv);

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
        lyricItemOffset = rv.getHeight() / 2;
    }

    /* 设置歌词数据*/
    public void setData(Lyric lyric) {
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

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.ib_lyric_play){

            onLyricClickListener.onLyricClick(scrollSelectedLyricLine.getStartTime());
            showScrollLyricView();
            cancelTask();
        }

    }

    /*设置歌词监听器*/
    public void setOnLyricClickListener(OnLyricClickListener onLyricClickListener) {
        this.onLyricClickListener = onLyricClickListener;
    }

    /*根据传递进来的时间显示对应的歌词*/
    public void show(long position) {
        if (lyric == null) {
            return;
        }
        //如果手动拖拽时，就不滚动
        if (isDrag) {
            return;
        }

        int newLineNumber = lyric.getLineNumber(position) + DEFAULT_FILL_LYRIC_COUNT;
        if (newLineNumber != lineNumber) {
            scrollToPosition(newLineNumber);
            this.lineNumber = newLineNumber;
        }

        if (lyric.isAccurate()) {
            int realNumber = lineNumber - DEFAULT_FILL_LYRIC_COUNT;
            int lyricCurrentWordIndex = lyric.getWordIndex(realNumber, position);
            float wordPlayedTime = lyric.getWordPlayedTime(realNumber, position);

            View view = layoutManager.findViewByPosition(lineNumber);
            if (view != null) {
                LyricLineView llv = view.findViewById(R.id.llv);
                llv.setLyricCurrentWordIndex(lyricCurrentWordIndex);
                llv.setWordPlayedTime(wordPlayedTime);
                llv.show(position);
            }

        }

    }

    private void scrollToPosition(int lineNumber) {

        adapter.setSelectedIndex(lineNumber);

        if (lyricItemOffset > 0) {
            layoutManager.scrollToPositionWithOffset(lineNumber, lyricItemOffset);
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_HIDE_TIME_LINE:
                    showScrollLyricView();
                    break;
            }
        }
    };

    private void showScrollLyricView() {
        isDrag = false;
        ll_lyric_drag_container.setVisibility(GONE);
    }

    public void setLyricListener(LyricListener lyricListener) {
        this.lyricListener = lyricListener;
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
