package com.example.muiscdemo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.example.muiscdemo.R;
import com.example.muiscdemo.util.DensityUtil;

public class RecordBackgroundView extends View {

    /**
     * Cd白圈背景的比例
     */
    public static final float CD_BG_SCALE = 1.3F;

    /**
     * 白圈
     */
    private Drawable cdBg;

    /**
     * CD白圈背景到顶部的比例
     */
    public static final float CD_BG_TOP_SCALE = 17.052F;

    public RecordBackgroundView(Context context) {
        super(context);
        init();
    }

    public RecordBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecordBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public RecordBackgroundView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        cdBg = getResources().getDrawable(R.drawable.shape_cd_bg);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredWidth = getMeasuredWidth();

        int cdBgWidth = (int) (measuredWidth/CD_BG_SCALE);

        int cdBgLeft = (measuredWidth -cdBgWidth)/2;
        int cdBgTop = DensityUtil.dip2px(getContext(),measuredWidth/CD_BG_TOP_SCALE);

        cdBg.setBounds(cdBgLeft,cdBgTop,cdBgLeft+cdBgWidth,cdBgTop+cdBgWidth);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        cdBg.draw(canvas);
        canvas.restore();
    }
}
