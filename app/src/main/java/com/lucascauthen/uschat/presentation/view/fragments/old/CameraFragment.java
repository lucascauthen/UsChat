package com.lucascauthen.uschat.presentation.view.fragments.old;

import android.app.Activity;
import android.app.Dialog;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.lucascauthen.uschat.presentation.view.components.CameraPreview;
import com.lucascauthen.uschat.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CameraFragment extends Fragment {
    /*
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

    private OnRequestFriendsList listener;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private File latestMediaFile;

    /** Create a file Uri for saving an image or video */
    /*
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }


    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "UsChat");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("UsChatCamera", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
    private Camera.PictureCallback picture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d("UsChatCamera", "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                latestMediaFile = pictureFile;
                chatDialogFactory().show();
            } catch (FileNotFoundException e) {
                Log.d("UsChatCamera", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("UsChatCamera", "Error accessing file: " + e.getMessage());
            }
        }
    };

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
        final
        Detector gesture = new GestureDetector(getActivity(),
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



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnRequestFriendsList) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnRequestFriendsList {
        public List<Friend> getFriends();
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
            camera.takePicture(null, null, picture);
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
    public void sendChat(String sendTo) {
        Log.d("camera", "Sending chat to: " + sendTo);
    }
    public Dialog chatDialogFactory() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_sendchat);
        dialog.setTitle("Who do you want to send this to?");
        final Spinner spinner = (Spinner)dialog.findViewById(R.id.dialog_chat_choose);
        List<Friend> list = listener.getFriends();
        ArrayList<String> stringList = new ArrayList<String>();
        for(Friend friend : list) {
            stringList.add(friend.getName());
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, stringList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        Button close = (Button)dialog.findViewById(R.id.dialog_chat_close);
        Button enter = (Button)dialog.findViewById(R.id.dialog_chat_enter);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sendChat(spinner.getSelectedItem().toString());
            }
        });

        return dialog;
    }
    */
}
