package com.example.opengles_texture;


import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	private RectangleView mGLView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGLView = new RectangleView(this);
        setContentView(mGLView);
	}

}
