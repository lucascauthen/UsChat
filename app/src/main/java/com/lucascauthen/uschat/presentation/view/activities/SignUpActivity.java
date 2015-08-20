package com.lucascauthen.uschat.presentation.view.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.presenters.SignUpPresenter;
import com.lucascauthen.uschat.presentation.view.base.SignUpView;
import rx.Observable;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;

import javax.inject.Inject;

public class SignUpActivity extends BaseActivity implements SignUpView {

    @InjectView(R.id.username) EditText usernameField;
    @InjectView(R.id.email) EditText emailField;
    @InjectView(R.id.emailAgain) EditText emailAgainField;
    @InjectView(R.id.password) EditText passwordField;
    @InjectView(R.id.passwordAgain) EditText passwordAgainField;
    @InjectView(R.id.registerButton) Button registerButton;
    @InjectView(R.id.loadingBar) ProgressBar loading;

    @Inject SignUpPresenter presenter;

    private Observable<OnTextChangeEvent> usernameObservable;
    private Observable<OnTextChangeEvent> passwordObservable;
    private Observable<OnTextChangeEvent> passwordAgainObservable;
    private Observable<OnTextChangeEvent> emailObservable;
    private Observable<OnTextChangeEvent> emailAgainObservable;


    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SignUpActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_signup);

        //Butterknife initialization
        ButterKnife.inject(this);

        initObservables();

        Observable.combineLatest(
                //Test username
                usernameObservable.map(s -> testUsername(s.text().toString())),
                //Test emails
                Observable.combineLatest(emailObservable, emailAgainObservable, (a, b) -> {
                    if (a.text().toString().equals(b.text().toString())) {
                        return a.text().toString();
                    } else {
                        return false;
                    }
                }).map(s -> {
                    return s.getClass().equals(String.class) && testEmail((String) s);
                }),
                //Test passwords
                Observable.combineLatest(passwordObservable, passwordAgainObservable, (a, b) -> {
                    if (a.text().toString().equals(b.text().toString())) {
                        return a.text().toString();
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
                enableControls();
            } else {
                disableControls();
            }
        });

        initFields();

        getApplicationComponent().inject(this);

        presenter.attachView(this);
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

    private void initObservables() {
        usernameObservable = WidgetObservable.text(usernameField);

        emailObservable = WidgetObservable.text(emailField);

        emailAgainObservable = WidgetObservable.text(emailAgainField);

        passwordObservable = WidgetObservable.text(passwordField);

        passwordAgainObservable = WidgetObservable.text(passwordAgainField);
    }

    private void initFields() {
        usernameField.setText("");
        emailField.setText("");
        emailAgainField.setText("");
        passwordField.setText("");
        passwordAgainField.setText("");
    }
}
