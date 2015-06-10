package com.lucascauthen.uschat;

import android.app.Activity;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lucascauthen.uschat.Camera.CameraPreview;

import java.io.IOException;
import java.util.logging.LogRecord;


public class CameraFragment extends Fragment {

    private  final int VIDEO_CAPTURE_DELAY = 1000;
    private Camera camera;
    private CameraPreview preview;
    private int curCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    //TODO: Fix this: this prevents the app from reassigning the camera on first opening
    private boolean fromPause = false;


    private boolean isMouseDown = false;
    private boolean tryingToCaptureVideo = false;
    private boolean capturingVideo = false;
    private boolean mouseCameUp = false;

    private OnFragmentInteractionListener mListener;

    public static CameraFragment newInstance(String param1, String param2) {
        CameraFragment fragment = new CameraFragment();
        return fragment;
    }

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        camera = getCameraInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        preview = new CameraPreview(this.getActivity(), camera);
        final FrameLayout previewFrame = (FrameLayout) view.findViewById(R.id.camera_preview);
        previewFrame.addView(this.preview);
        //startPrintVariables();
        view.findViewById(R.id.button_capture).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isMouseDown = true;
                        if(!tryingToCaptureVideo) {
                            tryCaptureVideo();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        isMouseDown = false;
                        mouseCameUp = true;
                        capturePicture();
                    default:
                        break;
                }
                return false;
            }
        });
        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        switchCameras();
                        return false;
                    }
                });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });
        // Inflate the layout for this fragment
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        return view;
    }
    //TODO: Delete after testing:
    public void startPrintVariables() {
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Print info:
                Log.d(getString(R.string.camera_log_tag), "isMouseDown: " + isMouseDown);
                Log.d(getString(R.string.camera_log_tag), "tryingToCaptureVideo: " + tryingToCaptureVideo);
                Log.d(getString(R.string.camera_log_tag), "capturingVideo: " + capturingVideo);
                Log.d(getString(R.string.camera_log_tag), "mouseCameUp: " + mouseCameUp);
                startPrintVariables();
            }
        }, 1000);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }
    public Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(curCameraId); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    public void switchCameras() {
        if(curCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
            curCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
        else {
            curCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        camera.stopPreview();
        camera.release();
        reloadCameraPreview();
        camera.startPreview();
    }
    public void reloadCameraPreview() {
        camera = getCameraInstance();
        try {
            camera.setPreviewDisplay(preview.getHolder());
            Camera.Parameters parameters = camera.getParameters();
            parameters.set("orientation", "portrait");
            parameters.setRotation(90);
            camera.setParameters(parameters);
            camera.setDisplayOrientation(90);
        } catch (IOException e) {
            Log.d(getString(R.string.camera_log_tag), "Error setting camera preview: " + e.getMessage());
        }

    }

    @Override
    public void onPause() {
        camera.stopPreview();
        camera.release();
        fromPause = true;
        super.onPause();
    }

    @Override
    public void onResume() {
        if(fromPause) {
            camera = getCameraInstance();
            preview.reloadCamera(camera);
            fromPause = false;
        }
        super.onResume();
    }

    public void capturePicture() {
        if(!capturingVideo) {
            Log.d(getString(R.string.camera_log_tag), "Capturing picture!");

        }
    }
    public void tryCaptureVideo() {
        Log.d(getString(R.string.camera_log_tag), "Trying to capture video!");
        tryingToCaptureVideo = true;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isMouseDown && !mouseCameUp){
                    captureVideo();
                } else {
                    tryingToCaptureVideo = false;
                    mouseCameUp = false;
                    Log.d(getString(R.string.camera_log_tag), "Not capturing video, taking a picture instead!");
                }
            }
        }, VIDEO_CAPTURE_DELAY);

    }
    public void captureVideo() {
        tryingToCaptureVideo = false;
        capturingVideo = true;
        Log.d(getString(R.string.camera_log_tag), "Capturing video!");
        capturingVideo = false;

    }
}
