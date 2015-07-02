package com.lucascauthen.uschat;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by lhc on 6/26/15.
 */
public class AndroidApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this);
    }
}
