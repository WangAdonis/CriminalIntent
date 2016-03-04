package cn.adonis.criminalintent.activity;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import cn.adonis.criminalintent.Crime;
import cn.adonis.criminalintent.R;
import cn.adonis.criminalintent.fragment.CrimeFragment;
import cn.adonis.criminalintent.fragment.CrimeListFragment;

/**
 * Created by thinkpad on 2016/2/1.
 */
public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks,CrimeFragment.Callbacks{
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if(findViewById(R.id.detailFragmentContainer)==null){
            Intent i=new Intent(this,CrimePagerActivity.class);
            i.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.getId());
            startActivity(i);
        }else {
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            Fragment oldDetail=fm.findFragmentById(R.id.detailFragmentContainer);
            Fragment newDetail=CrimeFragment.newInstance(crime.getId());
            if(oldDetail!=null){
                ft.remove(oldDetail);
            }
            ft.add(R.id.detailFragmentContainer,newDetail);
            ft.commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        FragmentManager fm=getSupportFragmentManager();
        CrimeListFragment crimeListFragment=(CrimeListFragment)fm.findFragmentById(R.id.fragmentContainer);
        crimeListFragment.updateUI();
    }
}
