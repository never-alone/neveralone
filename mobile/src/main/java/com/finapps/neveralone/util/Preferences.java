package com.finapps.neveralone.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.finapps.neveralone.dao.Contact;

/**
 * Created by OVIL on 24/10/2014.
 */
public class Preferences {

    private static final String NAME_PREFERENCES = "elearning_android";
    public static final String SEPARADOR="##";
    public static final String LAST_BDD_VERSION = "last_database_version";
    public static final String LMS_CONSULTED = "lms_consulted";


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
}