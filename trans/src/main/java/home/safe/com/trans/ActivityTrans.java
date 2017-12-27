package home.safe.com.trans;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
    /*2017. 11. 03
    author 박준규
    이동수단 설정 -> 메인
    이동수단의 내역 버튼클릭(이동수단 내역에 대한 새로운 액티비티 띄움)
    이동수단 등록 -> 팝업메뉴(menu_transelect.xml) 띄움
    부가정보 입력 -> 부가정보 내용 저장
    확인버튼 클릭 -> 메인액티비티로 이동
    */



/*
* 해야할일!!
* 1. 이동수단내역 리스트뷰에 시간을 추가해주기
* 2. 이동수단내역 확인 버튼 클릭시 전 화면으로 돌아가게 만들어 주기
* 3. 이동수단등록 fragmaent_transreg.xml의 콘텐츠를 weight가 아닌 수동 크기로 주기!! 최소사이즈 지정!!
* */

/*
* 클래스를 새로 생성!!
* 액티비티는 - > 뷰만
* 디비연동할때는 새로 만든 클래스!!
* 새로만든 클래스를 피쥐체인저로
*
* 만들어야 할것
* 디비매니져 -> 접두어 trans
* 디비매니저에 pgchager - > pretest에 있는 것들 만들어야 함
*
*
*
*
*
* */


public class ActivityTrans extends AppCompatActivity {


    FragmentTransReg fragmentTransReg;
    FragmentTransList fragmentTransList;
    android.support.v7.widget.Toolbar toolbarTrans;
    TabLayout tabbarTrans;
    ViewPager vpagerTrans;


    AdapterFragTabTrans adapterFragTabTrans;

    //지킴이 피지킴이를 구분하기 위한 플래그
    int mainStat = 1;
    String mainId = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);

        tabbarTrans = (TabLayout)findViewById(R.id.tabbarTrans);
        vpagerTrans = (ViewPager)findViewById(R.id.vpagerTrans);

        adapterFragTabTrans = new AdapterFragTabTrans(getSupportFragmentManager());



        //받은 지킴/피지킴이의 스탯의 정보를 기본으로 어댑터를 다르게 생성
        if(mainStat == 0){
            adapterFragTabTrans.adapterstat(0);
            vpagerTrans.setAdapter(adapterFragTabTrans);
        }
        if(mainStat == 1){
            mainId = "김종하";
            adapterFragTabTrans.adapterId(mainId);
            adapterFragTabTrans.adapterstat(1);
            vpagerTrans.setAdapter(adapterFragTabTrans);
        }

        //vpagerTrans.setAdapter(new AdapterFragTabTrans(getSupportFragmentManager()));

        //플래그먼트를 미리 로딩 시켜놓음
        vpagerTrans.setOffscreenPageLimit(1);

        tabbarTrans.addTab(tabbarTrans.newTab().setText("이동수단등록"), 0 , true);
        tabbarTrans.addTab(tabbarTrans.newTab().setText("이동수단내역"), 1);


        tabbarTrans.addOnTabSelectedListener(tabSelectedListener);

        vpagerTrans.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabbarTrans));

    }

    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {

            //페이지 슬라이딩 시, 키보드를 없애기 위한 코드
            EditText etTextTrans = (EditText)findViewById(R.id.etTextTrans);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etTextTrans.getWindowToken(), 0);


            vpagerTrans.setCurrentItem(tab.getPosition());

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };



    //쉐어드프리페어드 메소드

    /*public void getShared(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);

        *//*this.mainStat*//*

    }

    public void toShared(String sharedkind, String sharedtext){

        SharedPreferences preferences = getContext().getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("TransName", sharedkind);
        editor.putString("TransMemo", sharedtext);
        editor.commit();
    }*/



}
