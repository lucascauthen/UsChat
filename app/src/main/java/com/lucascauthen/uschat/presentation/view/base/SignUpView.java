package com.lucascauthen.uschat.presentation.view.base;

public interface SignUpView {
    void disableControls();

    void enableControls();

    void showLoading();

    void hideLoading();

    void notifySignUpFailure(String error);

    void notifySignUpSuccess();
}
