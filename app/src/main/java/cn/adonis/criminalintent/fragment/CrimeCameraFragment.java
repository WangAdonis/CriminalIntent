package cn.adonis.criminalintent.fragment;



import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;
import java.util.List;

import cn.adonis.criminalintent.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeCameraFragment extends Fragment {

    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private Button mTakePictureButton;
    public CrimeCameraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_crime_camera, container, false);
        mTakePictureButton=(Button)v.findViewById(R.id.crime_camera_takePictureButton);
        mTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mSurfaceView=(SurfaceView)v.findViewById(R.id.crime_camera_surfaceView);
        SurfaceHolder holder=mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(mCamera!=null){
                    try {
                        mCamera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if(mCamera==null)
                    return;
                Camera.Parameters parameters=mCamera.getParameters();
                Camera.Size s=getBestSupportedSize(parameters.getSupportedPreviewSizes(),width,height);
                parameters.setPreviewSize(s.width,s.height);
                mCamera.setParameters(parameters);
                try{
                    mCamera.startPreview();
                }catch (Exception e){
                    e.printStackTrace();
                    mCamera.release();
                    mCamera=null;
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if(mCamera!=null)
                    mCamera.stopPreview();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.GINGERBREAD){
            mCamera=Camera.open(0);  //0一般指后置摄像头
        }else{
            mCamera=Camera.open();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mCamera!=null){
            mCamera.release();
            mCamera=null;
        }
    }

    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes,int width,int height){
        Camera.Size bestSize=sizes.get(0);
        int largestArea=bestSize.width*bestSize.height;
        for(Camera.Size s:sizes){
            int area=s.width*s.height;
            if(area>largestArea){
                bestSize=s;
                largestArea=area;
            }
        }
        return bestSize;
    }
}
