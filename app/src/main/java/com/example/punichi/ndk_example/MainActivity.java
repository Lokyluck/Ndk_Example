package com.example.punichi.ndk_example;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import static android.R.attr.width;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

/*    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      *//*  // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
*//*

    }*/

/*    *//**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     *//*
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }*/

//OpenCv code
    private static final String TAG = "Main_Activity";
    private JavaCameraView javaCameraView;
    private Button button;
    private Mat Rgba_img;

    private Mat mRgba;
    private Mat mRgbaF;
    private Mat mRgbaT;
    private int i=0;

    //check if OpenCv library loaded
    static {
        if (OpenCVLoader.initDebug())
            Log.i(TAG, "Successfully Loaded OpenCv!");
        else
            Log.i(TAG, "Fail to Load OpenCv.");
    }

    //BaseLoaderCallback
    BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status)
            {
                case SUCCESS:
                    javaCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        javaCameraView = (JavaCameraView) findViewById(R.id.camera_view);
        javaCameraView.setCvCameraViewListener(this);
        javaCameraView.setVisibility(View.INVISIBLE);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                if (i%2 == 1){
                    javaCameraView.setVisibility(SurfaceView.VISIBLE);
                }
                else javaCameraView.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug())
        {
            Log.i(TAG, "OpenCv loaded sucessfully!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else
        {
            Log.i(TAG, "OpenCv failed to load.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_2, this, mLoaderCallback);}
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (javaCameraView != null)
            javaCameraView.disableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (javaCameraView != null)
            javaCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        //Rgba_img = new Mat(height, width, CvType.CV_8UC4);
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgbaF = new Mat(height, width, CvType.CV_8UC4);
        mRgbaT = new Mat(width, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Rgba_img = inputFrame.gray();
        Core.transpose(Rgba_img, mRgbaT);
        Imgproc.resize(mRgbaT, mRgbaF, mRgbaF.size(), 0,0, 0);
        Core.flip(mRgbaF, Rgba_img, 1 );
        return Rgba_img;
    }


}
