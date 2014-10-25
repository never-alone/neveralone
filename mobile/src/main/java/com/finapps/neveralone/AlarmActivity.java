package com.finapps.neveralone;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.finapps.neveralone.dao.Contact;
import com.finapps.neveralone.dao.ContactDAO;
import com.finapps.neveralone.util.Preferences;
import com.finapps.neveralone.util.UtilBattery;
import com.finapps.neveralone.util.UtilPictures;

import java.util.List;


public class AlarmActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        getActionBar().hide();
        String tipo ="";
        final Bundle bundle = getIntent().getExtras();
        if (bundle!=null && bundle.containsKey("tipo")){
            tipo = bundle.getString("tipo");
        }
        Preferences pref = new Preferences(this);
        if (tipo.equals("gps")){
            findViewById(R.id.layoutGPS).setVisibility(View.VISIBLE);
            List<Contact> contactos = ContactDAO.obtenerContactos(this);
            Contact contact = contactos.get(0);
            ((TextView)findViewById(R.id.nombre)).setText(contact.getNombre());
            ((TextView)findViewById(R.id.mail)).setText(contact.getMail());
            ((TextView)findViewById(R.id.telefono)).setText(contact.getTelefono());
            Bitmap picture=null;
            if (contact.getPathFoto()!=null && !contact.getPathFoto().trim().equals("")){
                picture = BitmapFactory.decodeFile(contact.getPathFoto());
            }else{
                picture = BitmapFactory.decodeResource(getResources(), R.drawable.default_contact2);
            }
            picture = UtilPictures.getRoundedShape(picture, 150);
            ImageView imageContact = (ImageView)findViewById(R.id.imageContact);

            imageContact.setBackgroundColor(getResources().getColor(R.color.transparent));
            imageContact.setImageBitmap(picture);
        }else if (tipo.equals("bateria")){
            findViewById(R.id.layoutBatery).setVisibility(View.VISIBLE);
            String texto = getString(R.string.bateryLow);
            float batery = UtilBattery.getBatteryCharge(this)*100;
            texto = texto.replace("#" , Float.toString(batery));
            ((TextView)findViewById(R.id.texto)).setText(texto);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
}
