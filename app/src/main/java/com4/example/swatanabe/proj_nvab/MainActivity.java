package com4.example.swatanabe.proj_nvab;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class MainActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private CameraBridgeViewBase mCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCameraView = (CameraBridgeViewBase) findViewById(R.id.camera_view);
        mCameraView.setCvCameraViewListener(this);
    }

    protected void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, new OpenCVLoaderCallback(this, mCameraView));
    }

    protected void onPause() {
        super.onPause();
        mCameraView.disableView();
    }

    private static class OpenCVLoaderCallback extends BaseLoaderCallback {
        private final CameraBridgeViewBase mCameraView;

        private OpenCVLoaderCallback(Context context, CameraBridgeViewBase cameraView) {
            super(context);
            mCameraView = cameraView;
        }

        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    mCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    @Override
    public void onCameraViewStopped() {
    }

    
    // 直下の関数の元となるもの．
//    @Override
//    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame){
//        return inputFrame.rgba();
//    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba = inputFrame.rgba();
        MatOfPoint maxArea = SkinDetector.getInstance().getMaxSkinArea(rgba);
        if (maxArea != null) {
            Rect rectOfArea = Imgproc.boundingRect(maxArea);
            Imgproc.rectangle(rgba, rectOfArea.tl(), rectOfArea.br(), new Scalar(0, 0, 255), 3);
        }
        return rgba;

//        Mat hsv = new Mat();
//        Imgproc.cvtColor(rgba, hsv, Imgproc.COLOR_RGB2HSV);
//        Imgproc.medianBlur(hsv, hsv, 3);
//        Core.inRange(hsv, new Scalar(0, 100, 30), new Scalar(10, 255, 255), hsv);
//        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
//        Mat hierarchy = new Mat(hsv.cols(), hsv.rows(), CvType.CV_32SC1);
//        Imgproc.findContours(hsv, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
//        if (contours != null) {
//
//
//        }
//        return inputFrame.rgba();
    }
}
