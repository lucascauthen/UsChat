package com.lucascauthen.uschat.presentation.presenters;

import com.lucascauthen.uschat.util.executor.BackgroundExecutor;
import com.lucascauthen.uschat.util.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.view.base.CameraView;
import com.lucascauthen.uschat.util.NullObject;


public class CameraPresenter implements BasePresenter<CameraView>{
    private static final CameraView NULL_VIEW = NullObject.create(CameraView.class);

    private CameraView view = NULL_VIEW;


    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;

    private boolean hasCamera = false;
    private boolean hasPermission = false;

    public CameraPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
    }

    public void onSwitchCameras() {
        if(hasCamera) {
            foregroundExecutor.execute(() -> {
                view.showLoading();
                view.disableControls();
                backgroundExecutor.execute(() -> {
                    view.switchCameras();
                    view.loadCamera();
                    foregroundExecutor.execute(() -> {
                        view.hideLoading();
                        view.enableControls();
                    });
                });
            });
        } else {
            view.showMessage("Can't switch cameras because you don't have a camera!");
        }
    }

    public void onAcceptPicture(byte[] compressedPicture) {
        view.showFriendSelectDialog(compressedPicture);
    }

    public void attachView(CameraView view) {
        this.view = view;
    }

    public void detachView() {
        this.view = NULL_VIEW;
    }

    public void onPictureTaken(byte[] data) {
        foregroundExecutor.execute(() -> {
            view.hideLoading();
            view.showPictureConfirmDialog(data);
            view.enableControls();
        });
    }

    public void onBeforeCapture() {
        if(hasCamera) {
            foregroundExecutor.execute(() -> {
                view.showLoading();
                view.disableControls();
                backgroundExecutor.execute(() -> {
                    view.capture();
                });
            });
        } else {
            view.showMessage("Unable to take picture because your phone does not have a camera!");
        }
    }

    public void onLoadCamera() {
        foregroundExecutor.execute(() -> {
            view.showLoading();
            view.disableControls();
            backgroundExecutor.execute(() -> {
                view.loadCamera();
            });
        });
    }

    public void onCameraLoaded() {
        foregroundExecutor.execute(() -> {
            view.hideLoading();
            view.enableControls();
        });
    }

    public void onCaptureError(String msg) {
        foregroundExecutor.execute(() -> {
            view.hideLoading();
            view.enableControls();
            view.showMessage(msg);
        });
    }

    public void setHasCamera(boolean hasCamera) {
        this.hasCamera = hasCamera;
    }
}
