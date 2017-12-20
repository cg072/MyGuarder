package home.safe.com.guarder;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

//import home.safe.com.guarder.R;


public class ActivityGuarder extends AppCompatActivity {

    final static private String TAB_FIRST = "회원 검색";
    final static private String TAB_SECOND = "지킴이 등록/해제";

    private TabLayout tabLayout;
    private ViewPager viewPager;

    ListView lvGuarders;
    ListViewItemSearch lvItemSearch;
    ListViewItemGuarders lvItemGuarders;
    ListViewAdapterSearch lvAdapterSearch;
    ListViewAdapterGuarders lvAdapterGuarders;
    String nowGuarderName;
    String nowGuarderPhone;

    ArrayList<ListViewItemSearch> alSearch = null;
    ArrayList<ListViewItemSearch> alSearchResult = null;
    ArrayList<ListViewItemGuarders> alGuarders = null;

    final private String TAG = "가드";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guarder);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        tabLayout.addTab(tabLayout.newTab().setText(TAB_FIRST), 0, true);
        tabLayout.addTab(tabLayout.newTab().setText(TAB_SECOND), 1);
        tabLayout.addOnTabSelectedListener(tabSelectedListener);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        lvGuarders = (ListView) findViewById(R.id.lvGuarders);

        alGuarders = new ArrayList<ListViewItemGuarders>();
    }

    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

            EditText etSearch = (EditText) findViewById(R.id.etSearch);

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    /*
     *  date     : 2017.11.27
     *  author   : Kim Jong-ha
     *  title    : alSearchResult() 메소드 생성
     *  comment  : 단어를 필터링해주고, 필터링된 ArrayList를 반환한다.
     *             본래는 서버로 String를 보내고, 필터링된 값들을 리스트에 담아야한다.
     *  return   : ArrayList<ListViewItemSearch>
     * */
    private ArrayList<ListViewItemSearch> resultSearch() {

        // 결과 담는 ArrayList 초기화
        alSearchResult = new ArrayList<ListViewItemSearch>();

        // EditText로부터 필터링할 값 가져옴
        //String strSearch = etSearch.getText().toString();

        // 필터링된 값들을 초기화된 alSearchResult에 담는다.
/*        for(ListViewItemSearch a : alSearch) {
            if(a.getTvPhone().contains(strSearch) == true || a.getTvName().contains(strSearch) == true) {
                alSearchResult.add(a);
            }
        }*/

        // 필터링된 결과값 반환
        return alSearchResult;
    }

    /*
     *  date     : 2017.11.22
     *  author   : Kim Jong-ha
     *  title    : hyphenRemove() 메소드 생성
     *  comment  : 전화 번호 사이의 '-' 를 제거한다
     *  return   : String 형태
     * */
    private String hyphenRemove(String phone) {

            String[] basePhone = phone.split("-");

            Log.v(TAG, "나눔"+basePhone[0].length()+ " " + basePhone[0]);
            String resultPhone = basePhone[0];
            if(basePhone[0].length() < 10) {
                resultPhone = resultPhone + basePhone[1] + basePhone[2];
            }

           return resultPhone;
    }

    // ArrayList에 데이터를 로드하는 loadItemsFromDB() 함수를 추가
    // 현재 내용은 직접 DB를 다루진 않고 리스트에 값을 저장해서 리턴시킴킴
   /*
    *  date     : 2017.11.19
    *  author   : Kim Jong-ha
    *  title    : getList 메소드 생성
    *  comment  : 시험용 리스트
    * */
    private void listSetting() {
        /* 코딩될 내용들
            1. 버튼 눌렀을 때, edittext에서 getText 후 스트링으로 바꾼 다음, 서버로 보내기
            2. 리턴 값을 받아서 화면에 뿌려주기 <- 뿌려주는건 대충 구현됨
            3. 지킴이 목록에 있는 회원은 보여주지 않는다.
         */
        // 예시용 목록 회원
        //Collections.sort(alReturnList ,new ActivityGuarder.NameDescCompareSearch());

        //alSearch = alReturnList;
        //alSearchResult = alSearch;

        // 지킴이 어댑터 갱신
        //guarderAdapterUpdate();
        //searchAdapterUpdate(searchUpdate(alSearch, alGuarders));
        //searchUpdate(alSearch, alGuarders);

    }



    /*
    *  date     : 2017.11.20
    *  author   : Kim Jong-ha
    *  title    : guarderSearchUpdate(ArrayList<ListViewItemSearch> alSearch, ArrayList<ListViewItemGuarders> alGuarders 메소드 생성
    *  comment  : Activity 로딩 시, 회원 목록과 지킴이 목록의 중첩을 검색 후, 회원목록에서 삭제한다.
    *  return   : ArrayList<ListViewItemSearch> 형태
    * */
