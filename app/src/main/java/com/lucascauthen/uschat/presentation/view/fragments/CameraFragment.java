package com.lucascauthen.uschat.presentation.view.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import com.lucascauthen.uschat.presentation.controller.CameraPresenter;
import com.lucascauthen.uschat.presentation.view.components.CameraPreview;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lhc on 7/1/15.
 */
public class CameraFragment extends Fragment implements CameraPresenter.CameraCrudView {

    //Camera Specific:
    private CameraPreview preview;

    //Presentation
    private CameraPresenter presenter;
    @InjectView(R.id.camera_preview)FrameLayout previewFrame;
    @InjectView(R.id.camera_capture_button)Button captureButton;
    @InjectView(R.id.camera_loading)ProgressBar loading;

    private GestureDetector gestureDetector;

    public CameraFragment() {
        //Required empty
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Executor backgroundExecutor = Executors.newFixedThreadPool(10);

        final Handler handler = new Handler();
        Executor foregroundExecutor = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
        presenter = new CameraPresenter(Schedulers.io(), AndroidSchedulers.mainThread(), backgroundExecutor, foregroundExecutor);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera, null);
        ButterKnife.inject(this, v);
        preview = presenter.createPreview(getActivity());
        previewFrame.addView(preview);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCaptureClick();
            }
        });
        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        presenter.onDoubleTap();
                        return false;
                    }
                });

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });
        presenter.attachView(this);
        return v;
    }
    private boolean touch(MotionEvent e) {
        return gestureDetector.onTouchEvent(e);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public static CameraFragment newInstance() {
        return new CameraFragment();
    }

    @Override
    public void enableControls() {
        this.captureButton.setEnabled(true);
        //TODO: Enable other controls
    }

    @Override
    public void disableControls() {
        this.captureButton.setEnabled(false);
        //TODO: Disable other controls
    }

    @Override
    public void notifyCaptureSuccess() {
        //TODO: UI changes for a successful capture, possibly a preview dialog
    }

    @Override
    public void notifyCaptureFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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

    public void onCaptureClick() {
        presenter.onTryCapture();
    }

}
