package com.finapps.neveralone.dao;

import android.content.Context;
import com.finapps.neveralone.util.Preferences;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OVIL on 24/10/2014.
 */
public class ContactDAO {

    /**
     * Todo, obtener todos los contactos de la BBDD
     * @param context
     * @return
     */
    public static List<Contact> obtenerContactos(Context context){
        ArrayList<Contact> resultado = new ArrayList<Contact>();
        Preferences pref = new Preferences(context);
        Contact contacto = pref.getContact();

        resultado.add(contacto);
        return resultado;
    }

    /**
     * Guardar el contacto en la BBDD
     * @param context
     */
    public static void guardarContacto(Context context, Contact contacto){
        Preferences pref = new Preferences(context);
        pref.saveContact(contacto);
    }

}
