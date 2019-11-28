package com.example.muiscdemo.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

public class SquareLinearLayout extends LinearLayout {
    public SquareLinearLayout(Context context) {
        super(context);
    }

    public SquareLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SquareLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0,widthMeasureSpec),getDefaultSize(0,heightMeasureSpec));

        int width = getMeasuredWidth();
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
