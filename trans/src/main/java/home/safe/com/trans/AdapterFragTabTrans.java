package home.safe.com.trans;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by plupin724 on 2017-12-03.
 */

public class AdapterFragTabTrans extends FragmentStatePagerAdapter {

    public AdapterFragTabTrans(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0 :
                return new FragmentTransReg();

            case 1 :
                return new FragmentTransList();

        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
