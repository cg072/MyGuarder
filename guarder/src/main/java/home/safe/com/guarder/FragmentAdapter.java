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

    // FragmentSearch에 전화부, 지킴이 목록을 셋팅(액티비티에서 작업)
    public void setSearchListInFmSearch(ArrayList<GuarderVO> searchList, ArrayList<GuarderVO> guarderList) {
        fragmentSearch.setList(searchList, guarderList);
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

    // FragmentGuarder에서 추가된 지킴이들을 담은 리스트를 셋팅(액티비티에서 작업)
    public void setAddGuarderListInFmGuarder(ArrayList<GuarderVO> list){
        fragmentGuarders.addGuarderList(list);
    }

    // FragmentGuarder로부터 지킴이 리스트를 가져옴(액티비티에서 작업)
    public ArrayList<GuarderVO> getGuarderListInFmGuarder() {
        return fragmentGuarders.getGuarderList();
    }

    // FragmentGuarder에서 GuarderManager를 셋팅(액티비티에서 작업)
    public void setGuarderManager(Context context) {
        fragmentGuarders.setGuarderManager(context);
    }
}
