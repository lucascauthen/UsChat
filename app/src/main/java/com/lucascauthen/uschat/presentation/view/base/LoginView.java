package com.lucascauthen.uschat.presentation.view.base;

public interface LoginView {
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
