package com.lucascauthen.uschat.presentation.view.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.controller.base.BaseCameraViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BasePagerViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseSelectFriendsDialogPresenter;
import com.lucascauthen.uschat.presentation.view.adapters.newadapters.PersonViewAdapter;
import com.lucascauthen.uschat.presentation.view.components.CameraPreview;
import com.lucascauthen.uschat.presentation.view.dialogs.PicturePreviewDialog;
import com.lucascauthen.uschat.presentation.view.dialogs.SelectFriendsDialog;
import com.lucascauthen.uschat.util.NullObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lhc on 7/30/15.
 */
public class CameraFragment extends Fragment implements BaseCameraViewPresenter.CameraView {

    @InjectView(R.id.camera_preview)FrameLayout previewFrame;
    @InjectView(R.id.camera_capture_button)ImageButton captureButton;
    @InjectView(R.id.camera_switch_button) ImageButton switchButton;
    @InjectView(R.id.camera_loading)ProgressBar loading;

    private BaseCameraViewPresenter presenter;

    private BaseCameraViewPresenter.CameraPreview cameraPreview;
    private BasePagerViewPresenter.PagerViewChanger pageChanger = NullObject.create(BasePagerViewPresenter.PagerViewChanger.class);

    private BaseSelectFriendsDialogPresenter subPresenter;
    private PersonViewAdapter adapter;

    private SelectFriendsDialog selectFriendsDialog;
    private PicturePreviewDialog picturePreviewDialog;


    public static CameraFragment newInstance(BaseCameraViewPresenter mainPresenter, BaseSelectFriendsDialogPresenter selectFriendsDialogPresenter, PersonViewAdapter adapter) {
        CameraFragment f = new CameraFragment();
        f.presenter = mainPresenter;
        f.subPresenter = selectFriendsDialogPresenter;
        f.adapter = adapter;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera, null);
        v.setClickable(true);
        selectFriendsDialog = new SelectFriendsDialog(getActivity(), subPresenter, adapter); //Not injected with dagger because it requires an activity context
        cameraPreview = new CameraPreview(getActivity()); //Not injected because this requires activity scope and my current DI configuration is not setup for scoping
        ButterKnife.inject(this, v);
        presenter.attachPreview(cameraPreview);

        previewFrame.addView(cameraPreview.getView());
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onTryCapture();
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
    public void showPictureConfirmDialog(Bitmap image) {
        PicturePreviewDialog dialog = new PicturePreviewDialog(getActivity(), image);
        dialog.setOnAcceptPictureListener((bitmap) -> {
            dialog.hide();
            dialog.cancel();
            presenter.onAcceptPicture();
        });

        dialog.show();
    }

    @Override
    public void closePictureConfirmDialog() {
        //TODO
    }

    @Override
    public void showFriendSelectDialog() {
        selectFriendsDialog.show();
        selectFriendsDialog.update();
    }

    @Override
    public void onSendChatComplete() {
        //TODO
    }

    @Override
    public void sendMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(presenter != null) {
            presenter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(presenter != null) {
            presenter.onPause();
        }
    }
}
