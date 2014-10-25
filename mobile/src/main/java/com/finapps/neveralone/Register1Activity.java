package com.finapps.neveralone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class Register1Activity extends Activity {

    private String nombre;
    private String mail;
    private String number;
    private String nombreUsu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_1);
        getActionBar().hide();

        final Bundle bundle = getIntent().getExtras();
        if (bundle!=null && bundle.containsKey("nombre")){
            nombre = bundle.getString("nombre");
            ((EditText) findViewById(R.id.nombre)).setText(nombre);
        }
        if (bundle!=null && bundle.containsKey("mail")){
            mail = bundle.getString("mail");
            ((EditText) findViewById(R.id.mail)).setText(mail);
        }
        if (bundle!=null && bundle.containsKey("number")){
            number = bundle.getString("number");
            ((EditText) findViewById(R.id.telefono)).setText(number);
        }
        if (bundle!=null && bundle.containsKey("nombreUsu")) {
            nombreUsu = bundle.getString("nombreUsu");
        }
    }

    public void onToRegistro2(View v){
        nombre = ((EditText)findViewById(R.id.nombre)).getText().toString();
        mail = ((EditText)findViewById(R.id.mail)).getText().toString();
        number = ((EditText)findViewById(R.id.telefono)).getText().toString();
        number = number.replace(" ", "");
        if (nombre.trim().equals("")) {
            Toast.makeText(this, getString(R.string.errorNombre), Toast.LENGTH_LONG).show();
        }else if (!isValidEmailAddress(mail)){
            Toast.makeText(this, getString(R.string.errorMail), Toast.LENGTH_LONG).show();
        }else if (!isValidPhoneNumber(number)){
            Toast.makeText(this, getString(R.string.errorTelefono), Toast.LENGTH_LONG).show();
        }else {
            Intent nextLogica = new Intent(this, Register2Activity.class);
            nextLogica.putExtra("nombre", nombre);
            nextLogica.putExtra("mail", mail);
            nextLogica.putExtra("number", number);
            if (nombreUsu!=null && !nombreUsu.trim().equals(""))
                nextLogica.putExtra("nombreUsu", nombreUsu);
            startActivity(nextLogica);
            finish();
        }
    }

    private static boolean isValidEmailAddress(String email) {
        String emailreg = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return email.matches(emailreg);
    }

    private static boolean isValidPhoneNumber(String phoneNumber) {
        try{
            Integer.parseInt(phoneNumber);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }

}
