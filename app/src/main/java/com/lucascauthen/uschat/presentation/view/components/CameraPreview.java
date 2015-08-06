package com.lucascauthen.uschat.presentation.view.components;


import android.app.Activity;
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
    int curCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private static int HEIGHT;

    @Inject
    public CameraPreview(Activity context) {
        super(context);
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
                List<Camera.Size> sizeList = parameters.getSupportedPictureSizes();
                int chosenSize = getPictureSizeIndexForHeight(sizeList, HEIGHT);
                parameters.setPictureSize(sizeList.get(chosenSize).width, sizeList.get(chosenSize).height);
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
    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the CameraFragment preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if(camera != null) {
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
}