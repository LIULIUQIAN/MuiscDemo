package com.example.muiscdemo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.muiscdemo.R;
import com.example.muiscdemo.parser.domain.Line;
import com.example.muiscdemo.util.DensityUtil;

public class LyricLineView extends View {

    /**
     * 默认歌词字体大小
     */
    private static final float DEFAULT_LYRIC_FONT_SIZE = 16;

    /**
     当前歌词行是否选中，也就是唱到这一行了
     */
    private boolean isSelected;

    /**
     * 当前歌词是否是精确模式
     */
    private boolean isAccurate;

    /**
     * 当前播放时间点，在该行歌词的第几个字
     */
    private int lyricCurrentWordIndex = -1;

    /**
     * 当前行歌词已经唱过的宽度，也就是歌词高亮宽度
     */
    private float lineLyricPlayedWidth;

    /**
     * 当前字，已经播放的时间
     */
    private float wordPlayedTime = 0;

    private Paint backgroundTextPaint;
    private Paint foregroundTextPaint;
    private Line line;

    public LyricLineView(Context context) {
        super(context);
        init();
    }

    public LyricLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LyricLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public LyricLineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        backgroundTextPaint = new Paint();
        backgroundTextPaint.setDither(true);
        backgroundTextPaint.setAntiAlias(true);
        backgroundTextPaint.setTextSize(DensityUtil.dip2px(getContext(),DEFAULT_LYRIC_FONT_SIZE));
        backgroundTextPaint.setColor(Color.WHITE);

        foregroundTextPaint = new Paint();
        foregroundTextPaint.setDither(true);
        foregroundTextPaint.setAntiAlias(true);
        foregroundTextPaint.setTextSize(DensityUtil.dip2px(getContext(),DEFAULT_LYRIC_FONT_SIZE));
        foregroundTextPaint.setColor(getResources().getColor(R.color.main_color));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (line == null){
            return;
        }

        canvas.save();

        //当前歌词的宽高
        float textWidth = getTextWidth(backgroundTextPaint,line.getLineLyrics());
        float textHeight = getTextHeight(backgroundTextPaint);
        Paint.FontMetrics fontMetrics = backgroundTextPaint.getFontMetrics();

        float centerY = (getMeasuredHeight() - textHeight)/2 + Math.abs(fontMetrics.top);
        float centerX = (getMeasuredWidth() - textWidth)/2;
        canvas.drawText(line.getLineLyrics(),centerX,centerY,backgroundTextPaint);

        if (!isSelected){
            canvas.restore();
            return;
        }
        if (isAccurate){

            if (lyricCurrentWordIndex == -1){
                lineLyricPlayedWidth = textWidth;
            }else {
                String[] lyricsWord = line.getLyricsWord();
                int[] wordDuration = line.getWordDuration();
                //获取当前时间前面的文字
                String beforeText = line.getLineLyrics().substring(0,lyricCurrentWordIndex);
                float beforeTextWidth = getTextWidth(foregroundTextPaint,beforeText);

                //获取当前字
                String currentWord = lyricsWord[lyricCurrentWordIndex];
                float currentWordTextWidth = getTextWidth(foregroundTextPaint,currentWord);

                float currentWordWidth = currentWordTextWidth/wordDuration[lyricCurrentWordIndex];
                lineLyricPlayedWidth = beforeTextWidth + currentWordWidth;
            }

            canvas.clipRect(centerX,0,centerX+lineLyricPlayedWidth,getMeasuredHeight());
            canvas.drawText(line.getLineLyrics(),centerX,centerY,foregroundTextPaint);

        }else {
            canvas.drawText(line.getLineLyrics(),centerX,centerY,foregroundTextPaint);
        }

        canvas.restore();
    }

    private float getTextWidth(Paint paint, String text){
        return paint.measureText(text);
    }
    private float getTextHeight(Paint paint){
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();

        return (float) Math.ceil(fontMetrics.descent - fontMetrics.ascent);
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public void setLineSelected(boolean selected) {
        isSelected = selected;
    }

    public void setAccurate(boolean accurate) {
        isAccurate = accurate;
    }

    public void show(long position) {
        invalidate();
    }

    public void setLyricCurrentWordIndex(int lyricCurrentWordIndex) {
        this.lyricCurrentWordIndex = lyricCurrentWordIndex;
    }

    public void setWordPlayedTime(float wordPlayedTime) {
        this.wordPlayedTime = wordPlayedTime;
    }

}
