package home.safe.com.guarder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by hotki on 2017-12-18.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {

    ArrayList<ListViewItemSearch> alSearch;

    public FragmentAdapter(FragmentManager fm, ArrayList<ListViewItemSearch> list) {
        super(fm);
        this.alSearch = list;
    }

    FragmentSearch fragmentSearch = new FragmentSearch();
    FragmentGuarders fragmentGuarders = new FragmentGuarders();

    int a =0;
    int b = 0;



    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0 :
                a += 1;
                Log.v("로그a", String.valueOf(a));
                fragmentSearch.setInstance(alSearch, fragmentGuarders);
                return fragmentSearch;

            case 1 :
                b += 1;
                Log.v("로그b", String.valueOf(b));
                return fragmentGuarders;

        }

        return null;
    }



    @Override
    public int getCount() {
        return 2;
    }
}
