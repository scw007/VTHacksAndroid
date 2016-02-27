package com.example.steven.vthacksar.Core;

import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;

/**
 * Created by Fadi on 5/11/2014.
 */
public class CameraEngine {

    static final String TAG = "DBG_" + CameraUtils.class.getName();

    boolean on;
    Camera camera;
    SurfaceHolder surfaceHolder;

    Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {

        }
    };

    public boolean isOn() {
        return on;
    }

    private CameraEngine(SurfaceHolder surfaceHolder){
        this.surfaceHolder = surfaceHolder;
    }

    static public CameraEngine New(SurfaceHolder surfaceHolder){
        Log.d(TAG, "Creating camera engine");
        return  new CameraEngine(surfaceHolder);
    }

    public void requestFocus() {
        if (camera == null)
            return;

        if (isOn()) {
            Camera.Parameters p = camera.getParameters();
            List<String> focusModes = p.getSupportedFocusModes();

            if(focusModes != null && focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                //Phone supports autofocus!
                camera.autoFocus(autoFocusCallback);
            }
            else {
                //Phone does not support autofocus!
            }
        }
    }

    public void start() {

        Log.d(TAG, "Entered CameraEngine - start()");
        this.camera = CameraUtils.getCamera();

        if (this.camera == null)
            return;

        Log.d(TAG, "Got camera hardware");

        try {

            this.camera.setPreviewDisplay(this.surfaceHolder);
            this.camera.setDisplayOrientation(180);
            this.camera.startPreview();

            on = true;

            Log.d(TAG, "CameraEngine preview started");

        } catch (IOException e) {
            Log.e(TAG, "Error in setPreviewDisplay");
        }
    }

    public void stop(){

        if(camera != null){
            //this.autoFocusEngine.stop();
            camera.release();
            camera = null;
        }

        on = false;

        Log.d(TAG, "CameraEngine Stopped");
    }

    public void takeShot(Camera.ShutterCallback shutterCallback,
                         Camera.PictureCallback rawPictureCallback,
                         Camera.PictureCallback jpegPictureCallback ){
        if(isOn()){
            camera.takePicture(shutterCallback, rawPictureCallback, jpegPictureCallback);
        }
    }

}
