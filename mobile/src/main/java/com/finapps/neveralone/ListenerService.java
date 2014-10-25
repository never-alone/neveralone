package com.finapps.neveralone;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.AsyncTask;

import com.finapps.neveralone.net.RestClient;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Listens for disconnection from home device.
 */
public class ListenerService extends WearableListenerService {


    private static final int FORGOT_PHONE_NOTIFICATION_ID = 1;

    static TimerTask timerTask;
    boolean disconnected=false;

    @Override
    public void onPeerDisconnected(com.google.android.gms.wearable.Node peer) {

        if(timerTask!=null){
            //timerTask.cancel(true);
            return;
        }

        disconnected=true;

        timerTask=new TimerTask();
        timerTask.execute();
    }

    @Override
    public void onPeerConnected(com.google.android.gms.wearable.Node peer) {

        disconnected=false;
        synchronized(this) {
            if (timerTask != null){
                //timerTask.segons=-1;
                timerTask.cancel(false);
            }
        }

        timerTask=null;
        // Remove the "forgot phone" notification when connection is restored.
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                .cancel(FORGOT_PHONE_NOTIFICATION_ID);
    }



    class TimerTask extends AsyncTask<Void, Void, Void>{

        int segons;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            segons=100;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            if(!ListenerService.this.disconnected) return;

            String s=segons>0?String.valueOf(segons):getString(R.string.wearable_disconnected_title);

            // Create a "forgot phone" notification when phone connection is broken.
            Notification.Builder notificationBuilder = new Notification.Builder(ListenerService.this)
                    .setContentText(s)
                    //.setContentTitle(getString(R.string.wearable_disconnected_title))
                    .setContentTitle(getString(R.string.wearable_disconnected_content))
                    .setVibrate(new long[] {0, 200})  // Vibrate for 200 milliseconds.
                    .setSmallIcon(R.drawable.ic_launcher)
                            //.setLocalOnly(true)
                    .setPriority(Notification.PRIORITY_MAX);
            Notification card = notificationBuilder.build();
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                    .notify(FORGOT_PHONE_NOTIFICATION_ID, card);
        }

        @Override
        protected Void doInBackground(Void... params) {

            while (!this.isCancelled() && segons > 0 && ListenerService.this.disconnected) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                segons--;
                this.publishProgress();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(!isCancelled()){
                RestClient client = new RestClient(ListenerService.this);
                client.alarm(getString(R.string.wearable_disconnected_content));
            }
            ListenerService.this.timerTask=null;
        }
    }
}
