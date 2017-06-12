package com.lucascauthen.uschat.presentation.view.base;

public interface CameraView extends BaseView {
    void enableControls();

    void disableControls();

    void showLoading();

    void hideLoading();

    void showPictureConfirmDialog(byte[] picture);

    void showFriendSelectDialog(byte[] compressedPicture);

    void switchCameras();

    void capture();

    void loadCamera();

    void showMessage(String msg);
}
