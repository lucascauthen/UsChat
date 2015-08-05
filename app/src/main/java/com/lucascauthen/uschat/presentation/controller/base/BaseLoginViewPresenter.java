package com.lucascauthen.uschat.presentation.controller.base;


/**
 * Created by lhc on 7/30/15.
 */
public interface BaseLoginViewPresenter extends BasePresenter<BaseLoginViewPresenter.LoginView> {

    void tryLogin(String username, String password);

    void onUsernameChanged(String username);

    void onPasswordChanged(String password);

    interface LoginView extends BaseView {
        void disableAllControls();

        void enableAllControls();

        void disableLoginControls();

        void enableLoginControls();

        void showLoading();

        void hideLoading();

        void notifyLoginFailure(String error);

        void notifyLoginSuccess();

        String getUsername();

        String getPassword();
    }
}
