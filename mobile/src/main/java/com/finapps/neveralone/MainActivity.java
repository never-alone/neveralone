package com.finapps.neveralone;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.finapps.neveralone.dao.Contact;
import com.finapps.neveralone.dao.ContactDAO;

import java.util.List;


public class MainActivity extends Activity {

    ContactAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();

        List<Contact> contactos = ContactDAO.obtenerContactos(this);
        adapter = new ContactAdapter(this, R.layout.row_contact, contactos);
        ListView listaContactos = (ListView)findViewById(R.id.listacontactos);
        listaContactos.setAdapter(adapter);
    }

}
