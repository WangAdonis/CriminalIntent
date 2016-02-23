package cn.adonis.criminalintent.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

import cn.adonis.criminalintent.Crime;
import cn.adonis.criminalintent.CrimeLab;
import cn.adonis.criminalintent.R;


public class CrimeFragment extends Fragment {

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    public static final String EXTRA_CRIME_ID="cn.adonis.criminalintent.crime_id";
    private static final String DIALOG_DATE="date";
    private static final int REQUEST_DATE=0;

    public CrimeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeID=(UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime= CrimeLab.get(getActivity()).getCrimes(crimeID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_crime, container, false);
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
                FragmentManager fm=getActivity().getSupportFragmentManager();
//                DatePickerFragment dialog=new DatePickerFragment();
                DatePickerFragment dialog=DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                dialog.show(fm,DIALOG_DATE);
            }
        });
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
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
        getActivity().setResult(Activity.RESULT_OK,null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=Activity.RESULT_OK)
            return;
        if(requestCode==REQUEST_DATE){
            Date date=(Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            mDateButton.setText(mCrime.getDate().toString());
        }
    }
}
