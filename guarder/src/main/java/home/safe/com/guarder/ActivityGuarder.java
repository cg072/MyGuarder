package home.safe.com.guarder;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class ActivityGuarder extends AppCompatActivity {

    final static private String TAB_FIRST = "회원 검색";
    final static private String TAB_SECOND = "지킴이 등록/해제";

    private TabLayout tabLayout;
    private ViewPager viewPager;

    String id;

    private boolean checkPermission = false;

    ListView lvGuarders;
    GuarderVO guarderVO;

    ArrayList<GuarderVO> phoneList = null;
    FragmentAdapter fragmentAdapter;

    final private String TAG = "가드";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guarder);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        tabLayout.addTab(tabLayout.newTab().setText(TAB_FIRST), 0, true);
        tabLayout.addTab(tabLayout.newTab().setText(TAB_SECOND), 1);
        tabLayout.addOnTabSelectedListener(tabSelectedListener);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        lvGuarders = (ListView) findViewById(R.id.lvGuarders);

        setTest();

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), id);
        fragmentAdapter.setActivity(this);
        fragmentAdapter.setGuarderListInFmSearch(fragmentAdapter.getGuarderListInFmGuarder());
        viewPager.setAdapter(fragmentAdapter);

        //checkPermission();


    }

    // 키보드 숨기기
    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

            EditText etSearch = (EditText) findViewById(R.id.etSearch);

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

            viewPager.setCurrentItem(tab.getPosition());

            if(tab.getPosition() == 0) {
                fragmentAdapter.resetAddGuarderVOInFmSearch();
                fragmentAdapter.setGuarderListInFmSearch(fragmentAdapter.getGuarderListInFmGuarder());
            } else if (tab.getPosition() == 1){
                fragmentAdapter.updateGuarderListInFmGuarder();
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    // 아이디, 비번 불러옴
    private void loadData() {
        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);

        id =  preferences.getString("MemberID",null);
    }

    // 테스트

    private void setTest() {
        GuarderVO guarderVO = new GuarderVO();
        GuarderManager guarderManager = new GuarderManager(getApplicationContext());

        ArrayList<GuarderVO> resultList = guarderManager.select(GuarderShareWord.TARGET_SERVER, GuarderShareWord.TYPE_SELECT_ALL, null);

        Log.v("테스트", "결과 " + resultList.size());
        if(resultList.size() == 0) {
            guarderVO.setGmcname("T_NAME_1");
            guarderVO.setGmcphone("01011112222");
            guarderVO.setGstate(0);
            guarderVO.setGmid("T_ID_1");

            guarderManager.insert(GuarderShareWord.TARGET_SERVER, guarderVO);
            guarderManager.insert(GuarderShareWord.TARGET_DB, guarderVO);

            guarderVO.setGmcname("T_NAME_2");
            guarderVO.setGmcphone("01022221111");
            guarderVO.setGstate(0);
            guarderVO.setGmid("T_ID_2");

            guarderManager.insert(GuarderShareWord.TARGET_SERVER, guarderVO);
            guarderManager.insert(GuarderShareWord.TARGET_DB, guarderVO);

            guarderVO.setGmcname("T_NAME_3");
            guarderVO.setGmcphone("01033331111");
            guarderVO.setGstate(0);
            guarderVO.setGmid("T_ID_3");

            guarderManager.insert(GuarderShareWord.TARGET_SERVER, guarderVO);
            guarderManager.insert(GuarderShareWord.TARGET_DB, guarderVO);

            guarderVO.setGmcname("T_NAME_4");
            guarderVO.setGmcphone("01011113333");
            guarderVO.setGstate(0);
            guarderVO.setGmid("T_ID_4");

            guarderManager.insert(GuarderShareWord.TARGET_SERVER, guarderVO);
            guarderManager.insert(GuarderShareWord.TARGET_DB, guarderVO);

            guarderVO.setGmcname("root");
            guarderVO.setGmcphone("01033332222");
            guarderVO.setGstate(1);
            guarderVO.setGmcid("root");
            guarderVO.setGmid("root");

            guarderManager.insert(GuarderShareWord.TARGET_SERVER, guarderVO);
            guarderManager.insert(GuarderShareWord.TARGET_DB, guarderVO);
        }





    }

    // 테스트
}


//import home.safe.com.guarder.R;
