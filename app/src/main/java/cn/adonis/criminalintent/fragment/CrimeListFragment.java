package cn.adonis.criminalintent.fragment;

import android.support.v4.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
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
import cn.adonis.criminalintent.activity.CrimeActivity;
import cn.adonis.criminalintent.activity.CrimePagerActivity;


public class CrimeListFragment extends ListFragment {

    private ArrayList<Crime> mCrimes;
    private static final int REQUEST_CRIME=1;

    // TODO: Customize parameter argument names
    public CrimeListFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.crime_title);
        mCrimes= CrimeLab.get(getActivity()).getCrimes();
        ArrayAdapter<Crime> adapter=new CrimeAdapter(mCrimes);
        setListAdapter(adapter);


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
}
