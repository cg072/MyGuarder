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


    int adapterTabStat;
    String adapterId = null;

    ArrayList<TestListViewDTO> adaptArrDto = new ArrayList<TestListViewDTO>();

    public AdapterFragTabTrans(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        Log.v("포지션", Integer.toString(position));

        switch (position){

            case 0 :

                Log.v("case0", "확인1");
                return new FragmentTransReg();

            case 1 :

                Log.v("case1", "확인2");
                return new FragmentTransList();

        }


        /*switch (position) {

            case 0 :

                if(adaptArrDto != null){
                    fragmentTransReg.setRegRecvDTO(adaptArrDto);
                }


                fragmentTransReg.fragStat(adapterTabStat);

                if(adapterTabStat == 1){
                    if(adapterId != null){
                        fragmentTransReg.fragId(adapterId);
                    }
                }

                return fragmentTransReg;



            case 1 :

                fragmentTransList.setListRecvDTO(adaptArrDto);

                return fragmentTransList;
        }*/

        return null;
    }


    @Override
    public int getCount() {
        return 2;
    }

    //메인액티비티에서 받은 스탯의 정보로 현재 어댑터의 스탯 정보를 변경
    public void adapterstat(int mainRecvStat){
        this.adapterTabStat = mainRecvStat;

        //정보확인용
        String a = Integer.toString(adapterTabStat);
        Log.v("받은 스탯", a);
    }



    //메인에서 받은 아이디를 얻어옴
    public void adapterId(String mainRecvId){
       this.adapterId = mainRecvId;
    }


}
