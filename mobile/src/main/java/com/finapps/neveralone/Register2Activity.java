package com.finapps.neveralone;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.finapps.neveralone.dao.Contact;
import com.finapps.neveralone.services.GPSService;
import com.finapps.neveralone.util.Preferences;


public class Register2Activity extends Activity implements LocationListener {

    private String nombre;
    private String mail;
    private String number;
    private String nombreUsu;

    private Handler handler;

    private float latitude;
    private float longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_2);
        getActionBar().hide();

        final Bundle bundle = getIntent().getExtras();
        if (bundle!=null && bundle.containsKey("nombre")){
            nombre = bundle.getString("nombre");
        }
        if (bundle!=null && bundle.containsKey("mail")){
            mail = bundle.getString("mail");
        }
        if (bundle!=null && bundle.containsKey("number")){
            number = bundle.getString("number");
        }
        if (bundle!=null && bundle.containsKey("nombreUsu")) {
            nombreUsu = bundle.getString("nombreUsu");
            ((EditText) findViewById(R.id.nombreUsu)).setText(nombreUsu);
        }
    }

    public void onToRegistro1(View v){
        nombreUsu = ((EditText)findViewById(R.id.nombreUsu)).getText().toString();

        if (nombreUsu.trim().equals("")) {
            Toast.makeText(this, getString(R.string.errorNombre), Toast.LENGTH_LONG).show();
        }else{
            Intent nextLogica = new Intent(this, Register1Activity.class);
            nextLogica.putExtra("nombre", nombre);
            nextLogica.putExtra("mail", mail);
            nextLogica.putExtra("number", number);
            nextLogica.putExtra("nombreUsu", nombreUsu);
            startActivity(nextLogica);
            finish();
        }
    }

    public void onRegister(View v){
        nombreUsu = ((EditText) findViewById(R.id.nombreUsu)).getText().toString();
        if (nombreUsu.trim().equals("")) {
            Toast.makeText(this, getString(R.string.errorNombre), Toast.LENGTH_LONG).show();
        }else {
            Preferences pref = new Preferences(this);
            Contact contacto = new Contact();
            contacto.setNombre(nombre);
            contacto.setTelefono(number);
            contacto.setMail(mail);
            pref.saveContact(contacto);
            pref.saveNameUser(nombreUsu);

            Location location = getLocation();

            if (location!=null) {
                float latitud = Float.parseFloat(location.getLatitude() + "");
                float longitud = Float.parseFloat(location.getLongitude() + "");
                pref.saveInitialLatitude(latitude);
                pref.saveInitialLongitude(longitud);
                Toast.makeText(this, "location: lat="+latitud+", long="+longitud, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "imposible obtener la locaclización", Toast.LENGTH_LONG).show();
            }
            startService(new Intent(this, GPSService.class));
            setContentView(R.layout.activity_register_3);
            handler = new Handler();
            handler.postDelayed(nextLogical, 1500);
        }
    }

    private Runnable nextLogical = new Runnable() {
        public void run() {
            //simulamos el tiempo de espera de conexión a un posible servidor
            goNextActivity();
        }
    };

    private void goNextActivity(){
        Intent nextLogica = new Intent(this, MainActivity.class);
        startActivity(nextLogica);
        finish();
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private Location getLocation() {
        try{
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                return null;
            } else {
                Location location = null;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled && location == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }
                return location;
            }
        }catch(Exception e ){
            return null;
        }

    }
}
