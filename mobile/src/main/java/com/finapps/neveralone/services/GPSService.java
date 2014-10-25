package com.finapps.neveralone.services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import com.finapps.neveralone.AlarmActivity;
import com.finapps.neveralone.Application;
import com.finapps.neveralone.R;
import com.finapps.neveralone.util.Preferences;
import com.finapps.neveralone.util.UtilGps;


/**
 * Created by OVIL on 25/10/2014.
 */
public class GPSService extends Service  {


    //cuanto mas altos sean estos numeros, menos bateria gastara este servicio
    //   encontrar unos que permitan que la app funcione correctamente y no
    //   perjudique la bateria demasiado
    private static int MIN_TIME_BW_UPDATES = 20*1000;//20 segudos
    private static int MIN_DISTANCE_CHANGE_FOR_UPDATES = 20;//10 metros

    private static int RADIO_ZONA_CONFORT = 30;
    private static int RADIO_AVISO_ZONA_CONFORT = 10;
    private static boolean sigoFueraZonaConfort = true;



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
            float latitud = Float.parseFloat(Double.toHexString(location.getLatitude()));
            float longitud = Float.parseFloat(Double.toHexString(location.getLongitude()));
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
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
        double distance = UtilGps.distFrom(initLatitude, initLongitude, latitude, longitude);

        if (distance>RADIO_ZONA_CONFORT){
            if (sigoFueraZonaConfort){


                sigoFueraZonaConfort = false;
            }else{
                //si ya estabamos fuera de la zona de confort, ya hemos mostrado la pantalla de alarma
            }
        }else if (distance>RADIO_AVISO_ZONA_CONFORT){
            sigoFueraZonaConfort = true;
            mostrarNotificacion(distance, RADIO_ZONA_CONFORT-distance);
        }else{
            sigoFueraZonaConfort = true;
        }

    }



    /**
     * Este metodo lanza una notificacion (o la modifica) en cuanto el usuario se
     *  aleje demasiado del centro de su area de confort
     * @param metrosToOrigen
     * @param metrosToFuera
     */
    private void mostrarNotificacion(double metrosToOrigen, double metrosToFuera){

        Notification note=null;
        /*
        final Notification.Builder builder = new Notification.Builder(this);
        builder.setStyle(new Notification.BigTextStyle(builder)
                .bigText("Estás demasiado lejos de tu casa. No te alejes más o avisaremos a tus familiares")
                .setBigContentTitle("Estás demasiado lejos de casa")
                .setSummaryText(subtitle))
                .setContentTitle(title)
                .setContentText(subtitle)
                .setNumber(countActivas)
                .setSmallIcon(R.drawable.ic_launcher_blank)
                .setLargeIcon(bm);
                */
        note = new Notification(R.drawable.ic_action_warning, "Estas demasiado lejos de tu casa", System.currentTimeMillis());
    }

    private void enviarMensajeFueraAreaControl(){
        Intent nextLogica = new Intent(Application.getContext(), AlarmActivity.class);
        nextLogica.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        nextLogica.putExtra("motivo", "fuera_zona_confort");
        Application.getContext().startActivity(nextLogica);
    }






}
