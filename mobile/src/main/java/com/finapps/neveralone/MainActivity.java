package com.finapps.neveralone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.finapps.neveralone.dao.Contact;
import com.finapps.neveralone.dao.ContactDAO;
import com.finapps.neveralone.util.Preferences;

import java.util.List;


public class MainActivity extends Activity {

    ContactAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getActionBar().hide();
        getActionBar().setIcon(null);
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.main_bar));

        List<Contact> contactos = ContactDAO.obtenerContactos(this);
        adapter = new ContactAdapter(this, R.layout.row_contact, contactos);
        ListView listaContactos = (ListView)findViewById(R.id.listacontactos);
        listaContactos.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        Preferences pref = new Preferences(this);
        //if (pref.getInitialLatitude()==40){
            //simulamos que estamos en pekin
            menu.removeGroup(R.id.action_out_gps);
        //}else{
            menu.removeGroup(R.id.action_in_gps);
        //}

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Preferences pref = new Preferences(this);
        int id = item.getItemId();
        if (id == R.id.action_descon_wear) {
            showDialog(1);
            return true;
        }else if (id == R.id.action_out_gps) {
            pref.simularAreaConfortPekin();
            //invalidateOptionsMenu();
            Register0Activity.arrancarServiciosGPS();
            Toast.makeText(this, getString(R.string.developer_menu_simular_mail_gps_expl), Toast.LENGTH_LONG).show();
        }else if (id == R.id.action_in_gps){
            pref.simularAreaConfortPekin_deshacer();
            invalidateOptionsMenu();
        }else if (id == R.id.action_bateria){
            pref.siumlarNoBateria();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id==1){
            return new AlertDialog.Builder(this)
                    .setTitle(R.string.developer_menu_simular_perder_wear)
                    .setMessage(R.string.developer_menu_simular_perder_wear_expl).create();
        } else {
            return super.onCreateDialog(id);
        }
    }

}
