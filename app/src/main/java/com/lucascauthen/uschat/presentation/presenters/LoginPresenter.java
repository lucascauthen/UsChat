package com.lucascauthen.uschat.presentation.presenters;

import com.lucascauthen.uschat.data.entities.User;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.view.base.LoginView;
import com.lucascauthen.uschat.util.NullObject;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginPresenter implements BasePresenter<LoginView> {

    private static final LoginView NULL_VIEW = NullObject.create(LoginView.class);
    private LoginView view = NULL_VIEW;

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;

    public LoginPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
    }

    @Override
    public void attachView(LoginView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = NULL_VIEW;
    }

    public void tryLogin(String username, String password) {
        foregroundExecutor.execute(() -> {
            view.disableAllControls();
            view.showLoading();
            backgroundExecutor.execute(() -> {
                try {
                    ParseUser.logIn(username, password);
                    foregroundExecutor.execute(() -> {
                        view.hideLoading();
                        view.enableAllControls();
                        view.notifyLoginSuccess();
                        User.login(ParseUser.getCurrentUser().getUsername());
                    });
                } catch (ParseException e) {
                    view.hideLoading();
                    view.enableAllControls();
                    view.notifyLoginFailure(e.getMessage());
                }
            });
        });
    }
}
