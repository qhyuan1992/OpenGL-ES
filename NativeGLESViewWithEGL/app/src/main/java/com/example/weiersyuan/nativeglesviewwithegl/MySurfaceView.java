package com.example.weiersyuan.nativeglesviewwithegl;

import android.content.Context;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by weiersyuan on 2016/11/30.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    public MySurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        nativeRequestRender();
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        setRender();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        nativeSurfaceChanged(surfaceHolder.getSurface());
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        nativeSurfaceDestroyed();
    }

    private void setRender() {
        nativeStartRender();
    }

    private static native void nativeSurfaceChanged(Surface surface);

    private static native void nativeSurfaceDestroyed();

    private static native void nativeStartRender();

    private static native void nativeRequestRender();
    static {
        System.loadLibrary("NativeWithEGL");
    }
}
