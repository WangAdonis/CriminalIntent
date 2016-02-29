package cn.adonis.criminalintent.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Date;
import java.util.UUID;
import cn.adonis.criminalintent.Crime;
import cn.adonis.criminalintent.CrimeLab;
import cn.adonis.criminalintent.Photo;
import cn.adonis.criminalintent.PictureUtils;
import cn.adonis.criminalintent.R;
import cn.adonis.criminalintent.activity.CrimeCameraActivity;


public class CrimeFragment extends Fragment {

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    public static final String EXTRA_CRIME_ID="cn.adonis.criminalintent.crime_id";
    private static final String DIALOG_DATE="date";
    private static final int REQUEST_DATE=0;
    private static final int REQUEST_PHOTO=1;
    private static final String DIALOG_IMAGE="image";

    public CrimeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeID=(UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime= CrimeLab.get(getActivity()).getCrimes(crimeID);
        setHasOptionsMenu(true);
    }
    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_crime, container, false);
        getActivity().setTitle(mCrime.getTitle());
        mPhotoView=(ImageView)view.findViewById(R.id.crime_imageView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo p=mCrime.getPhoto();
                if(p==null)
                    return;
                FragmentManager fm=getActivity().getSupportFragmentManager();
                String path=getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
                ImageFragment.newInstance(path).show(fm,DIALOG_IMAGE);
            }
        });

        mTitleField=(EditText)view.findViewById(R.id.crime_title);
        mDateButton=(Button)view.findViewById(R.id.crime_date);
        mSolvedCheckBox=(CheckBox)view.findViewById(R.id.crime_solved);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
//                DatePickerFragment dialog=new DatePickerFragment();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);  //设置回传目标
                dialog.show(fm, DIALOG_DATE);
            }
        });
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            if(NavUtils.getParentActivityName(getActivity())!=null) {
                //getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mPhotoButton=(ImageButton)view.findViewById(R.id.crime_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), CrimeCameraActivity.class);
                startActivityForResult(i,REQUEST_PHOTO);
            }
        });

        PackageManager pm=getActivity().getPackageManager();
        boolean hasCamera=pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                ||pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
                ||Build.VERSION.SDK_INT<Build.VERSION_CODES.GINGERBREAD
                ||Camera.getNumberOfCameras()>0;
        if(!hasCamera)
            mPhotoButton.setEnabled(false);
        return view;
    }

    public static CrimeFragment newInstance(UUID crimeID){
        Bundle args=new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeID);
        CrimeFragment fragment=new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void returnResult(){
        getActivity().setResult(Activity.RESULT_OK, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {  //接收从DatePickerFragment回传的数据
        if(resultCode!=Activity.RESULT_OK)
            return;
        if(requestCode==REQUEST_DATE){
            Date date=(Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            mDateButton.setText(mCrime.getDate().toString());
        }
        if(requestCode==REQUEST_PHOTO){
            String filename=data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            if(filename!=null){
                Photo p=new Photo(filename);
                mCrime.setPhoto(p);
                showPhoto();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(NavUtils.getParentActivityName(getActivity())!=null){
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            case R.id.menu_item_crime_delete:
                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    private void showPhoto(){
        Photo p=mCrime.getPhoto();
        BitmapDrawable b=null;
        if(p!=null){
            String path=getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
            b= PictureUtils.getScaledDrawable(getActivity(),path);
        }
        mPhotoView.setImageDrawable(b);
    }

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }
}
