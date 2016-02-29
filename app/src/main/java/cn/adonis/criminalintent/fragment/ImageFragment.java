package cn.adonis.criminalintent.fragment;


import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.adonis.criminalintent.PictureUtils;
import cn.adonis.criminalintent.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends DialogFragment {

    public static final String EXTRA_IMAGE_PATH="cn.adonis.criminalintent.image_path";
    private ImageView mImageView;

    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(String imagePath){
        Bundle bundle=new Bundle();
        bundle.putSerializable(EXTRA_IMAGE_PATH, imagePath);
        ImageFragment imageFragment=new ImageFragment();
        imageFragment.setArguments(bundle);
        imageFragment.setStyle(DialogFragment.STYLE_NO_TITLE,0);
        return imageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mImageView=new ImageView(getActivity());
        String path=(String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
        BitmapDrawable image= PictureUtils.getScaledDrawable(getActivity(),path);
        mImageView.setImageDrawable(image);
        return mImageView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PictureUtils.cleanImageView(mImageView);
    }
}
