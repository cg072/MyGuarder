package home.safe.com.trans;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ContextMenu;
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

    /*해야할 일 :
                1. 스위치문 고치기!!!!! xxxxxx
                2. 이동수단 내역 보기와 등록하는 영역을 구분해주기!!!
                3. 에디트텍스트 영역 늘려주고 구분선 지어주기
                4. 이동수단 클릭시 팝업메뉴 키우기 혹은 컨텍스트메뉴로 변경하기!!!!
                5. 확인 버튼 클릭시, 부가정보입력 된 텍스트 저장하기!!!
                6. 내역보기 클릭시 디비에서 정보 받아오기!!!!
                7. 확인버튼 클릭시 디비에 저장 하기!!!!!
                */

/*public class ActivityTrans extends AppCompatActivity implements View.OnClickListener {

    //리스트, 선택 영역의 텍스트뷰 선언
    TextView transList;
    TextView transSelect;
    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);

        //리스트, 선택 영역의 아이디를 가져오기
        transList = (TextView) findViewById(R.id.transList);
        transSelect = (TextView) findViewById(R.id.transSelect);
        btnOk = (Button) findViewById(R.id.btnOk);


        //리스트, 선택 영역에 온클릭리스너 세팅
        transList.setOnClickListener(this);
        transSelect.setOnClickListener(this);


    }


    //클릭 이벤트
    @Override
    public void onClick(View view) {
        //리스트버튼 클릭
        if (view == transList) {

            //인텐트로 보내주기 위한 객체 어레이 리스트 생성
            ArrayList<TestListViewDTO> arrtest = new ArrayList<>();
            //어레이 리스트에 dto 값을 넣어둠
            arrtest.add(new TestListViewDTO("1", "택시", "모범"));
            arrtest.add(new TestListViewDTO("2", "버스", "좌석"));
            arrtest.add(new TestListViewDTO("3", "대리", "대리"));
            arrtest.add(new TestListViewDTO("4", "지하철", "지하철"));

            //ActivityTransListPopup.class로 인텐트를 전송
            Intent intentPopup = new Intent(this, ActivityTransListPopup.class);


            //인텐트에 dto객체 어레이 리스트를 부가로 전송
            intentPopup.putExtra("dtotest" , arrtest);

            startActivity(intentPopup);
        }


        //셀렉트버튼 클릭
        if (view == transSelect) {
            //팝업 메뉴 만들어주기
            PopupMenu popupTransSelect = new PopupMenu(this, view);
            //팝업메뉴 인플레이트
            MenuInflater inflater = popupTransSelect.getMenuInflater();
            Menu menu = popupTransSelect.getMenu();
            inflater.inflate(R.menu.menu_transselect, menu);
            popupTransSelect.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.transWalk:
                            Toast.makeText(getApplicationContext(), "도보", Toast.LENGTH_LONG).show();
                            transSelect.setText("도보");
                        break;

                        case R.id.transBus:
                            Toast.makeText(getApplicationContext(), "버스", Toast.LENGTH_LONG).show();
                            transSelect.setText("버스");
                        break;

                        case R.id.transTaxi:
                            Toast.makeText(getApplicationContext(), "택시", Toast.LENGTH_LONG).show();
                            transSelect.setText("택시");
                        break;

                        case R.id.transSub:
                            Toast.makeText(getApplicationContext(), "지하철", Toast.LENGTH_LONG).show();
                            transSelect.setText("지하철");
                        break;

                        case R.id.transEtc:
                            Toast.makeText(getApplicationContext(), "기타", Toast.LENGTH_LONG).show();
                            transSelect.setText("기타");
                        break;

                    }

                    return false;
                }
            });

            popupTransSelect.show();

        }


    }


}*/

/*
* 해야할일!!
* 1. 이동수단내역 리스트뷰에 시간을 추가해주기
* 2. 이동수단내역 확인 버튼 클릭시 전 화면으로 돌아가게 만들어 주기
* 3. 이동수단등록 fragmaent_transreg.xml의 콘텐츠를 weight가 아닌 수동 크기로 주기!! 최소사이즈 지정!!
* */


public class ActivityTrans extends AppCompatActivity {


    FragmentTransReg fragmentTransReg;
    FragmentTransList fragmentTransList;
    android.support.v7.widget.Toolbar toolbarTrans;
    TabLayout tabbarTrans;
    ViewPager vpagerTrans;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);

        tabbarTrans = (TabLayout)findViewById(R.id.tabbarTrans);
        vpagerTrans = (ViewPager)findViewById(R.id.vpagerTrans);

        vpagerTrans.setAdapter(new AdapterFragTabTrans(getSupportFragmentManager()));
        vpagerTrans.setOffscreenPageLimit(1);

        tabbarTrans.addTab(tabbarTrans.newTab().setText("이동수단등록"), 0 , true);
        tabbarTrans.addTab(tabbarTrans.newTab().setText("이동수단내역"), 1);

        /*Bundle bundle = new Bundle(1);*/

        tabbarTrans.addOnTabSelectedListener(tabSelectedListener);

        vpagerTrans.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabbarTrans));






        ////////////////////두번째코드시작////////////////////////

       /* //툴바의 아이디를 찾아옴
        toolbarTrans = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbarTrans);
        //액션바에 툴바를 세팅
        setSupportActionBar(toolbarTrans);

        //탭바의 아이디를 찾아옴
        tabbarTrans = (TabLayout) findViewById(R.id.tabbarTrans);

        //뷰페이저 아이디를 찾아옴
        vpagerTrans = (ViewPager) findViewById(R.id.vpagerTrans);

        vpagerTrans.setAdapter(new AdapterFragMoveTrans(getSupportFragmentManager()));
        vpagerTrans.setOffscreenPageLimit(1);

        //탭바 안에 제목을 붙여주고 페이지를 등록
        tabbarTrans.addTab(tabbarTrans.newTab().setText("이동수단등록"), 0, true);
        tabbarTrans.addTab(tabbarTrans.newTab().setText("이동수단내역"), 1);

        Log.v("됩니까", "돼요?");

        tabbarTrans.addOnTabSelectedListener(tabSelectedListener);

        vpagerTrans.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabbarTrans));

        //프래그먼트 인스턴스를 생성
        fragmentTransReg = new FragmentTransReg();
        fragmentTransList = new FragmentTransList();*/

       ////////////////////두번째코드 끝///////////////////



        ////////////////첫번째코드시작///////////////////



        /*//첫번째 화면에 이동수단등록 프래그먼트를 띄움
        getSupportFragmentManager().beginTransaction().add(R.id.transContainer, fragmentTransReg).commit();


        tabbarTrans.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                android.support.v4.app.Fragment selected = null;

                if(position == 0){
                    selected = fragmentTransReg;
                }else if(position == 1){
                    selected = fragmentTransList;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.transContainer, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

        //////////////////첫번째코드끝//////////////////////

    }

    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

            vpagerTrans.setCurrentItem(tab.getPosition());

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

}
