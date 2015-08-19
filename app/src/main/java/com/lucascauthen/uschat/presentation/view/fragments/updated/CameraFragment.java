package com.lucascauthen.uschat.presentation.view.fragments.updated;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.controller.base.BasePagerViewPresenter;
import com.lucascauthen.uschat.presentation.presenters.CameraPresenter;
import com.lucascauthen.uschat.presentation.presenters.FriendSelectPresenter;
import com.lucascauthen.uschat.presentation.presenters.PicturePreviewPresenter;
import com.lucascauthen.uschat.presentation.view.components.CameraPreview;
import com.lucascauthen.uschat.presentation.view.dialogs.PicturePreviewDialog;
import com.lucascauthen.uschat.presentation.view.dialogs.SelectFriendsDialog;
import com.lucascauthen.uschat.presentation.view.views.CameraView;
import com.lucascauthen.uschat.util.NullObject;

import java.io.OutputStream;

/**
 * Created by lhc on 7/30/15.
 */
public class CameraFragment extends Fragment implements CameraView {

    @InjectView(R.id.camera_preview) FrameLayout previewFrame;
    @InjectView(R.id.camera_capture_button) ImageButton captureButton;
    @InjectView(R.id.camera_switch_button) ImageButton switchButton;
    @InjectView(R.id.camera_loading) ProgressBar loading;

    private CameraPresenter presenter;
    private PicturePreviewPresenter previewPresenter;
    private FriendSelectPresenter selectPresenter;

    private CameraPreview cameraPreview;
    private BasePagerViewPresenter.PagerViewChanger pageChanger = NullObject.create(BasePagerViewPresenter.PagerViewChanger.class);

    Camera camera;
    private Camera.PictureCallback pictureCallback;
    private int curCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;

    public static CameraFragment newInstance(CameraPresenter mainPresenter, PicturePreviewPresenter previewPresenter, FriendSelectPresenter selectPresenter) {
        CameraFragment f = new CameraFragment();
        f.presenter = mainPresenter;
        f.previewPresenter = previewPresenter;
        f.selectPresenter = selectPresenter;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera, null);
        v.setClickable(true);

        cameraPreview = new CameraPreview(getActivity()); //Not injected because this requires activity scope and my current DI configuration is not setup for scoping

        ButterKnife.inject(this, v);

        previewFrame.addView(cameraPreview.getView());

        pictureCallback = (data, theCamera) -> {
            camera.stopPreview();
            presenter.onPictureTaken(data);
        };

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onBeforeCapture();
            }
        });
        switchButton.setOnClickListener((view) -> {
            presenter.onDoubleTap();
        });

        presenter.attachView(this);
        return v;
    }


    @Override
    public void enableControls() {
        captureButton.setEnabled(true);
        switchButton.setEnabled(true);
    }

    @Override
    public void disableControls() {
        captureButton.setEnabled(false);
        switchButton.setEnabled(false);
    }

    @Override
    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loading.setVisibility(View.GONE);
    }

    @Override
    public void showPictureConfirmDialog(byte[] image) {
        PicturePreviewDialog dialog = new PicturePreviewDialog(getActivity(), image, previewPresenter);
        dialog.setOnAcceptPictureListener((compressedPicture) -> {
            presenter.onLoadCamera();
            dialog.hide();
            dialog.cancel();
            presenter.onAcceptPicture(compressedPicture);
        });
        dialog.setOnRejectPictureListener(() -> {
            presenter.onLoadCamera();
        });

        dialog.show();
    }

    @Override
    public void showFriendSelectDialog(byte[] compressedImage) {
        SelectFriendsDialog dialog = new SelectFriendsDialog(getActivity(), selectPresenter, compressedImage);
        dialog.show();
    }

    @Override
    public void switchCameras() {
        if (curCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            curCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            curCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        camera.stopPreview();
        camera.release();
        camera = null;
        camera = getCameraInstance();
    }

    @Override
    public void capture() {
        camera.takePicture(null, null, pictureCallback);
    }

    @Override
    public void loadCamera() {
        if (camera == null) {
            camera = getCameraInstance();
        }
        cameraPreview.attachCamera(camera);
        presenter.onCameraLoaded();
    }

    @Override
    public void onAttach() {
        //EMPTY
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onLoadCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (camera != null) {
            camera.stopPreview();
            camera.release();
        }
        camera = null;

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
