package cn.adonis.criminalintent.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

import cn.adonis.criminalintent.fragment.CrimeFragment;
import cn.adonis.criminalintent.R;

public class CrimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.fragmentContainer);
        if(fragment==null){
            //fragment=new CrimeFragment()
            UUID crimeID=(UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
            fragment=CrimeFragment.newInstance(crimeID);
            fm.beginTransaction().add(R.id.fragmentContainer,fragment).commit();
        }
    }
}
