package com.lucascauthen.uschat.presentation.view.fragments.newfrag;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.controller.base.BaseCameraViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BasePagerViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BasePersonListViewPresenter;
import com.lucascauthen.uschat.presentation.view.adapters.newadapters.PersonViewAdapter;
import com.lucascauthen.uschat.presentation.view.components.newcomp.CameraPreview;
import com.lucascauthen.uschat.presentation.view.dialogs.listeners.OnAcceptListener;
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
    @InjectView(R.id.camera_capture_button)Button captureButton;
    @InjectView(R.id.camera_loading)ProgressBar loading;

    private BaseCameraViewPresenter mainPresenter;
    private BasePersonListViewPresenter friendSelectPresenter;
    private PersonViewAdapter adapter;
    private BaseCameraViewPresenter.CameraPreview cameraPreview;
    private BasePagerViewPresenter.PagerViewChanger pageChanger = NullObject.create(BasePagerViewPresenter.PagerViewChanger.class);

    public static CameraFragment newInstance(BaseCameraViewPresenter mainPresenter, BasePersonListViewPresenter friendSelectPresenter, PersonViewAdapter adapter) {
        CameraFragment f = new CameraFragment();
        f.mainPresenter = mainPresenter;
        f.friendSelectPresenter = friendSelectPresenter;
        f.adapter = adapter;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera, null);
        cameraPreview = new CameraPreview(getActivity()); //Not injected because this requires activity scope and my current DI configuration is not setup for scoping
        ButterKnife.inject(this, v);
        mainPresenter.attachPreview(cameraPreview);

        previewFrame.addView(cameraPreview.getView());
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.onTryCapture();
            }
        });

        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return false;
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        mainPresenter.onDoubleTap();
                        return false;
                    }
                });

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });
        mainPresenter.attachView(this);
        return v;
    }


    @Override
    public void enableControls() {
        captureButton.setEnabled(true);
    }

    @Override
    public void disableControls() {
        captureButton.setEnabled(false);
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
        dialog.setOnAcceptListener(new OnAcceptListener() {
            @Override
            public void accept(Dialog dialog) {
                mainPresenter.onAcceptPicture();
            }
        });
        dialog.show();
    }

    @Override
    public void showFriendSelectDialog() {
        SelectFriendsDialog dialog = new SelectFriendsDialog(getActivity(), friendSelectPresenter, adapter, people -> {
            mainPresenter.onSendChat(people);
        });
        dialog.show();
    }

    @Override
    public void onSendChatComplete() {

    }

    @Override
    public void sendMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mainPresenter != null) {
            mainPresenter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mainPresenter != null) {
            mainPresenter.onPause();
        }
    }
}
