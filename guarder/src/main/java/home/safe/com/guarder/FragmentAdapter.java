package home.safe.com.guarder;

import android.content.ContentValues;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hotki on 2017-12-18.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {

    ArrayList<ListViewItemSearch> alSearch;
    ArrayList<ListViewItemSearch> alGuarder;
    GuaderController guaderController = new GuaderController();
    GuarderDBHelper guarderDBHelper;
    FragmentSearch fragmentSearch = new FragmentSearch();
    FragmentGuarders fragmentGuarders = new FragmentGuarders();

    public FragmentAdapter(FragmentManager fm, ArrayList<ListViewItemSearch> list, GuarderDBHelper guarderDBHelper) {
        super(fm);
        this.alSearch = list;
        this.guarderDBHelper = guarderDBHelper;
        guaderController.setDBHelper(this.guarderDBHelper);
/*        Log.v("프레그먼트", "생성시작");
        fragmentSearch = new FragmentSearch();
        fragmentGuarders = new FragmentGuarders();
        Log.v("프레그먼트", "생성완료");*/
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0 :
                //fragmentSearch = new FragmentSearch();
                fragmentSearch.setInstance(alSearch, fragmentGuarders);
                return fragmentSearch;

            case 1 :
                //fragmentGuarders = new FragmentGuarders();
                fragmentGuarders.guaderController.setDBHelper(guarderDBHelper);
                return fragmentGuarders;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
