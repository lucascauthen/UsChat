package com.lucascauthen.uschat.presentation.controller.base;

/**
 * Created by lhc on 7/30/15.
 */
public interface BaseSignUpViewPresenter extends BasePresenter<BaseSignUpViewPresenter.SignUpView> {

    void trySignUp(String username, String email, String password);

    void onUsernameChanged(String username);

    void onPasswordChanged(String password);

    void onPasswordAgainChanged(String passwordAgain);

    void onEmailChanged(String email);

    void onEmailAgainChanged(String emailAgain);

    interface SignUpView extends BaseView {
        void disableControls();

        void enableControls();

        void showLoading();

        void hideLoading();

        void notifySignUpFailure(String error);

        void notifySignUpSuccess();
    }
}
