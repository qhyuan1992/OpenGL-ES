package com.example.opengles_stenciltest;


import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	private ShapeView mGLView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGLView = new ShapeView(this);
        setContentView(R.layout.activity_main);
        LinearLayout ll = (LinearLayout) findViewById(R.id.main_liner);
		ll.addView(mGLView);
		CheckBox cb = (CheckBox) this.findViewById(R.id.cb);
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mGLView.setOpenStencilTest(true);
				} else {
					mGLView.setOpenStencilTest(false);
				}
				mGLView.requestRender();
			}
		});
	}
}
