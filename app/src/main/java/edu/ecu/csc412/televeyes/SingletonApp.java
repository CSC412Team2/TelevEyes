package edu.ecu.csc412.televeyes;

import android.app.Application;
import android.content.Context;

/**
 * Created by joshu on 10/3/2016.
 */

public class SingletonApp extends Application{
    private static SingletonApp mInstance;
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        this.setAppContext(getApplicationContext());
    }

    public static SingletonApp getInstance(){
        return mInstance;
    }
    public static Context getAppContext() {
        return mAppContext;
    }
    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
    }
}
