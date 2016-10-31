package com.example.opengles_texture2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;

public class MainActivity extends Activity {

	private RectangleView mGLView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGLView = new RectangleView(this);
		setContentView(R.layout.activity_main);
		LinearLayout ll = (LinearLayout) findViewById(R.id.main_liner);
		ll.addView(mGLView);
		SeekBar sb = (SeekBar) this.findViewById(R.id.sb);
		RadioGroup rg = (RadioGroup) this.findViewById(R.id.radioGroup);
		sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				mGLView.setScale((float)progress/seekBar.getMax() * 2, (float)progress/seekBar.getMax() * 2);
				mGLView.requestRender();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});
		
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mGLView.setFilterType(checkedId - R.id.nearest);
				mGLView.requestRender();
			}
		});
	}

}
