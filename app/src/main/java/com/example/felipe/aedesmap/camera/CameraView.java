package com.example.felipe.aedesmap.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by Felipe on 14/05/2016.
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private Camera camera;

    public CameraView(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
        this.camera.setDisplayOrientation(90);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();

        } catch (IOException e) {
            Log.d("cameraErro","Deu Algo errado na camera: +"+e.getMessage());
           // e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(holder.getSurface()==null){
            return;
        }
        try {
            camera.stopPreview();
        }
        catch(Exception e){
            Log.d("cameraErro","Deu Algo errado na camera: +"+e.getMessage());
        }

        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            Log.d("cameraErro","Deu Algo errado na camera: +"+e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            camera.stopPreview();
        }
        catch(Exception e){
            Log.d("cameraErro","Deu Algo errado na camera: +"+e.getMessage());
        }
        camera.release(); //.release() termina a instância


    }
}
