package com.lucascauthen.uschat.presentation.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Log;
import android.widget.FrameLayout;

import com.lucascauthen.uschat.presentation.view.components.CameraPreview;

import java.io.IOException;
import java.util.concurrent.Executor;

import dagger.Provides;
import rx.Scheduler;

/**
 * Created by lhc on 6/25/15.
 */
public class CameraPresenter implements Presenter {
    private final Scheduler backgroundScheduler;
    private final Scheduler foregroundScheduler;
    private final Executor backgroundExecutor;
    private final Executor foregroundExecutor;

    //Camera Specific:
    private Camera camera;
    private CameraPreview preview;
    private int curCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

    private static final NullCameraCrudView NULL_VIEW = new NullCameraCrudView();
    private CameraCrudView view = NULL_VIEW;
    private Bitmap lastImage = null;

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) { //Currently in the background executor
            lastImage = BitmapFactory.decodeByteArray(data, 0, data.length);
            foregroundExecutor.execute(() -> {
                view.hideLoading();
                view.notifyCaptureSuccess(lastImage);
            });
        }
    };

    public CameraPresenter(Scheduler backgroundScheduler, Scheduler foregroundScheduler, Executor backgroundExecutor, Executor foregroundExecutor) {
        this.backgroundScheduler = backgroundScheduler;
        this.foregroundScheduler = foregroundScheduler;
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
    }
    public void attachView(CameraCrudView view) {
        this.view = view;
    }
    public void detachView() {
        this.view = NULL_VIEW;
    }
    public void onDoubleTap() {
        view.showLoading();
        backgroundExecutor.execute(() -> {
            switchCameras();
            foregroundExecutor.execute(() -> {
                view.hideLoading();
            });
        });
    }

    public void onTryCapture() {
        view.showLoading();
        view.disableControls();
        backgroundExecutor.execute(() -> {
            camera.stopPreview();
            camera.takePicture(null, null, pictureCallback);
        });
    }

    public CameraPreview createPreview(Context context) {
        return preview = new CameraPreview(context, camera);
    }

    private Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(curCameraId); // attempt to get a Camera instance
        } catch (Exception e) {
            Log.d("Camera", "Something went wrong trying to open the camera.");
        }
        return c; // returns null if camera is unavailable
    }

    private void switchCameras() {

        if (curCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            curCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            curCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        camera.stopPreview();
        camera.release();
        camera = getCameraInstance();
        preview.reloadPreview(camera);
    }


    public void onPause() {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public void onResume() {
        view.showLoading();
        backgroundExecutor.execute(() -> {
            if(camera == null) {
                camera = getCameraInstance();
            }
            preview.reloadPreview(camera);
            foregroundExecutor.execute(() -> {
                view.hideLoading();
            });
        });
    }


    public interface CameraCrudView {
        void enableControls();

        void disableControls();

        void notifyCaptureSuccess(Bitmap image); //TODO: Might pass some information about the capture back to the view

        void notifyCaptureFailure(String message);

        void showLoading();

        void hideLoading();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
    public static class NullCameraCrudView implements CameraCrudView {

        @Override
        public void enableControls() {

        }

        @Override
        public void disableControls() {

        }

        @Override
        public void notifyCaptureSuccess(Bitmap image) {

        }

        @Override
        public void notifyCaptureFailure(String message) {

        }

        @Override
        public void showLoading() {

        }

        @Override
        public void hideLoading() {

        }
    }
}
