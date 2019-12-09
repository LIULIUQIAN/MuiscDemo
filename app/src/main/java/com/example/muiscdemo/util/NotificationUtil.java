package com.example.muiscdemo.util;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Window;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.muiscdemo.MainActivity;
import com.example.muiscdemo.R;
import com.example.muiscdemo.domain.Song;

public class NotificationUtil {
    private static final int NOTIFICATION_MUSIC_ID = 10000;
    private static final int NOTIFICATION_UNLOCK_LYRIC_ID = 10001;
    public static final String sID = "channel_1";
    public static final String sName = "channel_name_1";
    private static NotificationManager notificationManager;

    /**
     * 显示播放音乐通知栏
     *
     * @param context
     * @param song
     * @param isPlaying
     */
    public static void showMusicNotification(final Context context, final Song song, final boolean isPlaying) {
        RequestOptions options = new RequestOptions();
        options.centerCrop();

        Glide.with(context)
                .asBitmap()
                .apply(options)
                .load(ImageUtil.getImageURI(song.getBanner()))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        showData(context,song,isPlaying,resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

    }

    private static void showData(Context context, Song song, boolean isPlaying,Bitmap resource){
        //设置标准通知数据
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_music_play);
        contentView.setImageViewBitmap(R.id.iv_icon, resource);
        contentView.setTextViewText(R.id.tv_title, song.getTitle());
        if (song.getArtist() != null && song.getAlbum() != null) {
            contentView.setTextViewText(R.id.tv_info, String.format("%s - %s", song.getArtist().getNickname(), song.getAlbum().getTitle()));
        }

        int playResId = isPlaying ? R.drawable.ic_music_notification_pause : R.drawable.ic_music_notification_play;
        contentView.setImageViewResource(R.id.iv_play, playResId);

        //设置标准通知，点击事件
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, Consts.ACTION_PLAY.hashCode(), new Intent(Consts.ACTION_PLAY), PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.iv_play, playPendingIntent);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, Consts.ACTION_NEXT.hashCode(), new Intent(Consts.ACTION_NEXT), PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.iv_next, nextPendingIntent);
        PendingIntent lyricPendingIntent = PendingIntent.getBroadcast(context, Consts.ACTION_LYRIC.hashCode(), new Intent(Consts.ACTION_LYRIC), PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.iv_play_screen_lyric, lyricPendingIntent);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Consts.ACTION_MUSIC_PLAYER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, Consts.ACTION_LYRIC.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        getNotificationManager(context);
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            Notification.Builder builder = new Notification.Builder(context, sID)
                    .setAutoCancel(false)
                    .setSmallIcon(R.drawable.ic_logo)
                    .setCustomContentView(contentView)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_logo))
                    .setContentIntent(contentPendingIntent);
            notificationManager.notify(NOTIFICATION_MUSIC_ID, builder.build());

        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setAutoCancel(false)
                    .setSmallIcon(R.drawable.ic_logo)
                    .setCustomContentView(contentView)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_logo))
                    .setContentIntent(contentPendingIntent);

            notificationManager.notify(NOTIFICATION_MUSIC_ID, builder.build());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(sID, sName, NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
    }


    /**
     * 显示解锁桌面歌词通知栏
     *
     * @param context
     */
    public static void showUnlockLyricNotification(final Context context) {
        PendingIntent contentPendingIntent = PendingIntent.getBroadcast(context, Consts.ACTION_UNLOCK_LYRIC.hashCode(), new Intent(Consts.ACTION_UNLOCK_LYRIC), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("桌面歌词已经锁定")
                .setContentText("点击解锁")
                .setContentIntent(contentPendingIntent);

//        notify(context, NOTIFICATION_UNLOCK_LYRIC_ID, builder.build());
    }


    /**
     * 如果歌词解锁了，需要清除解锁桌面歌词通知栏
     *
     * @param context
     */
    public static void clearUnlockLyricNotification(Context context) {
        getNotificationManager(context);
        notificationManager.cancel(NOTIFICATION_UNLOCK_LYRIC_ID);
    }

//    /**
//     * 显示聊天消息（不再聊天界面才显示）
//     *
//     * @param context
//     * @param userId
//     * @param content
//     */
//    public static void showMessageNotification(final Context context, final String userId, final String content, final int unreadCount) {
//        UserManager userManager = UserManagerImpl.getInstance(context);
//        //获取消息发送者的的信息
//        userManager.getUser(userId, new UserManager.OnUserListener() {
//            @Override
//            public void onUser(final User user) {
//                //获取用户信息，获取用户头像的Bitmap
//                //这里的嵌套可以通过RxJava转换成链式
//                RequestOptions options = new RequestOptions();
//                options.circleCrop();
//                Glide.with(context).asBitmap().load(user.getAvatar()).apply(options).into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_message);
//                        contentView.setImageViewBitmap(R.id.iv_icon, resource);
//
//                        if (unreadCount > 1) {
//                            contentView.setTextViewText(R.id.tv_title, String.format("%s（%d条消息）", user.getNickname(), unreadCount));
//                        } else {
//                            contentView.setTextViewText(R.id.tv_title, user.getNickname());
//                        }
//
//                        contentView.setTextViewText(R.id.tv_info, content);
//
//                        //点击通知，跳转到聊天界面
//                        Intent intent = new Intent(context, MainActivity.class);
//                        intent.putExtra(Consts.ID, userId);
//                        intent.setAction(Consts.ACTION_MESSAGE);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, Consts.ACTION_LYRIC.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                                .setSmallIcon(R.drawable.ic_logo)
//                                .setCustomContentView(contentView)
//                                .setAutoCancel(true)
//                                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_logo))
//                                .setContentIntent(contentPendingIntent);
//
//                        //用发送者的id来显示通知
//                        NotificationUtil.notify(context, userId.hashCode(), builder.build());
//
//
//                    }
//                });
//            }
//        });
//
//
//    }


    private static void getNotificationManager(Context context) {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }
}