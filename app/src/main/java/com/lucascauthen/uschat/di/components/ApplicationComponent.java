package com.lucascauthen.uschat.di.components;

import com.lucascauthen.uschat.di.modules.ApplicationModule;
import com.lucascauthen.uschat.presentation.view.activities.LoginActivity;
import com.lucascauthen.uschat.presentation.view.activities.PagerActivity;
import com.lucascauthen.uschat.presentation.view.activities.SignUpActivity;

import com.lucascauthen.uschat.presentation.view.fragments.newfrag.CameraFragment;
import com.lucascauthen.uschat.presentation.view.fragments.newfrag.ChatTabFragment;
import com.lucascauthen.uschat.presentation.view.fragments.newfrag.FriendTabFragment;


import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by lhc on 7/29/15.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(LoginActivity loginActivity);

    void inject(PagerActivity mainActivity);

    void inject(SignUpActivity signupActivity);

    void inject(CameraFragment cameraFragment);

    void inject(ChatTabFragment chatTabFragment);

    void inject(FriendTabFragment friendTabFragment);

}
