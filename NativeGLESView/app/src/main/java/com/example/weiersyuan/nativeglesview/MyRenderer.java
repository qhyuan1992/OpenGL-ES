package com.example.weiersyuan.nativeglesview;


import android.content.Context;
import android.opengl.GLSurfaceView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer{
    public float mAngleX;
    public float mAngleY;
    private Context mContext;
    public MyRenderer(Context context) {
		super();
    	mContext = context;
    }
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		String vetexShaderStr = LoadShaderStr(mContext, R.raw.vshader);
		String fragmentShaderStr = LoadShaderStr(mContext, R.raw.fshader);
		nativeInit(vetexShaderStr, fragmentShaderStr);
	}


	@Override
	public void onDrawFrame(GL10 gl) {
		nativeDraw(mAngleX, mAngleY);
	}


	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		nativeSurfaceChanged(width, height);
	}
	
	private String LoadShaderStr(Context context, int resId) {
		StringBuffer strBuf = new StringBuffer();
		try {
			InputStream inputStream = context.getResources().openRawResource(resId);
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			String read = in.readLine();
			while (read != null) {
				strBuf.append(read + "\n");
				read = in.readLine();
			}
			strBuf.deleteCharAt(strBuf.length() - 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

    static {
        System.loadLibrary("NativeGles");
    }

	public static native void nativeInit(String vertexShaderCode, String fragmentShaderCode);
    private static native void nativeDraw(float angleX, float angleY);
    private static native void nativeSurfaceChanged(int width, int height);

}
