package home.safe.com.trans;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
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
* 새로 만들어야 할것
* 1. fragmentTransReg 확인 버튼 클릭시, 디비에 인서트 되어야 한다
* 2. fragmentTransList 변경버튼 추가
* 3. 변경버튼 클릭시 업데이트 되어야 함
*
*
* !!!contentvalue => 맵처럼 키, 벨류로
*
*
* ///////////////////중요사항!!!!!!!!!!!!!!!///////////////////////////
*
*
* 데이터에 대한 정보는 새로운 클래스를 만들어서 처리하기!!!
* 뷰에 관련된것은 기존의것
* 데이터에 관련된 것은 새로운 클래스로!!
* 새로운 클래스 - > 컨트롤러 - > 디비
* 클래스명 : TransManager, NoticeManager
*
* 액티비티클래스 안에서 데이터 가공을 하지 말것!!
*
*
* ////////////메인액티비티에서 transManager생성 안됌 이렇게 쓰지 말것!!///////////////
*
* ///no such table 에러 찾아볼것
* 안되면 강제로 실행해볼것
*
* */


public class ActivityTrans extends AppCompatActivity {

    FragmentTransReg fragmentTransReg = new FragmentTransReg();
    FragmentTransList fragmentTransList = new FragmentTransList();


    android.support.v7.widget.Toolbar toolbarTrans;
    TabLayout tabbarTrans;
    ViewPager vpagerTrans;

    //TransManager transManager = new TransManager(this);


    TransIntegratedVO integratedVO = new TransIntegratedVO();
    ArrayList<TransIntegratedVO> selectedList = new ArrayList<>();

    AdapterFragTabTrans adapterFragTabTrans;


    final private String tag = "이동수단";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);

        tabbarTrans = (TabLayout)findViewById(R.id.tabbarTrans);
        vpagerTrans = (ViewPager)findViewById(R.id.vpagerTrans);

        //플래그먼트를 미리 로딩 시켜놓음
        vpagerTrans.setOffscreenPageLimit(1);

        tabbarTrans.addTab(tabbarTrans.newTab().setText("이동수단등록"), 0 , true);
        tabbarTrans.addTab(tabbarTrans.newTab().setText("이동수단내역"), 1);

        //transManager = new TransManager(this);

        //어댑터세팅
        //vpagerTrans.setAdapter(new AdapterFragTabTrans(getSupportFragmentManager(), this));
        //vpagerTrans.setAdapter(new AdapterFragTabTrans(getSupportFragmentManager(), transManager, this));

///////////////////////////////
        adapterFragTabTrans = new AdapterFragTabTrans(getSupportFragmentManager());

        //adapterFragTabTrans.setFragList(selectedList);

        vpagerTrans.setAdapter(adapterFragTabTrans);

/////////////////////////////////


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
            Log.v("탭포지션", Integer.toString(tab.getPosition()));

            // 리스트페이지로 갈때, 디비헬퍼로 리스트 가져와야 함!!

            if(tab.getPosition() == 1){

                TransManager transManager = new TransManager(getApplicationContext());

                ArrayList<TransIntegratedVO> resultList = new ArrayList<TransIntegratedVO>();

                resultList = transManager.select();

                setList(resultList);
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }


    };

    private void setList(ArrayList<TransIntegratedVO> list){
        adapterFragTabTrans.setToFragList(list);
    }

}
