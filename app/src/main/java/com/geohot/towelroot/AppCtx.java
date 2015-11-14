package com.geohot.towelroot;

import android.app.Application;
import android.content.Context;

public class AppCtx extends Application {

	public static Context mCtx;

    public void onCreate() {
        super.onCreate();
        mCtx = this.getApplicationContext();
    }

    public Context getAppContext() {
        return mCtx;
    }
}
