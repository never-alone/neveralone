package com.finapps.neveralone.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.finapps.neveralone.AlarmActivity;
import com.finapps.neveralone.Application;
import com.finapps.neveralone.R;
import com.finapps.neveralone.net.RestClient;
import com.finapps.neveralone.util.Preferences;
import com.finapps.neveralone.util.UtilBattery;
import com.finapps.neveralone.util.UtilGps;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by OVIL on 25/10/2014.
 */
public class GPSService extends Service  {


    //cuanto mas altos sean estos numeros, menos bateria gastara este servicio
    //   encontrar unos que permitan que la app funcione correctamente y no
    //   perjudique la bateria demasiado
    private static int MIN_TIME_BW_UPDATES = 40*1000;//20 segudos
    private static int MIN_DISTANCE_CHANGE_FOR_UPDATES = 20;//20 metros

    private static int RADIO_ZONA_CONFORT = 1000;
    private static int RADIO_AVISO_ZONA_CONFORT = 500;
    private static boolean sigoFueraZonaConfort = false;

    private LocationManager locationManager;
    private LocationListener listener = new LocationListener() {
        // @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        // @Override
        public void onProviderEnabled(String provider) {
            if (provider.equalsIgnoreCase( LocationManager.NETWORK_PROVIDER)){
            }
        }

        // @Override
        public void onProviderDisabled(String provider) {
            if (provider.equalsIgnoreCase( LocationManager.NETWORK_PROVIDER)){
            }
        }

        @Override
        public void onLocationChanged(Location location) {
            float latitud = Float.parseFloat(location.getLatitude() + "");
            float longitud = Float.parseFloat(location.getLongitude() + "");
            tratarPosicion(longitud, latitud);
        }
    };




    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        onStartActions();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onStartActions();
        return START_STICKY;//hacemos esto para que no se mate el servicio cuando falte memoria en el teléfono
    }

    private void onStartActions(){
        //return super.onStartCommand(intent, flags, startId);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
    }

    @Override
    public void onDestroy() {
        locationManager.removeUpdates(listener);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private void tratarPosicion(float longitude, float latitude) {
        Preferences pref = new Preferences(Application.getContext());
        float initLatitude = pref.getInitialLatitude();
        float initLongitude = pref.getInitialLongitude();

        if (pref.getInitialLatitude()==0 && pref.getInitialLongitude()==0){
            pref.saveInitialLatitude(latitude);
            pref.saveInitialLongitude(longitude);
            initLatitude = latitude;
            initLongitude = longitude;
        }
        double distance = UtilGps.distFrom(initLatitude, initLongitude, latitude, longitude);

        if (distance>RADIO_ZONA_CONFORT){
            if (sigoFueraZonaConfort){
                //si ya estabamos fuera de la zona de confort, ya hemos mostrado la pantalla de alarma
            }else{
                sigoFueraZonaConfort = true;
                enviarMensajeFueraAreaControl(latitude, longitude);
            }
        }else if (distance>RADIO_AVISO_ZONA_CONFORT){
            sigoFueraZonaConfort = false;
            mostrarNotificacion(distance, RADIO_ZONA_CONFORT-distance);
        }else{
            sigoFueraZonaConfort = false;
        }

    }



    /**
     * Este metodo lanza una notificacion (o la modifica) en cuanto el usuario se
     *  aleje demasiado del centro de su area de confort
     * @param metrosToOrigen
     * @param metrosToFuera
     */
    private void mostrarNotificacion(double metrosToOrigen, double metrosToFuera){
        enviarPush(Application.getContext().getString(R.string.gpsNotifWarningTitle), Application.getContext().getString(R.string.gpsNotifWarningMessage));
    }

    private void enviarMensajeFueraAreaControl(float latitude, float longigude){
        Preferences pref = new Preferences(Application.getContext());
        RestClient client = new RestClient(this);
        String mail = Application.getContext().getString(R.string.gpsNotifErrorMail);
        mail = mail.replaceFirst("#",pref.getNameUser());
        String direccion =UtilGps.getAddressFromLocation(latitude, longigude);
        mail = mail.replaceFirst("#",direccion);
        float battery = UtilBattery.getBatteryCharge(Application.getContext());
        mail = mail.replaceFirst("#",(Float.toString(battery*100)));
        client.alarm(mail);
        enviarPush(Application.getContext().getString(R.string.gpsNotifErrorTittle), Application.getContext().getString(R.string.gpsNotifErrorMessage));
    }


    public static void enviarPush(String titulo, String cuerpo){
        int notificationId = 448;
        // Build intent for notification content
        Intent viewIntent = new Intent(Application.getContext(), AlarmActivity.class);
        viewIntent.putExtra("tipo", "gps");
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
