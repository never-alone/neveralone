package com.finapps.neveralone;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.finapps.neveralone.util.Preferences;
import com.finapps.neveralone.util.UtilBattery;

public class AlarmBateryActivity extends Activity {

    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        getActionBar().hide();

        Preferences pref = new Preferences(this);
        findViewById(R.id.layoutBatery).setVisibility(View.VISIBLE);
        String texto = getString(R.string.bateryLow);
        float batery = UtilBattery.getBatteryCharge(this)*100;
        texto = texto.replace("#" , Float.toString(batery));
        ((TextView)findViewById(R.id.texto)).setText(texto);

    }
}
