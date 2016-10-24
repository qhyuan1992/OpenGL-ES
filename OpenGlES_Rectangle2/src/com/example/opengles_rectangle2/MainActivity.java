package com.example.opengles_rectangle2;


import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

public class MainActivity extends Activity {

	private RectangleView mGLView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGLView = new RectangleView(this);
        setContentView(mGLView);
	}

}
