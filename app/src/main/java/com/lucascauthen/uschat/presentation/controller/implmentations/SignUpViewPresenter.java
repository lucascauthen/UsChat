package com.lucascauthen.uschat.presentation.controller.implmentations;

import com.lucascauthen.uschat.presentation.controller.base.BaseSignUpViewPresenter;
import com.lucascauthen.uschat.util.NullObject;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import rx.subjects.BehaviorSubject;

/**
 * Created by lhc on 7/30/15.
 */
public class SignUpViewPresenter implements BaseSignUpViewPresenter {
    private static final SignUpView NULL_VIEW = NullObject.create(SignUpView.class);
    private SignUpView view = NULL_VIEW;

    private BehaviorSubject<String> userSubject;
    private BehaviorSubject<String> emailSubject;
    private BehaviorSubject<String> emailAgainSubject;
    private BehaviorSubject<String> passSubject;
    private BehaviorSubject<String> passAgainSubject;


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
    public void attachView(SignUpView view) {
        this.view = view;
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

    @Override
    public void trySignUp(String username, String email, String password) {
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
                    view.notifySignUpSuccess();
                } else {
                    view.notifySignUpFailure(e.getMessage());
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

    @Override
    public void onPasswordAgainChanged(String passwordAgain) {
        passAgainSubject.onNext(passwordAgain);
    }

    @Override
    public void onEmailChanged(String email) {
        emailSubject.onNext(email);
    }

    @Override
    public void onEmailAgainChanged(String emailAgain) {
        emailAgainSubject.onNext(emailAgain);
    }

    private boolean testUsername(String username) {
        return !username.equals("");
    }

    private boolean testEmail(String email) {
        return email.matches("^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$");
    }

    private boolean testPassword(String password) {
        return password.matches("^([a-zA-Z0-9@*#]{8,15})$");
    }
}
