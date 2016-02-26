package cn.adonis.criminalintent;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by thinkpad on 2016/2/1.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mAppContext;
    private ArrayList<Crime> mCrimes;
    private static final String TAG="CrimeLab";
    private static final String FILENAME="crime.json";
    private CriminalIntentJSONSerializer mSerializer;


    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrimes(UUID id) {
        for(Crime c:mCrimes){
            if(c.getId().equals(id))
                return c;
        }
        return null;
    }

    private CrimeLab(Context appContext){
        mAppContext=appContext;
        //mCrimes=new ArrayList<Crime>();
        mSerializer=new CriminalIntentJSONSerializer(mAppContext,FILENAME);
        try{
            mCrimes=mSerializer.loadCrimes();
        }catch (Exception e){
            mCrimes=new ArrayList<Crime>();
            Log.e(TAG,"Error loading crimes:",e);
        }
//        for(int i=0;i<100;i++){
//            Crime c=new Crime();
//            c.setTitle("Crime #"+i);
//            c.setSolved(i%2==0);
//            mCrimes.add(c);
//        }
    }

    public static CrimeLab get(Context c){
        if(sCrimeLab==null){
            sCrimeLab=new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;
    }

    public void addCrime(Crime c){
        mCrimes.add(c);
    }

    public boolean saveCrimes(){
        try{
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "crimes saved to file");
            return true;
        }catch (Exception e){
            Log.e(TAG,"Error saving crimes:",e);
            return false;
        }
    }
}
