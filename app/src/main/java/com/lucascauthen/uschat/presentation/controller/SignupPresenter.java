package com.lucascauthen.uschat.presentation.controller;

import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Observable;
import java.util.concurrent.Executor;

import rx.Scheduler;
import rx.subjects.BehaviorSubject;

/**
 * Created by lhc on 6/26/15.
 */
public class SignupPresenter implements Presenter {


    private final Scheduler backgroundScheduler;
    private final Scheduler foregroundScheduler;
    private final Executor backgroundExecutor;
    private final Executor foregroundExecutor;

    private static final NullSignupCrudView NULL_VIEW = new NullSignupCrudView();
    private SignupCrudView view = NULL_VIEW;

    private BehaviorSubject<String> userSubject;
    private BehaviorSubject<String> emailSubject;
    private BehaviorSubject<String> emailAgainSubject;
    private BehaviorSubject<String> passSubject;
    private BehaviorSubject<String> passAgainSubject;

    public SignupPresenter(Scheduler backgroundScheduler, Scheduler foregroundScheduler, Executor backgroundExecutor, Executor foregroundExecutor) {
        this.backgroundScheduler = backgroundScheduler;
        this.foregroundScheduler = foregroundScheduler;
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
    }

    public void attachView(SignupCrudView v) {
        this.view = v;
        userSubject = BehaviorSubject.create("");
        emailSubject = BehaviorSubject.create("");
        emailAgainSubject = BehaviorSubject.create("");
        passSubject = BehaviorSubject.create("");
        passAgainSubject = BehaviorSubject.create("");

        BehaviorSubject.combineLatest(
                //Test username
                userSubject.map(s -> testUsername(s)),
                //Test emails
                BehaviorSubject.combineLatest(emailSubject, emailAgainSubject, (a, b) -> {
                    if (a.equals(b)) {
                        return a;
                    } else {
                        return false;
                    }
                }).map(s -> {
                    return s.getClass().equals(String.class) && testEmail((String) s);
                }),
                //Test passwords
                BehaviorSubject.combineLatest(passSubject, passAgainSubject, (a, b) -> {
                    if (a.equals(b)) {
                        return a;
                    } else {
                        return false;
                    }
                }).map(s -> {
                    return s.getClass().equals(String.class) && testPassword((String) s);
                }),

                (a, b, c) -> {
                    //Combine the results
                    return a && b && c;
                }).subscribe(valid -> {
            if (valid) {
                view.enableControls();
            } else {
                view.disableControls();
            }
        });

    }

    public void detachView() {
        this.view = NULL_VIEW;
    }

    public void present() {

    }

    public void trySignup(String username, String email, String password) {
        view.showLoading();
        view.disableControls();
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                view.hideLoading();
                view.enableControls();
                if (e == null) {
                    //Registered Successful
                    view.notifySignupSuccess();
                } else {
                    view.notifySignupFailure(e.getMessage());
                }
            }
        });
    }

    public void loginFailed(String message) {
        view.hideLoading();
        view.enableControls();
        view.notifySignupFailure(message);
    }

    public void loginSuccess() {
        view.hideLoading();
        view.enableControls();
        view.notifySignupSuccess();
    }

    public void onUsernameChanged(String username) {
        userSubject.onNext(username);
    }

    public void onEmailChanged(String email) {
        emailSubject.onNext(email);
    }

    public void onEmailAgainChanged(String emailAgain) {
        emailAgainSubject.onNext(emailAgain);
    }

    public void onPasswordChanged(String password) {
        passSubject.onNext(password);
    }

    public void onPasswordAgainChanged(String passwordAgain) {
        passAgainSubject.onNext(passwordAgain);
    }

    //Validation tests:
    public boolean testUsername(String username) {
        return !username.equals("");
    }

    public boolean testEmail(String email) {
        return email.matches("^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$");
    }

    public boolean testPassword(String password) {
        return password.matches("^([a-zA-Z0-9@*#]{8,15})$");
    }

    public interface SignupCrudView {

        void disableControls();

        void enableControls();

        void showLoading();

        void hideLoading();

        void notifySignupFailure(String error);

        void notifySignupSuccess();

    }

    public static class NullSignupCrudView implements SignupCrudView {

        @Override
        public void disableControls() {

        }

        @Override
        public void enableControls() {

        }

        @Override
        public void showLoading() {

        }

        @Override
        public void hideLoading() {

        }

        @Override
        public void notifySignupFailure(String error) {

        }

        @Override
        public void notifySignupSuccess() {

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
