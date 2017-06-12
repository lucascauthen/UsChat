package com.lucascauthen.uschat.presentation.presenters;

import com.lucascauthen.uschat.util.executor.BackgroundExecutor;
import com.lucascauthen.uschat.util.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.view.base.SignUpView;
import com.lucascauthen.uschat.util.NullObject;
import com.parse.ParseException;
import com.parse.ParseUser;

public class SignUpPresenter implements BasePresenter<SignUpView> {

    private static final SignUpView NULL_VIEW = NullObject.create(SignUpView.class);
    private SignUpView view = NULL_VIEW;

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;

    public SignUpPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
    }

    @Override
    public void attachView(SignUpView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = NULL_VIEW;
    }
    public void trySignUp(String username, String email, String password) {
        foregroundExecutor.execute(() -> {
            view.showLoading();
            view.disableControls();
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            backgroundExecutor.execute(() -> {
                try {
                    user.signUp();
                    //Success
                    foregroundExecutor.execute(() -> {
                        view.hideLoading();
                        view.enableControls();
                        view.notifySignUpSuccess();
                    });
                } catch (ParseException e) {
                    //Failure
                    foregroundExecutor.execute(() -> {
                        view.hideLoading();
                        view.enableControls();
                        view.notifySignUpFailure(e.getMessage());
                    });
                }
            });
        });
    }
}
