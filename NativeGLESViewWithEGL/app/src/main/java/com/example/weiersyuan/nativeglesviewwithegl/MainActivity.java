package com.example.weiersyuan.nativeglesviewwithegl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;

public class MainActivity extends AppCompatActivity {

    private SurfaceView mView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = new MySurfaceView(this);
        setContentView(mView);
    }
}
