package home.safe.com.guarder;

import android.content.ContentValues;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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


    public FragmentAdapter(FragmentManager fm, ArrayList<ListViewItemSearch> list, GuarderDBHelper guarderDBHelper) {
        super(fm);
        this.alSearch = list;
        this.guarderDBHelper = guarderDBHelper;
        guaderController.setDBHelper(this.guarderDBHelper);
    }

    FragmentSearch fragmentSearch = new FragmentSearch();
    FragmentGuarders fragmentGuarders = new FragmentGuarders();

    @Override
    public Fragment getItem(int position) {

                switch (position){
                    case 0 :
                fragmentSearch.setInstance(alSearch, fragmentGuarders);
                return fragmentSearch;

            case 1 :
                fragmentGuarders.guaderController.setDBHelper(guarderDBHelper);
                if(loadDB() != null) {
                    fragmentGuarders.setList(loadDB());
                }
                return fragmentGuarders;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    private ArrayList<ListViewItemGuarders> loadDB() {
        ContentValues sendCV = new ContentValues();
        sendCV.put("type", "all");

        List<ContentValues> resultList = new ArrayList<>();
        resultList = guaderController.search(sendCV);

        ArrayList<ListViewItemGuarders> guaderlist = new ArrayList<ListViewItemGuarders>();
        ListViewItemGuarders listViewItemGuarders = new ListViewItemGuarders();

        if( resultList != null) {
            for (ContentValues contentValues : resultList) {

                listViewItemGuarders = new ListViewItemGuarders();

                listViewItemGuarders.setTvName(contentValues.get("gmcname").toString());
                listViewItemGuarders.setTvPhone(contentValues.get("gmcphone").toString());
                if (contentValues.get("guse").toString().equals("1")) {
                    listViewItemGuarders.setUse(true);
                } else {
                    listViewItemGuarders.setUse(false);
                }

                guaderlist.add(listViewItemGuarders);
            }
        } else {
            guaderlist = null;
        }


        return guaderlist;
    }
}
