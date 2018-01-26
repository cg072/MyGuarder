package home.safe.com.guarder;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by hotki on 2017-12-18.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {

    FragmentSearch fragmentSearch = new FragmentSearch();
    FragmentGuarders fragmentGuarders = new FragmentGuarders();

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
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

    // FragmentSearch에서 추가된 지킴이들을 담는 리스트 초기화(액티비티에서 작업)
    public void resetAddGuarderVOInFmSearch(){
        fragmentSearch.resetAddGuarderList();
    }

    // FragmentSearch로부터 추가된 지킴이들을 담는 리스트를 가져옴(액티비티에서 작업)
    public ArrayList<GuarderVO> getAddGuarderListInFmSearch(){
        return fragmentSearch.getAddGuarderList();
    }

    // FragmentSearch에서 지킴이 목록(full) 셋팅(액티비티에서 작업)
    public void setGuarderListInFmSearch(ArrayList<GuarderVO> list){
        fragmentSearch.setGuarderList(list);
    }

    public void updateGuarderListInFmGuarder() {
        fragmentGuarders.updateGuarderList();
    }

    // FragmentGuarder로부터 지킴이 리스트를 가져옴(액티비티에서 작업)
    public ArrayList<GuarderVO> getGuarderListInFmGuarder() {
        return fragmentGuarders.getGuarderList();
    }

    // FragmentGuarder에서 GuarderManager를 셋팅(액티비티에서 작업)
    public void setActivity(Activity activity) {
        fragmentGuarders.setGuarderManager(activity.getApplicationContext());
        fragmentSearch.setList(activity);
    }
}
