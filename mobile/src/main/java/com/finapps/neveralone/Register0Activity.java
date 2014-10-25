package com.finapps.neveralone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import com.finapps.neveralone.services.BatteryService;
import com.finapps.neveralone.services.GPSService;
import com.finapps.neveralone.util.Preferences;


public class Register0Activity extends Activity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_0);
        getActionBar().hide();

        //si ya se ha registrado, no hacemos esto y pasamos directamente a main activity
        Preferences pref = new Preferences(this);
        if (pref.getNameUser()!=null){
                arrancarServiciosGPS();
            Intent nextLogica = new Intent(this, MainActivity.class);
            startActivity(nextLogica);
            finish();
        }else {
            //arrancarServiciosBatery();
            handler = new Handler();
            handler.postDelayed(nextLogical, 2000);
        }

    }

    private Runnable nextLogical = new Runnable() {
        public void run() {
            goNextActivity();
        }
    };

    private void goNextActivity(){
        Intent nextLogica = new Intent(this, Register1Activity.class);
        startActivity(nextLogica);
        finish();
    }


    public static void arrancarServiciosGPS() {
        Application.getContext().startService(new Intent(Application.getContext(), GPSService.class));
    }

    public static void arrancarServiciosBatery(Context context){

        //Create a new PendingIntent and add it to the AlarmManager
        /*
        Intent intent = new Intent(this, BatteryService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),10000,pendingIntent);
        */

        Intent intent = new Intent(context, BatteryService.class);
        PendingIntent pintent = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager alarm = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),120000,pintent);
        //alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);
    }
}
