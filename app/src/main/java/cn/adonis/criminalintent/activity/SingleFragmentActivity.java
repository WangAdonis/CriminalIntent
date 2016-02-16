package cn.adonis.criminalintent.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.adonis.criminalintent.R;

/**
 * 动态调用单个Fragment的Activity抽象类
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm=getFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.fragmentContainer);
        if(fragment==null){
            fragment=createFragment();
            fm.beginTransaction().add(R.id.fragmentContainer,fragment).commit();
        }
    }
}