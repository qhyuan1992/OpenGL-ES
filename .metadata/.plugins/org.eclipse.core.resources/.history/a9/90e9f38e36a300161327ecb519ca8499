package com.example.opengles_rectangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

public class RectangleView extends GLSurfaceView{

	public RectangleView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		setRenderer(new MyRender());
	}
	
	class MyRender implements GLSurfaceView.Renderer {
		private Rectangle rectangle;
		
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1);
			rectangle = new Rectangle();
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			GLES20.glViewport(0, 0, width, height);
			float ratio= (float)height / width;
			Matrix.orthoM(mProjectionMatrix, 0, -1, 1, -ratio, ratio, 0, 5);
		}

		// 投影矩阵
	    private final float[] mProjectionMatrix = new float[16];
		@Override
		public void onDrawFrame(GL10 gl) {
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            rectangle.draw(mProjectionMatrix);
		}
	}
	
}