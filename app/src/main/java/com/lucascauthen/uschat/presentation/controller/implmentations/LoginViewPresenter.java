package com.lucascauthen.uschat.presentation.controller.implmentations;

import com.lucascauthen.uschat.data.entities.User;
import com.lucascauthen.uschat.presentation.controller.base.BaseLoginViewPresenter;
import com.lucascauthen.uschat.util.NullObject;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import rx.Observable;
import rx.Scheduler;
import rx.subjects.BehaviorSubject;

/**
 * Created by lhc on 7/30/15.
 */
public class LoginViewPresenter implements BaseLoginViewPresenter {
    private final Scheduler foregroundScheduler;
    private BehaviorSubject<String> userSubject;
    private BehaviorSubject<String> passSubject;

    private static final LoginView NULL_VIEW = NullObject.create(LoginView.class);
    private LoginView view = NULL_VIEW;

    public LoginViewPresenter(Scheduler foregroundScheduler) {
        this.foregroundScheduler = foregroundScheduler;
        this.userSubject = BehaviorSubject.create();
        this.passSubject = BehaviorSubject.create();
    }

    @Override
    public void detachView() {
        this.view = NULL_VIEW;
    }

    @Override
    public void onPause() {
        //Empty
    }

    @Override
    public void onResume() {
        //Empty
    }

    @Override
    public void attachView(LoginView view) {
        this.view = view;
        userSubject = BehaviorSubject.create(view.getUsername());
        passSubject = BehaviorSubject.create(view.getPassword());
        Observable.combineLatest(
                userSubject.map(s -> testUsername(s)),
                passSubject.map(s -> testPassword(s)),
                (a, b) -> {
                    return a && b;
                }).subscribeOn(foregroundScheduler).subscribe(valid -> {
            if (valid) {
                view.enableLoginControls();
            } else {
                view.disableLoginControls();
            }
        });
        if (ParseUser.getCurrentUser() != null) {
            view.notifyLoginSuccess();
            User.login(ParseUser.getCurrentUser().getUsername());
        }
    }

    public boolean testUsername(String text) {
        return !text.equals("");
    }

    public boolean testPassword(String text) {
        return text.matches("^([a-zA-Z0-9@*#]{8,15})$");
    }

    @Override
    public void tryLogin(String username, String password) {
        view.disableAllControls();
        view.showLoading();
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                view.enableAllControls();
                view.hideLoading();
                if (parseUser != null) {
                    view.notifyLoginSuccess();
                    User.login(ParseUser.getCurrentUser().getUsername());
                } else {
                    view.notifyLoginFailure(e.getMessage());
                }
            }
        });
    }

    @Override
    public void onUsernameChanged(String username) {
        userSubject.onNext(username);
    }

    @Override
    public void onPasswordChanged(String password) {
        passSubject.onNext(password);
    }

}
