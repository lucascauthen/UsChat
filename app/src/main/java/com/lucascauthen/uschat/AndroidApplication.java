package com.lucascauthen.uschat;

import android.app.Application;
import com.lucascauthen.uschat.di.components.ApplicationComponent;
import com.lucascauthen.uschat.di.components.DaggerApplicationComponent;
import com.lucascauthen.uschat.di.modules.ApplicationModule;
import com.parse.Parse;


/**
 * Created by lhc on 6/26/15.
 */
public class AndroidApplication extends Application {

    private ApplicationComponent applicationComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
        Parse.initialize(this);

    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                                                              .applicationModule(new ApplicationModule(this))
                                                              .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }

}
