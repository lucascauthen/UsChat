package com.lucascauthen.uschat.presentation.navigation;

import android.content.Context;
import android.content.Intent;

import com.lucascauthen.uschat.presentation.view.activities.MainActivity;
import com.lucascauthen.uschat.presentation.view.activities.SignupActivity;

/**
 * Created by lhc on 6/26/15.
 */
public class Navigator {
    public void navigateToSignUp(Context context) {
        if(context != null) {
            Intent intentToLaunch = SignupActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }
    public void navigateToMain(Context context) {
        if(context != null) {
            Intent intentToLaunch = MainActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }
    public void navigateToLogin(Context context) {

    }
    public void navigateToChat(Context context) {

    }
}
