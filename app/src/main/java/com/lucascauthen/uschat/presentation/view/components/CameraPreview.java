package com.lucascauthen.uschat.presentation.view.components;


import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.lucascauthen.uschat.R;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

public class CameraPreview extends SurfaceView {
    private SurfaceHolder holder;
    private Camera camera;
    private final int HEIGHT;

    public CameraPreview(Context context) {
        super(context);
        this.camera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        holder = getHolder();
        // deprecated setting, but required on Android versions prior to 3.0
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        HEIGHT = size.y;
    }
    private void loadPreview(SurfaceHolder holder) {
        if (camera != null) {
            try {
                camera.setPreviewDisplay(holder);
                Camera.Parameters parameters = camera.getParameters();
                List<Camera.Size> sizeList = parameters.getSupportedPictureSizes();
                int chosenSize = getPictureSizeIndexForHeight(sizeList, HEIGHT);
                parameters.setPictureSize(sizeList.get(chosenSize).width, sizeList.get(chosenSize).height);
                parameters.setRotation(90);
                camera.setParameters(parameters);
                setCameraDisplayOrientation();
                camera.startPreview();
            } catch (IOException e) {
                Log.d(getContext().getString(R.string.camera_log_tag), "Error setting camera preview: " + e.getMessage());
            }
        } else {
            Log.d(getContext().getString(R.string.camera_log_tag), "Can't load camera preview because the camera was not setup correctly.");
        }
    }

    private int getPictureSizeIndexForHeight(List<Camera.Size> sizeList, int height) {
        int chosenHeight = -1;
        for(int i=0; i<sizeList.size(); i++) {
            if(sizeList.get(i).height < height) {
                chosenHeight = i-1;
                if(chosenHeight==-1)
                    chosenHeight = 0;
                break;
            }
        }
        return chosenHeight;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if(camera != null) {
            //If your preview can change or rotate, take care of those events here.
            //Make sure to stop the preview before resizing or reformatting it.

            if (this.holder.getSurface() == null) { //preview surface does not exist
                return;
            }

            //stop preview before making changes
            try {
                camera.stopPreview();
            } catch (Exception e) {
                // ignore: tried to stop a non-existent preview
            }

            //set preview size and make any resize, rotate or
            //reformatting changes here
            //start preview with new settings
            try {
                camera.setPreviewDisplay(this.holder);
                camera.startPreview();

            } catch (Exception e) {
                Log.d(getContext().getString(R.string.camera_log_tag), "Error starting camera preview: " + e.getMessage());
            }
        }
    }
    //Taken from http://stackoverflow.com/questions/4645960/how-to-set-android-camera-orientation-properly
    public void setCameraDisplayOrientation() {
        Camera.Parameters parameters = camera.getParameters();

        Camera.CameraInfo camInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(getBackFacingCameraId(), camInfo);


        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (camInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (camInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (camInfo.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
    private int getBackFacingCameraId() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {

                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    public void attachCamera(Camera camera) {
        this.camera = camera;
        loadPreview(getHolder());
    }

    public SurfaceView getView() {
        return this;
    }
}