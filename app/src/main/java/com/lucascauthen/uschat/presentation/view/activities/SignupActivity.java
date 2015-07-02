package com.lucascauthen.uschat.presentation.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.controller.LoginPresenter;
import com.lucascauthen.uschat.presentation.controller.SignupPresenter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignupActivity extends AppCompatActivity implements SignupPresenter.SignupCrudView{

    @InjectView(R.id.register_username)EditText usernameField;
    @InjectView(R.id.register_email)EditText emailField;
    @InjectView(R.id.register_email_again)EditText emailAgainField;
    @InjectView(R.id.register_password)EditText passwordField;
    @InjectView(R.id.register_password_again)EditText passwordAgainField;
    @InjectView(R.id.register_button)Button registerButton;
    @InjectView(R.id.register_loading)ProgressBar loading;

    private SignupPresenter presenter;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SignupActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_signup);
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
        presenter = new SignupPresenter(Schedulers.io(), AndroidSchedulers.mainThread(), backgroundExecutor, foregroundExecutor);

        presenter.attachView(this);
        presenter.present();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    public void notifySignupFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void notifySignupSuccess() {
        Toast.makeText(this, getString(R.string.regist_success_message), Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @OnClick(R.id.register_button)
    void onRegisterClicked() {
        presenter.trySignup(usernameField.getText().toString(), emailField.getText().toString(), passwordField.getText().toString());
    }

    //Text changed actions
    @OnTextChanged(R.id.register_username)
    void onUsernameChanged() {
        presenter.onUsernameChanged(usernameField.getText().toString());
    }

    @OnTextChanged(R.id.register_email)
    void onEmailChanged() {
        presenter.onEmailChanged(emailField.getText().toString());
    }

    @OnTextChanged(R.id.register_email_again)
    void onEmailAgainChanged() {
        presenter.onEmailAgainChanged(emailAgainField.getText().toString());
    }

    @OnTextChanged(R.id.register_password)
    void onPasswordChanged() {
        presenter.onPasswordChanged(passwordField.getText().toString());
    }

    @OnTextChanged(R.id.register_password_again)
    void onPasswordAgainChanged() {
        presenter.onPasswordAgainChanged(passwordAgainField.getText().toString());
    }
}
