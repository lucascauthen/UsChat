package com.lucascauthen.uschat.presentation.controller;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.concurrent.Executor;

import rx.Observable;
import rx.Scheduler;
import rx.subjects.BehaviorSubject;

/**
 * Created by lhc on 6/25/15.
 */
public class LoginPresenter implements Presenter {
    private final Scheduler backgroundScheduler;
    private final Scheduler foregroundScheduler;
    private final Executor backgroundExecutor;
    private final Executor foregroundExecutor;
    private BehaviorSubject<String> userSubject;
    private BehaviorSubject<String> passSubject;

    private static final NullLoginCrudView NULL_VIEW = new NullLoginCrudView();
    private LoginCrudView view = NULL_VIEW;

    public LoginPresenter(Scheduler backgroundScheduler, Scheduler foregroundScheduler, Executor backgroundExecutor, Executor foregroundExecutor) {
        this.backgroundScheduler = backgroundScheduler;
        this.foregroundScheduler = foregroundScheduler;
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.userSubject = BehaviorSubject.create();
        this.passSubject = BehaviorSubject.create();
    }

    public void attachView(LoginCrudView v) {
        this.view = v;
        userSubject = BehaviorSubject.create(view.getUsername());
        passSubject = BehaviorSubject.create(view.getPassword());
        Observable.combineLatest(
                userSubject.map(s -> testUsername(s)),
                passSubject.map(s -> testPassword(s)),
                (a, b) -> {
                    return a && b;
                }).subscribe(valid -> {
            if (valid) {
                view.enableLoginControls();
            } else {
                view.disableLoginControls();
            }
        });
    }

    public void detachView() {
        this.view = NULL_VIEW;
    }

    public void present() {

    }

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
                } else {
                    view.notifyLoginFailure(e.getMessage());
                }
            }
        });

    }

    public void onUsernameChanged(String text) {
        userSubject.onNext(text);
    }

    public void onPasswordChanged(String text) {
        passSubject.onNext(text);
    }

    public boolean testUsername(String text) {
        return !text.equals("");
    }

    public boolean testPassword(String text) {
        return text.matches("^([a-zA-Z0-9@*#]{8,15})$");
    }

    public interface LoginCrudView {

        void disableAllControls();

        void enableAllControls();

        void disableLoginControls();

        void enableLoginControls();

        void showLoading();

        void hideLoading();

        void notifyLoginFailure(String error);

        void notifyLoginSuccess();

        String getUsername();

        String getPassword();
    }

    public static class NullLoginCrudView implements LoginCrudView {

        @Override
        public void disableAllControls() {

        }

        @Override
        public void enableAllControls() {

        }

        @Override
        public void disableLoginControls() {

        }

        @Override
        public void enableLoginControls() {

        }

        @Override
        public void showLoading() {

        }

        @Override
        public void hideLoading() {

        }

        @Override
        public void notifyLoginFailure(String error) {

        }

        @Override
        public void notifyLoginSuccess() {

        }

        @Override
        public String getUsername() {
            return "";
        }

        @Override
        public String getPassword() {
            return "";
        }

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
