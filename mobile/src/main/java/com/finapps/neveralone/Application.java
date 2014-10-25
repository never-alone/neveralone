package com.finapps.neveralone;

import android.content.Context;

/**
 * Created by OVIL on 25/10/2014.
 */
public class Application extends android.app.Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        mContext = this;

        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public void onTerminate() {
		/*
		GPSWakeupSharedPreferences pref = new GPSWakeupSharedPreferences(this);
		pref.desactivarTodos();
		*/
        super.onTerminate();
    }

    public static Context getContext() {
        return mContext;
    }
}

