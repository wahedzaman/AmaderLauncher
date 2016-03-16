package com.durbinsoft.amarlauncher;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Md.Wahuduzzaman on 3/16/2016.
 */
public class TimerService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        HomeClockUpdateHandler hm = new HomeClockUpdateHandler();
        if(hm.isInitialized()){
            hm.updateHomeClockTime();
        }else{
        }
        stopSelf();
        return START_NOT_STICKY;
    }
}