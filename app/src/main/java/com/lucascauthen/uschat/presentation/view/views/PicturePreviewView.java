package com.lucascauthen.uschat.presentation.view.views;

import android.graphics.Bitmap;

public interface PicturePreviewView {
    void showLoading();

    void hideLoading();

    interface OnAcceptPictureListener {
        void onAccept(byte[] data);
    }

    interface OnRejectPictureListener {
        void onReject();
    }
}
