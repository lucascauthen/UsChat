package com.lucascauthen.uschat.presentation.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.controller.base.BaseLoginViewPresenter;
import com.lucascauthen.uschat.util.ActivityNavigator;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;


public class LoginActivity extends BaseActivity implements BaseLoginViewPresenter.LoginView {

    @InjectView(R.id.login_username_field) EditText usernameField;
    @InjectView(R.id.login_password_field) EditText passwordField;
    @InjectView(R.id.login_signin_button) Button loginButton;
    @InjectView(R.id.login_signup_button) Button signUpButton;
    @InjectView(R.id.login_loading) ProgressBar loading;

    @Inject BaseLoginViewPresenter presenter;
    @Inject
    ActivityNavigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        //Butterknife initialization
        ButterKnife.inject(this);

        getApplicationComponent().inject(this);

        presenter.attachView(this);
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
    @OnClick(R.id.login_signin_button)
    void onButtonSignInClick() {
        presenter.tryLogin(usernameField.getText().toString(), passwordField.getText().toString());
    }

    @OnClick(R.id.login_signup_button)
    void onButtonSignupClick() {
        navigator.navigateToSignUp(getApplicationContext());
    }

    //Text field actions
    @OnTextChanged(R.id.login_username_field)
    void onUsernameChanged() {
        presenter.onUsernameChanged(usernameField.getText().toString());
    }

    @OnTextChanged(R.id.login_password_field)
    void onPasswordChanged() {
        presenter.onPasswordChanged(passwordField.getText().toString());
    }

    @Override
    public void sendMessage(String msg) {
        //Empty
    }
}
