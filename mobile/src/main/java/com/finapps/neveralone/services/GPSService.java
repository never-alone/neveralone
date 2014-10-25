package com.finapps.neveralone.services;

import android.app.Notification;
import android.app.NotificationManager;
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
import com.finapps.neveralone.util.UtilGps;

import java.util.Date;


/**
 * Created by OVIL on 25/10/2014.
 */
public class GPSService extends Service  {


    //cuanto mas altos sean estos numeros, menos bateria gastara este servicio
    //   encontrar unos que permitan que la app funcione correctamente y no
    //   perjudique la bateria demasiado
    private static int MIN_TIME_BW_UPDATES = 1*1000;//20 segudos
    private static int MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;//20 metros

    private static int RADIO_ZONA_CONFORT = 50;
    private static int RADIO_AVISO_ZONA_CONFORT = 20;
    private static boolean sigoFueraZonaConfort = false;

    Toast toast;

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
        if (initLatitude==0){
            pref.saveInitialLatitude(latitude);
            initLatitude = latitude;
        }

        if (pref.getInitialLatitude()==0 && pref.getInitialLongitude()==0){
            pref.saveInitialLatitude(latitude);
            pref.saveInitialLongitude(longitude);
            initLatitude = latitude;
            initLongitude = longitude;
        }
        double distance = UtilGps.distFrom(initLatitude, initLongitude, latitude, longitude);

        if (toast==null){
            toast = Toast.makeText(this,"distance="+distance, Toast.LENGTH_LONG);
        }else{
            toast.setText("distance="+distance);
        }
        toast.show();


        if (distance>RADIO_ZONA_CONFORT){
            if (sigoFueraZonaConfort){
                //si ya estabamos fuera de la zona de confort, ya hemos mostrado la pantalla de alarma
            }else{
                sigoFueraZonaConfort = true;
                enviarMensajeFueraAreaControl();
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
        enviarPush("alerta", "Estas demasiado lejos de tu casa");
    }

    private void enviarMensajeFueraAreaControl(){
        RestClient client = new RestClient(this);
        client.alarm("fuera zona control");
        enviarPush("alerta", "Estas fuera de la zona de control");
    }


    public static void enviarPush(String titulo, String cuerpo){
        Date now = new Date();
        Notification notification = new Notification(R.drawable.ic_action_back, titulo, now.getTime());
        notification.setLatestEventInfo(Application.getContext(), titulo, cuerpo, null);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager nm = (NotificationManager) Application.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, notification);

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
