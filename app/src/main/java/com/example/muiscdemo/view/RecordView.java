package com.example.muiscdemo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.muiscdemo.R;
import com.example.muiscdemo.util.DensityUtil;
import com.example.muiscdemo.util.ImageUtil;

import java.util.Timer;
import java.util.TimerTask;

public class RecordView extends View {

    /**
     * 黑胶唱片宽高比例
     */
    private static final float CD_SCALE = 1.333F;

    /**
     * 封面比例
     */
    //private static final float ALBUM_SCALE = 2.037F;
    private static final float ALBUM_SCALE = 2.1F;

    /**
     * 每16毫秒旋转的角度
     * <p>
     * 16毫秒是通过，每秒60帧计算出来的
     * 也就是1000/60=16，也就是说绘制一帧要在16毫秒中完成，不然就能感觉卡顿
     */
    public static final float ROTATION_PER = 0.2304F;

    /**
     * 画笔
     */
    private Paint paint;

    /**
     * 黑胶唱片bitmap
     */
    private Bitmap cd;

    /**
     * 黑胶唱片绘制坐标
     */
    private Point cdPoint = new Point();

    /**
     * 旋转点，都是在中点，所以一个就够了
     */
    private Point cdRotationPoint = new Point();

    /**
     * 封面的宽度
     */
    private int albumWidth;

    /**
     * 封面绘制坐标
     */
    private Point albumPoint = new Point();


    /**
     * 封面图
     */
    private String albumUri;


    /**
     * 封面bitmap
     */
    private Bitmap album;

    /**
     * 黑胶唱片矩阵
     */
    private Matrix cdMatrix = new Matrix();

    /**
     * 封面矩阵
     */
    private Matrix albumMatrix = new Matrix();


    /**
     * 旋转的角度
     */
    private float cdRotation = 0;

    /**
     * 计时器任务
     */
    private TimerTask timerTask;

    /**
     * 计算器，用来调度唱片，专辑转动
     */
    private Timer timer;

    public RecordView(Context context) {
        super(context);
        init();
    }

    public RecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public RecordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();

        initResource();

        int cdWidthHalf = cd.getWidth() / 2;
        int cdLeft = measuredWidth / 2 - cdWidthHalf;

        int cdBgwidth = (int) (measuredWidth / RecordBackgroundView.CD_BG_SCALE);
        int cdBgTop = DensityUtil.dip2px(getContext(), measuredWidth / RecordBackgroundView.CD_BG_TOP_SCALE);
        int cdBgCenterY = cdBgTop + cdBgwidth / 2;

        int cdTop = cdBgCenterY - cdWidthHalf;
        cdPoint.set(cdLeft, cdTop);
        cdRotationPoint.set(measuredWidth / 2, cdWidthHalf + cdTop);

        albumWidth = (int) (measuredWidth / ALBUM_SCALE);
        int albumLeft = measuredWidth / 2 - albumWidth / 2;
        int albumTop = cdBgCenterY - albumWidth / 2;
        albumPoint.set(albumLeft, albumTop);

        showAlbum();

    }

    private void showAlbum() {
        if (albumWidth != 0) {
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            options.circleCrop();
            options.override(albumWidth, albumWidth);
            Glide.with(this)
                    .asBitmap()
                    .load(ImageUtil.getImageURI(this.albumUri))
                    .apply(options)
                    .into(new CustomTarget<Bitmap>() {

                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            album = ImageUtil.resizeImage(resource, albumWidth, albumWidth);
                            invalidate();
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });

        }
    }

    public void setAlbumUri(String albumUri) {
        this.albumUri = albumUri;
        showAlbum();
    }

    private void initResource() {
        if (cd == null) {
            int cdWidth = (int) (getMeasuredWidth() / CD_SCALE);
            cd = ImageUtil.scaleBitmap(getResources(), R.drawable.cd_bg, cdWidth, cdWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        cdMatrix.setRotate(cdRotation,cdRotationPoint.x,cdRotationPoint.y);
        cdMatrix.preTranslate(cdPoint.x,cdPoint.y);
        canvas.drawBitmap(cd,cdMatrix,paint);

        if (album != null){
            albumMatrix.setRotate(cdRotation,cdRotationPoint.x,cdRotationPoint.y);
            albumMatrix.preTranslate(albumPoint.x,albumPoint.y);
            canvas.drawBitmap(album,albumMatrix,paint);
        }

        canvas.restore();
    }

    public void stopAlbumRotate() {
        cancelTask();
    }

    public void startAlbumRotate() {
        cancelTask();
        timerTask = new TimerTask() {

            @Override
            public void run() {
                if (cdRotation >= 360) {
                    cdRotation = 0;
                }
                cdRotation += ROTATION_PER;
                postInvalidate();
            }
        };
        timer = new Timer();

        timer.schedule(timerTask, 0, 16);
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
}
