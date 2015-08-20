package com.lucascauthen.uschat.presentation.view.base;

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
