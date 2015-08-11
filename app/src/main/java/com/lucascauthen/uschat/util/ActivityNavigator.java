package com.lucascauthen.uschat.util;

import android.content.Context;
import android.content.Intent;
import com.lucascauthen.uschat.presentation.view.activities.LoginActivity;
import com.lucascauthen.uschat.presentation.view.activities.PagerActivity;
import com.lucascauthen.uschat.presentation.view.activities.SignUpActivity;

/**
 * Created by lhc on 6/26/15.
 */
public class ActivityNavigator {
    public void navigateToSignUp(Context context) {
        Intent intentToLaunch = SignUpActivity.getCallingIntent(context);
        context.startActivity(intentToLaunch);
    }
    public void navigateToMain(Context context) {
        Intent intentToLaunch = PagerActivity.getCallingIntent(context);
        context.startActivity(intentToLaunch);
    }
    public void navigateToLogin(Context context) {
        Intent intentToLaunch = LoginActivity.getCallingIntent(context);
        context.startActivity(intentToLaunch);
    }

}
