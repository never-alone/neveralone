package com.finapps.neveralone;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.finapps.neveralone.dao.Contact;
import com.finapps.neveralone.dao.ContactDAO;
import com.finapps.neveralone.util.Preferences;
import com.finapps.neveralone.util.UtilBattery;
import com.finapps.neveralone.util.UtilPictures;

import java.util.ArrayList;
import java.util.List;


public class AlarmActivity extends Activity {

    private static final int NOTIFICATION_ID = 1;

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


    public static void msgBateria(){

        Context ctx=Application.getContext();
        NotificationManagerCompat notificationManager;
        NotificationCompat.Builder notificationBuilder;

        List extraPages = new ArrayList();
        // creating intent to open main Activity

        // bigstyle notification pages
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        // setting title for the notification
        bigTextStyle.setBigContentTitle("Bateria baja");
        // setting content

        String texto = ctx.getString(R.string.bateryLow);
        float batery = UtilBattery.getBatteryCharge(ctx)*100;
        texto = texto.replace("#" , Float.toString(batery));
        bigTextStyle.bigText(texto);
        // creating notification
        Notification extraPageNotify1 = new NotificationCompat.Builder(Application.getContext())
                .setStyle(bigTextStyle)
                        // setting up a background image
                .setLargeIcon(
                        BitmapFactory.decodeResource(Application.getContext().getResources(),
                                R.drawable.ic_launcher)).build();
        // adding notification to the list
        extraPages.add(extraPageNotify1);


        NotificationCompat.WearableExtender wearExtender = new NotificationCompat.WearableExtender()
                //adding Collection of pages to the notification
                .addPages(extraPages)
                .setHintHideIcon(true)
                .setBackground(
                        BitmapFactory.decodeResource(ctx.getResources(),
                                R.drawable.ic_launcher));

        // Here you create the notification and start adding all the attributes
        // you are going to use
        notificationBuilder = new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(ctx.getString(R.string.tituloAtencion))
                .setContentText("Bateria baja")
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .extend(wearExtender)
        // adding action that will be launch the main activity again
        //        .addAction(android.R.drawable.ic_media_play, "GO Back",
        //                mainPendingIntent)
        ;

        // Here we instantiate the Notification Manager object to start/stop the
        // notifications
        notificationManager = NotificationManagerCompat.from(ctx);
        notificationManager.notify(NOTIFICATION_ID,
                notificationBuilder.build());
    }



    public static void msg2(){

        Context ctx=Application.getContext();
        NotificationManagerCompat notificationManager;
        NotificationCompat.Builder notificationBuilder;

        List extraPages = new ArrayList();
        // creating intent to open main Activity

            // bigstyle notification pages
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            // setting title for the notification
            bigTextStyle.setBigContentTitle("Big Title");
            // setting content
            bigTextStyle.bigText("BigText");
            // creating notification
            Notification extraPageNotify1 = new NotificationCompat.Builder(Application.getContext())
                    .setStyle(bigTextStyle)
                            // setting up a background image
                    .setLargeIcon(
                            BitmapFactory.decodeResource(Application.getContext().getResources(),
                                    R.drawable.ic_launcher)).build();
            // adding notification to the list
            extraPages.add(extraPageNotify1);

        // bigstyle notification pages
        NotificationCompat.BigTextStyle bigTextStyle2 = new NotificationCompat.BigTextStyle();
        // setting title for the notification
        bigTextStyle2.setBigContentTitle("Big Title2");
        // setting content
        bigTextStyle2.bigText("BigText2");
        // creating notification
        Notification extraPageNotify2 = new NotificationCompat.Builder(Application.getContext())
                .setStyle(bigTextStyle2)
                        // setting up a background image
                .setLargeIcon(
                        BitmapFactory.decodeResource(Application.getContext().getResources(),
                                R.drawable.ic_launcher)).build();
        // adding notification to the list
        extraPages.add(extraPageNotify2);


        NotificationCompat.WearableExtender wearExtender = new NotificationCompat.WearableExtender()
                //adding Collection of pages to the notification
                .addPages(extraPages)
                .setHintHideIcon(true)
                .setBackground(
                        BitmapFactory.decodeResource(ctx.getResources(),
                                R.drawable.ic_launcher));

        // Here you create the notification and start adding all the attributes
        // you are going to use
        notificationBuilder = new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(ctx.getString(R.string.tituloAtencion))
                .setContentText(ctx.getString(R.string.atencion_gps2))
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .extend(wearExtender)
                        // adding action that will be launch the main activity again
        //        .addAction(android.R.drawable.ic_media_play, "GO Back",
        //                mainPendingIntent)
        ;

        // Here we instantiate the Notification Manager object to start/stop the
        // notifications
        notificationManager = NotificationManagerCompat.from(ctx);
        notificationManager.notify(NOTIFICATION_ID,
                notificationBuilder.build());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
}
