package com.lucascauthen.uschat.presentation.view.activities;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.lucascauthen.uschat.presentation.controller.LoginPresenter;
import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.navigation.Navigator;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class LoginActivity extends AppCompatActivity implements LoginPresenter.LoginCrudView{

    @InjectView(R.id.login_username_field) EditText usernameField;
    @InjectView(R.id.login_password_field) EditText passwordField;
    @InjectView(R.id.login_signin_button)Button loginButton;
    @InjectView(R.id.login_signup_button) Button signupButton;
    @InjectView(R.id.login_loading)ProgressBar loading;

    private LoginPresenter presenter;

    private Navigator navigator = new Navigator();

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        //Butterknife initialization
        ButterKnife.inject(this);

        Executor backgroundExecutor = Executors.newFixedThreadPool(10);

        final Handler handler = new Handler();
        Executor foregroundExecutor = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
        presenter = new LoginPresenter(Schedulers.io(), AndroidSchedulers.mainThread(), backgroundExecutor, foregroundExecutor);

        presenter.attachView(this);
        presenter.present();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void disableAllControls() {
        loginButton.setEnabled(false);
        signupButton.setEnabled(false);
    }

    @Override
    public void enableAllControls() {
        loginButton.setEnabled(true);
        signupButton.setEnabled(true);
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
}
