package cn.adonis.criminalintent.activity;

import android.support.v4.app.Fragment;
import cn.adonis.criminalintent.fragment.CrimeListFragment;

/**
 * Created by thinkpad on 2016/2/1.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

}
