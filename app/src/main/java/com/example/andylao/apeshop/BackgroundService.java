package com.example.andylao.apeshop;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;


/**
 * Created by Andy Lao on 2018-01-01.
 */

public class BackgroundService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        Toast.makeText(this, "Started Service", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        int userId = 0;
        SharedPreferences preferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("userId",userId);
        editor.apply();

        Toast.makeText(this, "Stop Service", Toast.LENGTH_LONG).show();
    }
}
