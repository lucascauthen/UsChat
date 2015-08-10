package com.lucascauthen.uschat.di.components;

import com.lucascauthen.uschat.di.modules.ApplicationModule;
import com.lucascauthen.uschat.presentation.view.activities.LoginActivity;
import com.lucascauthen.uschat.presentation.view.activities.PagerActivity;
import com.lucascauthen.uschat.presentation.view.activities.SignUpActivity;
import com.lucascauthen.uschat.presentation.view.fragments.CameraFragment;
import com.lucascauthen.uschat.presentation.view.fragments.ChatTabFragment;
import com.lucascauthen.uschat.presentation.view.fragments.FriendTabFragment;
import dagger.Component;

import javax.inject.Singleton;

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
