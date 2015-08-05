package com.lucascauthen.uschat.presentation.controller.implmentations;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Log;

import com.lucascauthen.uschat.data.entities.User;
import com.lucascauthen.uschat.di.PerActivity;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.controller.base.BaseCameraViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BasePagerViewPresenter;
import com.lucascauthen.uschat.util.NullObject;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by lhc on 7/30/15.
 */
@PerActivity
public class CameraViewPresenter implements BaseCameraViewPresenter {
    private static final CameraView NULL_VIEW = NullObject.create(CameraView.class);
    private static final BasePagerViewPresenter.PagerViewChanger NULL_PAGER_CHANGER = NullObject.create(BasePagerViewPresenter.PagerViewChanger.class);
    private static final CameraPreview NULL_PREVIEW = NullObject.create(CameraPreview.class);

    private BasePagerViewPresenter.PagerViewChanger changer = NULL_PAGER_CHANGER;
    private CameraView view = NULL_VIEW;
    private CameraPreview preview = NULL_PREVIEW;


    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;

    //private final CameraPreview preview;
    private Camera camera;

    private Bitmap lastImage = null;
    private byte[] lastImageData = null;

    private final Camera.PictureCallback pictureCallback;
    private int curCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;

    @Inject
    public CameraViewPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.pictureCallback = (data, theCamera) -> {
            camera.stopPreview();
            lastImageData = data;
            lastImage = BitmapFactory.decodeByteArray(data, 0, data.length);
            foregroundExecutor.execute(() -> {
                view.hideLoading();
                view.showPictureConfirmDialog(lastImage);
            });
        };
    }


    @Override
    public void onDoubleTap() {
        view.showLoading();
        backgroundExecutor.execute(() -> {
            switchCameras();
            foregroundExecutor.execute(() -> {
                view.hideLoading();
            });
        });
    }

    @Override
    public void attachPreview(CameraPreview preview) {
        this.preview = preview;
    }

    @Override
    public void onTryCapture() {
        view.showLoading();
        view.disableControls();
        backgroundExecutor.execute(() -> {
            camera.takePicture(null, null, pictureCallback);
        });
    }

    @Override
    public void onSendChat(List<User> names) {
        //I think this should be separated from the network call, but I couldn't think of how to refactor it
        //TODO
        /*
        backgroundExecutor.execute(() -> {
            if (lastImageData != null) {
                ParseFile file = new ParseFile(lastImageData);
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            view.sendMessage(e.getMessage());
                            e.printStackTrace();
                        } else {
                            //view.sendUpdateMessage("File saved successfully!");
                            ParseObject chat = new ParseObject("Chats");
                            chat.put("file", file);
                            chat.put("to", names);
                            chat.put("from", User.getCurrentUser().getName());
                            chat.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        view.sendMessage("Image uploaded successfully!");
                                        changer.changePage(BasePagerViewPresenter.PagerView.Pages.CHAT);
                                    } else {
                                        view.sendMessage(e.getMessage());
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        */
    }

    @Override
    public void onAcceptPicture() {
        view.showFriendSelectDialog();
    }

    public void attachView(CameraView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = NULL_VIEW;
    }

    @Override
    public void onPause() {
        if(camera != null) {
            camera.stopPreview();
            camera.release();
        }
        camera = null;
    }

    @Override
    public void onResume() {
        view.showLoading();
        backgroundExecutor.execute(() -> {
            if (camera == null) {
                camera = getCameraInstance();
            }
            preview.attachCamera(camera);
            foregroundExecutor.execute(() -> {
                view.hideLoading();
            });
        });
    }

    @Override
    public void attachPageChanger(BasePagerViewPresenter.PagerViewChanger changer) {
        this.changer = changer;
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

    }

    private Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(curCameraId); // attempt to get a CameraFragment instance
        } catch (Exception e) {
            Log.d("CameraFragment", "Something went wrong trying to open the camera.");
        }
        return c; // returns null if camera is unavailable
    }
}
