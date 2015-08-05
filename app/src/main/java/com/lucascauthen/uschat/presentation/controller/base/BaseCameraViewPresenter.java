package com.lucascauthen.uschat.presentation.controller.base;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.view.SurfaceView;

import com.lucascauthen.uschat.data.entities.User;

import java.util.List;

/**
 * Created by lhc on 7/30/15.
 */
public interface BaseCameraViewPresenter extends BasePresenter<BaseCameraViewPresenter.CameraView>, BasePagerViewPresenter.PagerSubView {
    void onDoubleTap();

    void attachPreview(CameraPreview preview);

    void onTryCapture();

    void onSendChat(List<User> people);

    void onAcceptPicture();

    interface CameraView extends BaseView {
        void enableControls();

        void disableControls();

        void showLoading();

        void hideLoading();

        void showPictureConfirmDialog(Bitmap image);

        void showFriendSelectDialog();

        void onSendChatComplete();
    }
    interface CameraPreview {
        void attachCamera(Camera camera);

        SurfaceView getView();
    }
}
