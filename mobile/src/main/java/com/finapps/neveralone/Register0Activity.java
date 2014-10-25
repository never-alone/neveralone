package com.finapps.neveralone;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;

import com.finapps.neveralone.services.GPSService;
import com.finapps.neveralone.util.Preferences;


public class Register0Activity extends Activity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_0);
        getActionBar().hide();

        //si ya se ha registrado, no hacemos esto y pasamos directamente a main activity
        Preferences pref = new Preferences(this);
        if (pref.getNameUser()!=null){
            startService(new Intent(Application.getContext(), GPSService.class));

            Intent nextLogica = new Intent(this, MainActivity.class);
            startActivity(nextLogica);
            finish();
        }else {
            handler = new Handler();
            handler.postDelayed(nextLogical, 2000);
        }

    }

    private Runnable nextLogical = new Runnable() {
        public void run() {
            goNextActivity();
        }
    };

    private void goNextActivity(){
        Intent nextLogica = new Intent(this, Register1Activity.class);
        startActivity(nextLogica);
        finish();
    }
}
