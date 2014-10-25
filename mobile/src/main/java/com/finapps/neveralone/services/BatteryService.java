package com.finapps.neveralone.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.finapps.neveralone.AlarmActivity;
import com.finapps.neveralone.Application;
import com.finapps.neveralone.R;
import com.finapps.neveralone.util.Preferences;
import com.finapps.neveralone.util.UtilBattery;


public class BatteryService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        Preferences pref = new Preferences(Application.getContext());
        if (pref.getNameUser()!=null) {
            //1. Get batery
            float batery = UtilBattery.getBatteryCharge(Application.getContext());

            //2. Get last pos gps
            float last_latitude = pref.getLastLatitude();
            float last_longitud = pref.getLastLongitude();
            //de momento no haremos nada con esto, pero cuanod el servidor esté preparado para recibirlo
            //  también irá en la llamada.


            if (batery * 100 < 5 || pref.askSimularNoBateria()) {
                //la bateria es inferior al 5%, lanzamos un aviso
                enviarPush(Application.getContext().getString(R.string.gpsNotifErrorTittle), Application.getContext().getString(R.string.batteryLowErrorMessage));
            }
        }
    }

    public static void enviarPush(String titulo, String cuerpo){
        int notificationId = 448;
        // Build intent for notification content
        Intent viewIntent = new Intent(Application.getContext(), AlarmActivity.class);
        viewIntent.putExtra("tipo", "bateria");
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(Application.getContext(), 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(Application.getContext())
                        .setSmallIcon(R.drawable.ic_action_place_light)
                        .setContentTitle(titulo)
                        .setContentText(cuerpo)
                        .setContentIntent(viewPendingIntent);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(Application.getContext());

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());

        playNotificationSound(Application.getContext());
    }

    private static void playNotificationSound(Context context) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (uri != null) {
            Ringtone rt = RingtoneManager.getRingtone(context, uri);
            if (rt != null)
                rt.play();
        }
    }
}
