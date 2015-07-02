package com.lucascauthen.uschat.presentation.view.components;


import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.lucascauthen.uschat.R;

import java.io.IOException;

public class CameraPreview extends SurfaceView {
    private SurfaceHolder holder;
    private Camera camera;
    int curCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.camera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        holder = getHolder();
        // deprecated setting, but required on Android versions prior to 3.0
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    public void reloadPreview(Camera camera) {
        this.camera = camera;
        loadPreview(getHolder());
    }

    private void loadPreview(SurfaceHolder holder) {
        if (camera != null) {
            try {
                camera.setPreviewDisplay(holder);
                Camera.Parameters parameters = camera.getParameters();
                parameters.set("orientation", "portrait");
                parameters.setRotation(90);
                camera.setParameters(parameters);
                camera.setDisplayOrientation(90);
                camera.startPreview();
            } catch (IOException e) {
                Log.d(getContext().getString(R.string.camera_log_tag), "Error setting camera preview: " + e.getMessage());
            }
        } else {
            Log.d(getContext().getString(R.string.camera_log_tag), "Can't load camera preview because the camera was not setup correctly.");
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (this.holder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            camera.setPreviewDisplay(this.holder);
            camera.startPreview();

        } catch (Exception e) {
            Log.d(getContext().getString(R.string.camera_log_tag), "Error starting camera preview: " + e.getMessage());
        }
    }
}