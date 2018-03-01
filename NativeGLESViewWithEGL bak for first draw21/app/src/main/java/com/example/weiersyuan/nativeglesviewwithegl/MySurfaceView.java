package com.example.weiersyuan.nativeglesviewwithegl;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
/**
 * Created by weiersyuan on 2016/11/30.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    Context mContext;
    Thread renderWork = new Worker();
    static int renderCount = 0;
    static boolean start  = false;
    Bitmap bitmap;
    public MySurfaceView(Context context) {
        super(context);
        mContext = context;
        getHolder().addCallback(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

       //if (start == false)
         //   nativeRequestRender();

      ///  start = true;
        super.onTouchEvent(event);
       renderWork.start();
        return true;
    }

    public class Worker extends Thread {

        @Override
        public void run() {
            // Loop for ten iterations.
            for(renderCount = 0;renderCount < 10000; renderCount++) {
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
        BitmapFactory.Options opts = new BitmapFactory.Options();
       // opts.inPreferredConfig = Config.RGB_565; ;//Config.ARGB_8888;
        opts.inPreferredConfig = Config.RGB_565; ;//Config.ARGB_8888;
        //opts.outHeight = 2160;
        //opts.outWidth  = 3480;
       // opts.inDensity = 0;

        // 先将inJustDecodeBounds设置为true来获取图片的长宽属性
       // final BitmapFactory.Options options = new BitmapFactory.Options();
      //  options.inJustDecodeBounds = true;
     //  BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bluef, options);

        // 计算inSampleSize

       // Log.i( "BMP option", String.format("BMP format width" + options.outWidth+ " height" + options.outHeight));

     //   opts.inSampleSize = calculateInSampleSize(options, opts.outWidth,  opts.outHeight);

        // 加载压缩版图片
      //  opts.inJustDecodeBounds = false;
        //options.inJustDecodeBounds
                // 根据具体情况选择具体的解码方法
       // return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.blue, options);

     bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bluef, opts);

        Log.i( "BMP", String.format("BMP format width" + bitmap.getWidth()+ " height" + bitmap.getHeight()));
        nativeStartRender(bitmap);
    }
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 原图片的宽高
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;


            // 计算inSampleSize值
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private static native void nativeSurfaceChanged(Surface surface);

    private static native void nativeSurfaceDestroyed();

    private static native void nativeStartRender(Bitmap bitmap);

    private static native void nativeRequestRender();
    static {
        System.loadLibrary("NativeWithEGL");
    }
}
