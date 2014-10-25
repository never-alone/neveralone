package com.finapps.neveralone.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.finapps.neveralone.AlarmBateryActivity;
import com.finapps.neveralone.AlarmGpsActivity;
import com.finapps.neveralone.Application;
import com.finapps.neveralone.R;
import com.finapps.neveralone.net.RestClient;
import com.finapps.neveralone.util.Preferences;
import com.finapps.neveralone.util.UtilBattery;

import java.util.ArrayList;
import java.util.List;


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

            RestClient client = new RestClient(this);
            client.track(batery);

            if (batery * 100 < 5 || pref.askSimularNoBateria()) {
                //la bateria es inferior al 5%, lanzamos un aviso
                enviarPush(Application.getContext().getString(R.string.gpsNotifErrorTittle), Application.getContext().getString(R.string.batteryLowErrorMessage));
            }
        }
    }

    public static void enviarPush(String titulo, String cuerpo){
        int notificationId = 448;
        // Build intent for notification content
        Intent viewIntent = new Intent(Application.getContext(), AlarmBateryActivity.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(Application.getContext(), 0, viewIntent, 0);
/*
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(Application.getContext())
                        .setSmallIcon(R.drawable.ic_action_battery)
                        .setContentTitle(titulo)
                        .setContentText(cuerpo)
                        .setContentIntent(viewPendingIntent);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(Application.getContext());

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
*/


        //-----

        Context ctx=Application.getContext();
        List extraPages = new ArrayList();
        // creating intent to open main Activity

        // bigstyle notification pages
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        // setting title for the notification
        bigTextStyle.setBigContentTitle("Bateria baja");
        // setting content

        String texto = ctx.getString(R.string.bateryLow);
        float batery = UtilBattery.getBatteryCharge(ctx)*100;
        texto = texto.replace("#" , Float.toString(batery));
        bigTextStyle.bigText(texto);
        // creating notification
        Notification extraPageNotify1 = new NotificationCompat.Builder(Application.getContext())
                .setStyle(bigTextStyle)
                        // setting up a background image
                .setLargeIcon(
                        BitmapFactory.decodeResource(Application.getContext().getResources(),
                                R.drawable.ic_launcher)).build();
        // adding notification to the list
        extraPages.add(extraPageNotify1);


        NotificationCompat.WearableExtender wearExtender = new NotificationCompat.WearableExtender()
                //adding Collection of pages to the notification
                .addPages(extraPages)
                .setHintHideIcon(true)
                .setBackground(
                        BitmapFactory.decodeResource(ctx.getResources(),
                                R.drawable.ic_launcher));

        // Here you create the notification and start adding all the attributes
        // you are going to use
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.ic_action_battery)
                .setContentTitle(titulo)
                .setContentText(cuerpo)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(viewPendingIntent)
                .extend(wearExtender);


        // adding action that will be launch the main activity again
        //        .addAction(android.R.drawable.ic_media_play, "GO Back",
        //                mainPendingIntent)


        // Here we instantiate the Notification Manager object to start/stop the
        // notifications
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);
        notificationManager.notify(notificationId,
                notificationBuilder.build());

        //-----




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
