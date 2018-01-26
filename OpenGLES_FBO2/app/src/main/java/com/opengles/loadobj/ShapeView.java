package com.opengles.loadobj;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

public class ShapeView extends GLSurfaceView{
	public static int sScreenWidth;
	public static int sScreenHeight;
	
	private float mPreviousY;
	private float mPreviousX;
	MyRender mMyRender;
	public ShapeView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		mMyRender = new MyRender(context);
		setRenderer(mMyRender);
	}
	
	public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
            float dy = y - mPreviousY;
            float dx = x - mPreviousX;
            mMyRender.yAngle += dx;
            mMyRender.xAngle+= dy;
            requestRender();
        }
        mPreviousY = y;
        mPreviousX = x;
        return true;
    }
	
	class MyRender implements GLSurfaceView.Renderer {
		private Shape mRectangle;
		float yAngle;
    	float xAngle;
		private Context mContext;
    	public MyRender(Context context) {
    		mContext = context;
    	}
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1);
			mRectangle = new Shape(mContext);
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			sScreenWidth = width;
			sScreenHeight = height;
			GLES20.glViewport(0, 0, width, height);
			Matrix.perspectiveM(mProjectionMatrix, 0, 45, (float)width/height, 2, 5);
			Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 3,  0, 0, 0, 0, 1, 0);
		}
	    private final float[] mProjectionMatrix = new float[16];
	    private final float[] mViewMatrix = new float[16];
	    private final float[] mModuleMatrix = new float[16];
	    private final float[] mViewProjectionMatrix = new float[16];
	    private final float[] mMVPMatrix = new float[16];
	    
		@Override
		public void onDrawFrame(GL10 gl) {
			Matrix.setIdentityM(mModuleMatrix, 0);
			Matrix.rotateM(mModuleMatrix, 0, xAngle, 1, 0, 0);
			Matrix.rotateM(mModuleMatrix, 0, yAngle, 0, 1, 0);
			Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
			Matrix.multiplyMM(mMVPMatrix, 0, mViewProjectionMatrix, 0, mModuleMatrix, 0);
			mRectangle.draw(mMVPMatrix, mModuleMatrix);
		}
	}
}