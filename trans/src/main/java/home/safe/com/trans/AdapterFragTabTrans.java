package home.safe.com.trans;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by plupin724 on 2017-12-03.
 */

public class AdapterFragTabTrans extends FragmentStatePagerAdapter {

    FragmentTransReg fragmentTransReg;
    FragmentTransList fragmentTransList;

    int adapterTabStat;
    TestListViewDTO recvDto;


    public AdapterFragTabTrans(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        /*switch (position){

            case 0 :
                return new FragmentTransReg();

            case 1 :
                return new FragmentTransList();

        }*/

        switch (position) {
            case 0 :
                fragmentTransReg = new FragmentTransReg();
                fragmentTransReg.fragStat(adapterTabStat);
                return fragmentTransReg;

            case 1 :

                return new FragmentTransList();


                /*getDto();
                fragmentTransList = new FragmentTransList();*/

        }



        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    //메인액티비티에서 받은 스탯의 정보로 현재 어댑터의 스탯 정보를 변경
    public void adapterstat(int mainRecvStat){
        this.adapterTabStat = mainRecvStat;

        String a = Integer.toString(adapterTabStat);
        Log.v("받은 스탯", a);
    }

    //fragementreg에서 작성된 정보를 불러오는 메소드

    public void getDto(){
        this.recvDto = new TestListViewDTO();
        this.recvDto = fragmentTransReg.dto;
    }

}
