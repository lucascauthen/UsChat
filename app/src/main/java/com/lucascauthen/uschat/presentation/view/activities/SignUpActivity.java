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
import butterknife.OnTextChanged;
import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.controller.base.BaseSignUpViewPresenter;

import javax.inject.Inject;

public class SignUpActivity extends BaseActivity implements BaseSignUpViewPresenter.SignUpView {

    @InjectView(R.id.username) EditText usernameField;
    @InjectView(R.id.email) EditText emailField;
    @InjectView(R.id.emailAgain) EditText emailAgainField;
    @InjectView(R.id.password) EditText passwordField;
    @InjectView(R.id.passwordAgain) EditText passwordAgainField;
    @InjectView(R.id.registerButton) Button registerButton;
    @InjectView(R.id.loadingBar) ProgressBar loading;

    @Inject BaseSignUpViewPresenter presenter;


    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SignUpActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        //Butterknife initialization
        ButterKnife.inject(this);

        getApplicationComponent().inject(this);

        presenter.attachView(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    @Override
    public void disableControls() {
        registerButton.setEnabled(false);
    }

    @Override
    public void enableControls() {
        registerButton.setEnabled(true);
    }

    @Override
    public void showLoading() {
        this.loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        this.loading.setVisibility(View.GONE);
    }

    @Override
    public void notifySignUpFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void notifySignUpSuccess() {
        Toast.makeText(this, getString(R.string.regist_success_message), Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @OnClick(R.id.registerButton)
    void onRegisterClicked() {
        presenter.trySignUp(usernameField.getText().toString(), emailField.getText().toString(), passwordField.getText().toString());
    }

    //Text changed actions
    @OnTextChanged(R.id.username)
    void onUsernameChanged() {
        presenter.onUsernameChanged(usernameField.getText().toString());
    }

    @OnTextChanged(R.id.email)
    void onEmailChanged() {
        presenter.onEmailChanged(emailField.getText().toString());
    }

    @OnTextChanged(R.id.emailAgain)
    void onEmailAgainChanged() {
        presenter.onEmailAgainChanged(emailAgainField.getText().toString());
    }

    @OnTextChanged(R.id.password)
    void onPasswordChanged() {
        presenter.onPasswordChanged(passwordField.getText().toString());
    }

    @OnTextChanged(R.id.passwordAgain)
    void onPasswordAgainChanged() {
        presenter.onPasswordAgainChanged(passwordAgainField.getText().toString());
    }

    @Override
    public void sendMessage(String msg) {
        //Empty
    }
}
