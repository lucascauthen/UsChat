package com.lucascauthen.uschat.presentation.presenters;

import android.graphics.Bitmap;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.controller.base.BasePagerViewPresenter;
import com.lucascauthen.uschat.presentation.view.views.CameraView;
import com.lucascauthen.uschat.util.NullObject;


public class CameraPresenter implements BasePresenter<CameraView>{
    private static final CameraView NULL_VIEW = NullObject.create(CameraView.class);
    private static final BasePagerViewPresenter.PagerViewChanger NULL_PAGER_CHANGER = NullObject.create(BasePagerViewPresenter.PagerViewChanger.class);


    private BasePagerViewPresenter.PagerViewChanger changer = NULL_PAGER_CHANGER;
    private CameraView view = NULL_VIEW;


    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;

    public CameraPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
    }

    public void onDoubleTap() {
        foregroundExecutor.execute(() -> {
            view.showLoading();
            view.disableControls();
            backgroundExecutor.execute(() -> {
                view.switchCameras();
                foregroundExecutor.execute(() -> {
                    view.hideLoading();
                    view.enableControls();
                });
            });
        });

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

    public void attachPageChanger(BasePagerViewPresenter.PagerViewChanger changer) {
        this.changer = changer;
    }

    public void onPictureTaken(byte[] data) {
        foregroundExecutor.execute(() -> {
            view.hideLoading();
            view.showPictureConfirmDialog(data);
            view.enableControls();
        });
    }

    public void onBeforeCapture() {
        foregroundExecutor.execute(() -> {
            view.showLoading();
            view.disableControls();
            backgroundExecutor.execute(() -> {
                view.capture();
            });
        });
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
}
