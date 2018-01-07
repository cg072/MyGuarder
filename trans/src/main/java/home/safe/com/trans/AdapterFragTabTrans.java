package home.safe.com.trans;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by plupin724 on 2017-12-03.
 */


//어댑터 객체로 접근하여 값 가져오기!!!!


public class AdapterFragTabTrans extends FragmentStatePagerAdapter {

    FragmentTransReg fragmentTransReg = new FragmentTransReg();
    FragmentTransList fragmentTransList = new FragmentTransList();

    TransController transController = new TransController();
    TransDBHelper transDBHelper;



    public AdapterFragTabTrans(FragmentManager fm, TransDBHelper dbHelper) {
        super(fm);
        this.transDBHelper = dbHelper;
    }


    @Override
    public Fragment getItem(int position) {

        Log.v("포지션", Integer.toString(position));


        switch (position) {

            case 0 :
                fragmentTransReg.setFragReg(fragmentTransList);
                Log.v("regArrDto" , "확인2");
                return fragmentTransReg;

            case 1 :
               // fragmentTransList.setFragList(adaptDto);
                Log.v("regArrDto" , "확인3");
                return fragmentTransList;
        }

        return null;
    }


    @Override
    public int getCount() {
        return 2;
    }


}
