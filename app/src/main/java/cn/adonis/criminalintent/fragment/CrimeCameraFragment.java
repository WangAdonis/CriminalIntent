package cn.adonis.criminalintent.fragment;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import cn.adonis.criminalintent.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeCameraFragment extends Fragment {

    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private Button mTakePictureButton;
    private View mProgressContainer;
    public static final String EXTRA_PHOTO_FILENAME="cn.adonis.criminalintent.photo_filename";

    public CrimeCameraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_crime_camera, container, false);
        mProgressContainer=v.findViewById(R.id.crime_camera_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);
        mTakePictureButton=(Button)v.findViewById(R.id.crime_camera_takePictureButton);
        mTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCamera!=null)
                    mCamera.takePicture(mShutterCallback,null,mJpegCallback);
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
                s=getBestSupportedSize(parameters.getSupportedPreviewSizes(),width,height);
                parameters.setPictureSize(s.width,s.height);
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

    private Camera.ShutterCallback mShutterCallback=new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            mProgressContainer.setVisibility(View.VISIBLE);
        }
    };

    private Camera.PictureCallback mJpegCallback=new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            String filename= UUID.randomUUID().toString()+".jpg";
            FileOutputStream os=null;
            boolean success=true;
            try{
                os=getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                os.write(data);
            }catch (Exception e){
                e.printStackTrace();
                success=false;
            }finally {
                try{
                    if(os!=null){
                        os.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    success=false;
                }
            }
            if (success){
                Intent i=new Intent();
                i.putExtra(EXTRA_PHOTO_FILENAME,filename);
                getActivity().setResult(Activity.RESULT_OK,i);
            }else{
                getActivity().setResult(Activity.RESULT_CANCELED);
            }
            getActivity().finish();
        }
    };
}
