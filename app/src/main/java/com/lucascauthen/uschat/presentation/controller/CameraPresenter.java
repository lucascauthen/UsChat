package com.lucascauthen.uschat.presentation.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.presentation.view.components.CameraPreview;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import dagger.Provides;
import rx.Scheduler;

/**
 * Created by lhc on 6/25/15.
 */
public class CameraPresenter {
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
    private byte[] lastImageRaw = null;

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) { //Currently in the background executor
            camera.stopPreview();
            lastImageRaw = data;
            lastImage = BitmapFactory.decodeByteArray(data, 0, data.length);
            Log.d("UsChat", "Before width: " + lastImage.getWidth());
            Log.d("UsChat", "Before height: " + lastImage.getHeight());
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

    public void resetCameraAfterCapture() {
        preview.reloadPreview(camera);
        view.enableControls();
        view.hideLoading();
    }

    public void onPause() {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public void onResume() {
        view.showLoading();
        backgroundExecutor.execute(() -> {
            if (camera == null) {
                camera = getCameraInstance();
            }
            preview.reloadPreview(camera);
            foregroundExecutor.execute(() -> {
                view.hideLoading();
            });
        });
    }

    public void updateBitmap(Bitmap newBitmap) {
        this.lastImage = newBitmap;
    }

    private byte[] convertToPNG() {
        if (lastImage != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            lastImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } else {
            return null;
        }
    }

    public void sendChat(List<String> names) {
        backgroundExecutor.execute(() -> {
            if (lastImageRaw != null) {
                ParseFile file = new ParseFile(lastImageRaw);
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            view.sendUpdateMessage(e.getMessage());
                            e.printStackTrace();
                        } else {
                            //view.sendUpdateMessage("File saved successfully!");
                            ParseObject chat = new ParseObject("Chats");
                            chat.put("file", file);
                            chat.put("to", names);
                            chat.put("from", Person.getCurrentUser().getName());
                            chat.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e == null) {
                                        view.sendUpdateMessage("Image uploaded successfully!");
                                        view.onSendChat();
                                    } else {
                                        view.sendUpdateMessage(e.getMessage());
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    public interface CameraCrudView {
        void enableControls();

        void disableControls();

        void notifyCaptureSuccess(Bitmap image); //TODO: Might pass some information about the capture back to the view

        void notifyCaptureFailure(String message);

        void showLoading();

        void hideLoading();

        void sendUpdateMessage(String msg);

        void onSendChat();
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

        @Override
        public void sendUpdateMessage(String msg) {

        }

        @Override
        public void onSendChat() {

        }
    }
}
