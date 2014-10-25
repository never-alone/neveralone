package com.finapps.neveralone;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.finapps.neveralone.dao.Contact;
import com.finapps.neveralone.dao.ContactDAO;
import com.finapps.neveralone.util.Preferences;
import com.finapps.neveralone.util.UtilPictures;

import java.util.List;


public class AlarmGpsActivity extends Activity {

    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        getActionBar().hide();

        Preferences pref = new Preferences(this);

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
    }
}
