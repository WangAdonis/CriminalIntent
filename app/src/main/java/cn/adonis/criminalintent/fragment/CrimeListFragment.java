package cn.adonis.criminalintent.fragment;

import android.os.Build;
import android.support.v4.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import cn.adonis.criminalintent.Crime;
import cn.adonis.criminalintent.CrimeLab;
import cn.adonis.criminalintent.R;
import cn.adonis.criminalintent.activity.CrimePagerActivity;


public class CrimeListFragment extends ListFragment {

    private ArrayList<Crime> mCrimes;
    private static final int REQUEST_CRIME=1;
    private boolean isSubtitleVisible;

    // TODO: Customize parameter argument names
    public CrimeListFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.crime_title);
        setHasOptionsMenu(true);  //设置fragment包含菜单
        setRetainInstance(true);  //设置保留fragment
        isSubtitleVisible=false;
        mCrimes= CrimeLab.get(getActivity()).getCrimes();
        ArrayAdapter<Crime> adapter=new CrimeAdapter(mCrimes);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= super.onCreateView(inflater, container, savedInstanceState);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            if (isSubtitleVisible){
                ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle);
            }
        }
        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Crime c=((CrimeAdapter)getListAdapter()).getItem(position);
        //Toast.makeText(getActivity(),c.getTitle(),Toast.LENGTH_SHORT).show();
//        Intent i=new Intent(getActivity(), CrimeActivity.class);
        Intent i=new Intent(getActivity(), CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
        //startActivity(i);
        startActivityForResult(i,REQUEST_CRIME);
    }

    private class CrimeAdapter extends ArrayAdapter<Crime> {

        public CrimeAdapter(ArrayList<Crime> crimes) {
            super(getActivity(),0,crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=getActivity().getLayoutInflater().inflate(R.layout.list_item_crime,null);
            }
            Crime c=getItem(position);
            TextView titleTextView=(TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c.getTitle());
            TextView dateTextView=(TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
            dateTextView.setText(c.getDate().toString());
            CheckBox solvedCheckBox=(CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.isSolved());
            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged(); //更新列表
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CRIME){

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem showSubtitle=menu.findItem(R.id.menu_item_show_subtitle);
        if (isSubtitleVisible&&showSubtitle!=null){
            showSubtitle.setTitle(R.string.hide_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime crime=new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent i=new Intent(getActivity(),CrimePagerActivity.class);
                i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
                startActivityForResult(i,0);
                return true;
            case R.id.menu_item_show_subtitle:
                //if(getActivity().getActionBar().getSubtitle()==null) {
                if(((AppCompatActivity)getActivity()).getSupportActionBar().getSubtitle()==null) {
                    //getActivity().getActionBar().setSubtitle(R.string.subtitle);
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle);
                    item.setTitle(R.string.hide_subtitle);
                    isSubtitleVisible=true;
                }else {
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(null);
                    item.setTitle(R.string.show_subtitle);
                    isSubtitleVisible=false;
                }
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
