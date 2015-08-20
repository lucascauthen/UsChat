package com.lucascauthen.uschat.presentation.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.presenters.LoginPresenter;
import com.lucascauthen.uschat.presentation.view.base.LoginView;
import com.lucascauthen.uschat.util.ActivityNavigator;
import rx.Observable;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;

import javax.inject.Inject;


public class LoginActivity extends BaseActivity implements LoginView {

    @InjectView(R.id.usernameField) EditText usernameField;
    @InjectView(R.id.passwordField) EditText passwordField;
    @InjectView(R.id.loginButton) Button loginButton;
    @InjectView(R.id.signUpButton) Button signUpButton;
    @InjectView(R.id.progressBar) ProgressBar loading;

    @Inject LoginPresenter presenter;
    @Inject ActivityNavigator navigator;

    private Observable<OnTextChangeEvent> usernameObservable;
    private Observable<OnTextChangeEvent> passwordObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Butterknife initialization
        ButterKnife.inject(this);
        usernameObservable = WidgetObservable.text(usernameField);
        passwordObservable = WidgetObservable.text(passwordField);

        Observable.combineLatest(
                usernameObservable.map((username) -> testUsername(username.text().toString())),
                passwordObservable.map((password) -> testPassword(password.text().toString())),
                (user, pass) -> user && pass)
                  .subscribe((valid) -> {
                      if (valid) {
                          disableLoginControls();
                      } else {
                          enableLoginControls();
                      }
                  });

        getApplicationComponent().inject(this);

        presenter.attachView(this);
    }

    private boolean testUsername(String text) {
        return !text.equals("");
    }

    private boolean testPassword(String text) {
        return text.matches("^([a-zA-Z0-9@*#]{8,15})$");
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    public void disableAllControls() {
        loginButton.setEnabled(false);
        signUpButton.setEnabled(false);
    }

    @Override
    public void enableAllControls() {
        loginButton.setEnabled(true);
        signUpButton.setEnabled(true);
    }

    @Override
    public void disableLoginControls() {
        loginButton.setEnabled(false);
    }

    @Override
    public void enableLoginControls() {
        loginButton.setEnabled(true);
    }

    @Override
    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loading.setVisibility(View.GONE);
    }

    @Override
    public void notifyLoginFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void notifyLoginSuccess() {
        Toast.makeText(this, getString(R.string.successful_login), Toast.LENGTH_SHORT).show();
        navigator.navigateToMain(getApplicationContext());
    }

    @Override
    public String getUsername() {
        return this.usernameField.getText().toString();
    }

    @Override
    public String getPassword() {
        return this.passwordField.getText().toString();
    }

    //Button actions
    @OnClick(R.id.loginButton)
    void onButtonSignInClick() {
        presenter.tryLogin(usernameField.getText().toString(), passwordField.getText().toString());
    }

    @OnClick(R.id.signUpButton)
    void onButtonSignupClick() {
        navigator.navigateToSignUp(getApplicationContext());
    }
}
