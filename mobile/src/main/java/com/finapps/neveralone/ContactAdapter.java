package com.finapps.neveralone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.finapps.neveralone.dao.Contact;
import com.finapps.neveralone.util.UtilPictures;

import java.util.List;

/**
 * Created by OVIL on 24/10/2014.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {
    int resource;

    List<Contact> contactos;

    public ContactAdapter(Context context, int resource, List<Contact> contactos) {
        super(context, resource, contactos);
        this.contactos = contactos;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout v = (LinearLayout) convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = (LinearLayout) vi.inflate(resource, null);
        }

        Contact elem = contactos.get(position);
        if (elem != null) {
            ((TextView) v.findViewById(R.id.nombre)).setText(elem.getNombre());
            ((TextView) v.findViewById(R.id.mail)).setText(elem.getMail());
            ((TextView) v.findViewById(R.id.telefono)).setText(elem.getTelefono());
            Bitmap picture=null;
            if (elem.getPathFoto()!=null && !elem.getPathFoto().trim().equals("")){
                picture = BitmapFactory.decodeFile(elem.getPathFoto());
            }else{
                picture = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.default_contact2);
            }
            picture = UtilPictures.getRoundedShape(picture, 150);
            ImageView imageContact = (ImageView)v.findViewById(R.id.imageContact);

            imageContact.setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
            imageContact.setImageBitmap(picture);

            v.setTag(elem.getTelefono());
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + v.getTag()));
                    getContext().startActivity(callIntent);
                }
            });

        }
        return v;
    }
}
