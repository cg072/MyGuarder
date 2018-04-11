package home.safe.com.trans;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;
    /*2017. 11. 03
    author 박준규
    이동수단 설정 -> 메인
    이동수단의 내역 버튼클릭(이동수단 내역에 대한 새로운 액티비티 띄움)
    이동수단 등록 -> 팝업메뉴(menu_transelect.xml) 띄움
    부가정보 입력 -> 부가정보 내용 저장
    확인버튼 클릭 -> 메인액티비티로 이동
    */
public class ActivityTrans extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbarTrans;
    TabLayout tabbarTrans;
    ViewPager vpagerTrans;

    TransIntegratedVO integratedVO = new TransIntegratedVO();
    ArrayList<TransIntegratedVO> selectedList = new ArrayList<>();

    AdapterFragTabTrans adapterFragTabTrans;


    final private String tag = "이동수단";

    //kch
    NetworkTask networkTask;
    HttpResultListener listener;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);

        tabbarTrans = (TabLayout)findViewById(R.id.tabbarTrans);
        vpagerTrans = (ViewPager)findViewById(R.id.vpagerTrans);

        loadData();

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

        listener = new HttpResultListener() {
            @Override
            public void onPost(String result) {

                if(!"401".equals(result.replace("/n",""))) {
                    String[] str = (result.replace("/n", "")).split("&");

                    ArrayList<TransIntegratedVO> resultList = new ArrayList<TransIntegratedVO>();

                    for (String s : str) {
                        String[] cul = s.split("/");

                        TransIntegratedVO vo = new TransIntegratedVO();
                        vo.setTtype(cul[0]);
                        vo.setTmemo(cul[1]);
                        vo.setTday(cul[2]);

                        Log.d("kch", vo.getTtype() + " - " + vo.getTmemo() + " - " + vo.getTday());

                        resultList.add(vo);
                    }

                    setList(resultList);
                }
            }
        };

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

                // kch - 서버에서 이동수단 리스트 가져오기

                networkTask = new NetworkTask(getApplicationContext(), listener);
                networkTask.strUrl = NetworkTask.HTTP_IP_PORT_PACKAGE_STUDY;
                networkTask.params= NetworkTask.CONTROLLER_TRANS_DO+ NetworkTask.METHOD_GET_TRANS_LIST + "&rmid="+id;
                networkTask.execute();
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

    private void loadData()
    {
        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        id = preferences.getString("MemberID","-");
    }
}
