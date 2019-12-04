package com.example.muiscdemo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.example.muiscdemo.manager.MusicPlayerManager;
import com.example.muiscdemo.manager.PlayListManager;
import com.example.muiscdemo.manager.impl.MusicPlayerManagerImpl;
import com.example.muiscdemo.manager.impl.PlayListManagerImpl;
import com.example.muiscdemo.util.ServiceUtil;

public class MusicPlayerService extends Service {

    private static MusicPlayerManager manager;
    private static PlayListManager playListManager;
//    private static FloatingLayoutManager floatingLayoutManager;
//

    public static MusicPlayerManager getMusicPlayerManager(Context context){
        startService(context);
        if (MusicPlayerService.manager == null){
            MusicPlayerService.manager = MusicPlayerManagerImpl.getInstance(context);
        }
        return manager;
    }

    /**
     * 获取一个PlayListManager对象
     * @param context
     * @return
     */
    public static PlayListManager getPlayListManager(Context context) {
        startService(context);

        if (MusicPlayerService.playListManager == null) {
            //初始化列表管理器
            MusicPlayerService.playListManager = PlayListManagerImpl.getInstance(context);
        }
        return playListManager;
    }

    private static void startService(Context context){
        if (!ServiceUtil.isServiceRunning(context)){
            Intent service = new Intent(context, MusicPlayerService.class);
            context.startService(service);
        }
    }



    public MusicPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
