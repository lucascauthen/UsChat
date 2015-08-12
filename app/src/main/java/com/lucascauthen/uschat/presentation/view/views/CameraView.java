package com.lucascauthen.uschat.presentation.view.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.FrameLayout;

public interface CameraView {
    void enableControls();

    void disableControls();

    void showLoading();

    void hideLoading();

    void showPictureConfirmDialog(Bitmap image);

    void closePictureConfirmDialog();

    void showFriendSelectDialog(); //This is probably not the best way to pass the data from the picture

    void onSendChatComplete();
}
