package com.finapps.neveralone.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.finapps.neveralone.util.UtilBattery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by joan on 25/10/14.
 *
 *
 *
 */
public class RestClient {
    private static String URL_BASE = "http://finapps.ziguie.com/";
    private static String ACTION_ALARM    = "v1/alarm";
    private static String ACTION_TRACK    = "v1/track";
    private static String ACTION_REGISTER = "v1/register";

    private Context ctx;

    public RestClient(Context context){
        ctx=context;
    }

    private String action(String action, HashMap<String,String> params){

        StringBuilder tmpParams=new StringBuilder();
        boolean firstParam=true;
        for(String key:params.keySet()){
            if(firstParam){
                firstParam=false;
            }else{
                tmpParams.append("&");
            }
            tmpParams.append(key);
            tmpParams.append("=");
            tmpParams.append(URLEncoder.encode(params.get(key)));
        }
        StringBuilder dades=new StringBuilder();

        try {
            URL url = new URL(URL_BASE+action+"?"+tmpParams.toString());
            System.out.println(url.toString());
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                dades.append(inputLine);//.append("\n");
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dades.toString();
    }

    public void register(final String email){
        saveUserId(email);
        new Thread() {
            @Override public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                action(ACTION_REGISTER, params);
            }
        }.start();
    }

    public void track(final float batteryCharge){
        new Thread() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", getUserId());
                params.put("battery", String.valueOf(batteryCharge));
                action(ACTION_TRACK, params);
            }
        }.start();
    }

    public void alarm(final String msg){
        new Thread() {
            @Override public void run() {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put("email", getUserId());
                params.put("msg", msg);
                action(ACTION_ALARM, params);

            }
        }.start();
    }

    private void saveUserId(String id){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", id);
        editor.commit();
    }

    private String getUserId(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);

        return  sharedPreferences.getString("id", "");
    }

}