/*    private ArrayList<ListViewItemSearch> searchUpdate(ArrayList<ListViewItemSearch> searchs, ArrayList<ListViewItemGuarders> guarders) {

        Log.v("사이즈", String.valueOf(searchs.size()));

        for( int i = searchs.size() - 1 ;  i >= 0 ; i -- ) {
            for( int j = 0 ; j < guarders.size() ; j++ ) {
                if(  searchs.get(i).getTvPhone().equals(guarders.get(j).getTvPhone())){
                    searchs.remove(i);
                    break;
                }
            }
        }
        return searchs;
    }*/
/*

    // 회원 리스트에서 등록 버튼을 눌렀을 시
    *//*
    *  date     : 2017.11.20
    *  author   : Kim Jong-ha
    *  title    : onSearchListBtnClick(int position, ListViewItemSearch lvItemSearch) 메소드 생성
    *  comment  : 회원 목록에 있는 '등록' Button을 눌렀을 시의 작동 코딩.
    * *//*
    @Override
    public void onSearchListBtnClick(int position) {

        // 등록을 클릭한 해당 postion의 리스트의 아이템(name, phone)을 가져온다.
        String name = alSearchResult.get(position).getTvName();
        String phone = alSearchResult.get(position).getTvPhone();

        // 지킴이 목록에 추가
        guarderAdd(name, phone);

        // 지킴이 어댑터 갱신
        guarderAdapterUpdate();

        // 추가된 것을 회원 전체 목록에서 삭제한다.
        for(ListViewItemSearch a : alSearch) {
            if(a.getTvPhone().equals(phone)){
                alSearch.remove(a);
                *//*alSearchResult.remove(position); <- 이게 왜 alSearch까지 지우는지 모르겠음
                searchAdapterUpdate(alSearchResult);*//*
                break;
            }
        }

        // 회원 어댑터 갱신
        searchAdapterUpdate(alSearch);
        etSearch.setText("");
    }


    // 지킴이 리스트에서 슬라이딩 버튼을 눌렀을 시
    *//*
    *  date     : 2017.11.20
    *  author   : Kim Jong-ha
    *  title    : onGuardersListBtnClick(int position, int count) 메소드 생성
    *  comment  : 지킴이 목록에 있는 SlidingButton을 눌렀을 경우의 작동 코딩
    *             지킴이는 1명만 지정이 가능하기 때문에, 지킴이로 지정된 ListView의 item을 제외하고는 모두 Button 상태가 false가 되어야한다.
    * *//*
    @Override
    public void onGuardersListBtnClick(int position, int count) {

        // count는 지킴이 목록의 숫자를 의미
        for( int i = 0 ; i < count ; i++ ) {

            if( i != position) {
                // 해당 position이 아니라면 button을 비활성화 시킨다
                alGuarders.get(i).setUse(false);

            } else {
                // Button Off 상태
                if(alGuarders.get(i).getUse() == false) {
                    // 해당 position이라면 button을 활성화 시키고, name, phone을 가져온다.
                    alGuarders.get(i).setUse(true);
                    nowGuarderName = alGuarders.get(i).getTvName();
                    nowGuarderPhone = alGuarders.get(i).getTvPhone();
                    Toast.makeText(this, nowGuarderName + " 님이 지킴이로 설정되었습니다.", Toast.LENGTH_SHORT).show();

                // Button On 상태
                } else {
                    // 해당 position이라면 button을 활성화 시키고, name, phone을 가져온다.
                    alGuarders.get(i).setUse(false);
                    nowGuarderName = null;
                    nowGuarderPhone = null;
                    Toast.makeText(this, "현재 지킴이가 없습니다.", Toast.LENGTH_SHORT).show();
                }

                //위의 nowGuarder부분을 다른 액티비티, 서버로 전송하여 준다.
            }
        }
        // button을 누름으로써, 변경된 내역을 기반으로 지킴이 목록 갱신
        guarderAdapterUpdate();
    }



    // 지킴이 목록의 어댑터 갱신
    private void guarderAdapterUpdate() {
        // Adapter 생성 (implements ListViewAdapterSearch.ListBtnClickListener를 하였기때문에, 마지막에 this해도 오류 안남)
        lvAdapterGuarders = new ListViewAdapterGuarders(this, R.layout.listview_item_guarders, alGuarders, this);
        // 변경된 Adapter 적용
        lvAdapterGuarders.notifyDataSetChanged();
        // 리스트뷰 참조 및 Adapter 달기
        lvGuarders.setAdapter(lvAdapterGuarders);
    }*/
}
