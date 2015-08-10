package com.lucascauthen.uschat.presentation.view.activities;

import android.support.v7.app.AppCompatActivity;
import com.lucascauthen.uschat.AndroidApplication;
import com.lucascauthen.uschat.di.components.ApplicationComponent;

/**
 * Created by lhc on 7/30/15.
 */
public abstract class BaseActivity extends AppCompatActivity{

    protected ApplicationComponent getApplicationComponent() {
        return ((AndroidApplication)getApplication()).getApplicationComponent();
    }

}
