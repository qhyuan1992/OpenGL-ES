package com.opengles.loadobj;


import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	private ShapeView mGLView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGLView = new ShapeView(this);
        setContentView(mGLView);
	}

}
