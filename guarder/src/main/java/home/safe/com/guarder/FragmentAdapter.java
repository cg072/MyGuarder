package home.safe.com.guarder;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by hotki on 2017-12-18.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {

    ArrayList<GuarderVO> alSearch;
    ArrayList<GuarderVO> alGuarder;
    FragmentSearch fragmentSearch = new FragmentSearch();
    FragmentGuarders fragmentGuarders = new FragmentGuarders();
    GuarderManager guarderManager;

    public FragmentAdapter(FragmentManager fm, ArrayList<GuarderVO> list, Context context) {
        super(fm);
        this.alSearch = list;
        this.guarderManager = new GuarderManager(context);
        fragmentSearch.setGuarderManager(guarderManager);
        fragmentGuarders.setGuarderManager(guarderManager);
        fragmentSearch.setInstance(alSearch, fragmentGuarders);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0 :
                return fragmentSearch;

            case 1 :
                return fragmentGuarders;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
