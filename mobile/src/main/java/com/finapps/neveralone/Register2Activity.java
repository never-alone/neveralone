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


public class Register2Activity extends Activity{

    private String nombre;
    private String mail;
    private String number;
    private String nombreUsu;

    private Handler handler;

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

            Register0Activity.arrancarServiciosGPS();
            Register0Activity.arrancarServiciosBatery(this);

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
}
