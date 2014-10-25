package com.finapps.neveralone.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.finapps.neveralone.dao.Contact;

/**
 * Created by OVIL on 24/10/2014.
 */
public class Preferences {

    private static final String NAME_PREFERENCES = "elearning_android";


    SharedPreferences settings;

    public Preferences(Context context) {
        this.settings = context.getSharedPreferences(NAME_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void saveNameUser(String name){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("nombre_usuario", name);
        editor.commit();
    }

    public String getNameUser() {
        String nombre = settings.getString("nombre_usuario", null);
        return nombre;
    }

    public void saveContact(Contact value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("nombre", value.getNombre());
        editor.putString("mail", value.getMail());
        editor.putString("telefono", value.getTelefono());
        editor.putString("pathfoto", value.getPathFoto());
        editor.commit();
    }

    public Contact getContact() {
        String nombre = settings.getString("nombre", "");
        String mail = settings.getString("mail", "");
        String telefono = settings.getString("telefono", "");
        String pathFoto = settings.getString("pathfoto", "");
        Contact res = new Contact();
        res.setNombre(nombre);
        res.setMail(mail);
        res.setTelefono(telefono);
        res.setPathFoto(pathFoto);
        return res;
    }

    public void saveInitialLatitude(float latitude){
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("init_latitude", latitude);
        editor.commit();
    }
    public void saveInitialLongitude(float longitude){
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("init_longitude", longitude);
        editor.commit();
    }
    public float getInitialLatitude() {
        float value = settings.getFloat("init_latitude", 0);
        return value;
    }
    public float getInitialLongitude() {
        float value = settings.getFloat("init_longitude", 0);
        return value;
    }

    public void saveLastLatitude(float latitude){
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("last_latitude", latitude);
        editor.commit();
    }
    public void saveLastLongitude(float longitude){
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("last_longitude", longitude);
        editor.commit();
    }
    public float getLastLatitude() {
        float value = settings.getFloat("last_latitude", 0);
        return value;
    }
    public float getLastLongitude() {
        float value = settings.getFloat("last_longitude", 0);
        return value;
    }

    public void simularAreaConfortPekin(){
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("init_latitude_save",getInitialLatitude());
        editor.putFloat("init_longitude_save", getInitialLongitude());
        editor.putFloat("init_latitude", 40 );
        editor.putFloat("init_longitude",116);
        editor.commit();
    }

    public void simularAreaConfortPekin_deshacer(){
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("init_latitude", settings.getFloat("init_latitude_save", 0));
        editor.putFloat("init_longitude", settings.getFloat("init_longitude_save", 0));
        editor.commit();
    }
}
