package com.lucascauthen.uschat;

import android.app.Activity;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
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


public class CameraFragment extends Fragment {

    private Camera camera;
    private CameraPreview preview;
    private int curCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    //TODO: Fix this: this prevents the app from reassigning the camera on first opening
    private boolean fromPause = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static CameraFragment newInstance(String param1, String param2) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        camera = getCameraInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        preview = new CameraPreview(this.getActivity(), camera);
        final FrameLayout previewFrame = (FrameLayout) view.findViewById(R.id.camera_preview);
        previewFrame.addView(this.preview);
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
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        // TODO: Update argument type and name
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
}
