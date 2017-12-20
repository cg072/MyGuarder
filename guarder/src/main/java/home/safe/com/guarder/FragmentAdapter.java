package home.safe.com.guarder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by hotki on 2017-12-18.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {
    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0 :
                return new FragmentSearch();

            case 1 :
                return new FragmentGuarders();

        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
