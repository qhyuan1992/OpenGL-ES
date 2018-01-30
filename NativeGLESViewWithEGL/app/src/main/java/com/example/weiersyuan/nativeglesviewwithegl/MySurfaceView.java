package com.example.weiersyuan.nativeglesviewwithegl;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by weiersyuan on 2016/11/30.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    Context mContext;
    Thread renderWork = new Worker();
    //static int renderCount = 100;
    Bitmap bitmap;
    public MySurfaceView(Context context) {
        super(context);
        mContext = context;
        getHolder().addCallback(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        super.onTouchEvent(event);
        renderWork.start();
        return true;
    }

    public class Worker extends Thread {

        @Override
        public void run() {
            // Loop for ten iterations.
            for(int i=0; i<10000; i++) {
              //  Log.d("Java BMP request render", "run: count" +i);
                nativeRequestRender();
                // Sleep for a while
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // Interrupted exception will occur if
                    // the Worker object's interrupt() method
                    // is called. interrupt() is inherited
                    // from the Thread class.
                    break;
                }
            }
        }

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
        bitmap.recycle();
        if (renderWork.isAlive())
        {
            renderWork.interrupt();
        }

    }

    private void setRender() {
        bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fourk);
        nativeStartRender(bitmap);
    }


    private static native void nativeSurfaceChanged(Surface surface);

    private static native void nativeSurfaceDestroyed();

    private static native void nativeStartRender(Bitmap bitmap);

    private static native void nativeRequestRender();
    static {
        System.loadLibrary("NativeWithEGL");
    }
}
