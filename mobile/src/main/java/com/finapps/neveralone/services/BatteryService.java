package com.finapps.neveralone.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;


public class BatteryService extends Service {
    @Override
    public IBinder onBind(Intent intent) {


        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "BatteryService.onCreate", Toast.LENGTH_SHORT);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Toast.makeText(this, "BatteryService.onStart", Toast.LENGTH_SHORT);
    }
}
