package home.safe.com.guarder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
//import home.safe.com.guarder.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ActivityGuarder extends AppCompatActivity implements ListViewAdapterSearch.SearchListBtnClickListener, ListViewAdapterGuarders.GuardersListBtnClickListener{

    EditText etSearch;
    Button btnSearch;
    ListView lvSearch;
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

        etSearch = (EditText) findViewById(R.id.etSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);

        lvSearch = (ListView) findViewById(R.id.lvSearch);
        lvGuarders = (ListView) findViewById(R.id.lvGuarders);

        alGuarders = new ArrayList<ListViewItemGuarders>();

        // 기기에 있는 전화번호를 불러온다. 추후에는 기기에 있는 번호를 보내는 메소드가 필요함
        // private ArrayList<ListViewItemSearch> listSetting로 값을 반환하여 서버에 보내야한다.

        // 서버와 연결 후 부터는 서버로부터 받아서 세팅하는 메소드
        listSetting();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 검색 버튼을 눌렀을 시 해야할 행동

                sendToServerText(etSearch.getText().toString());

                searchAdapterUpdate(resultSearch());

           }
        });
    }

    private void sendToServerText(String phone) {

    }
    private void sendToServerVO() {

    }

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
        String strSearch = etSearch.getText().toString();

        // 필터링된 값들을 초기화된 alSearchResult에 담는다.
        for(ListViewItemSearch a : alSearch) {
            if(a.getTvPhone().contains(strSearch) == true || a.getTvName().contains(strSearch) == true) {
                alSearchResult.add(a);
            }
        }

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
        ArrayList<ListViewItemSearch> alReturnList = new ArrayList<ListViewItemSearch>();

        ListViewItemSearch listViewItemSearch = new ListViewItemSearch();

        listViewItemSearch.setTvName("히히히");
        listViewItemSearch.setTvPhone("01011112222");
        alReturnList.add(listViewItemSearch);

        listViewItemSearch = new ListViewItemSearch();

        listViewItemSearch.setTvName("니나노");
        listViewItemSearch.setTvPhone("01033334444");
        alReturnList.add(listViewItemSearch);

        listViewItemSearch = new ListViewItemSearch();

        listViewItemSearch.setTvName("릴리리");
        listViewItemSearch.setTvPhone("0168887777");
        alReturnList.add(listViewItemSearch);

        listViewItemSearch = new ListViewItemSearch();

        listViewItemSearch.setTvName("지리구요");
        listViewItemSearch.setTvPhone("01083832562");
        alReturnList.add(listViewItemSearch);

        listViewItemSearch = new ListViewItemSearch();

        listViewItemSearch.setTvName("오졌구요");
        listViewItemSearch.setTvPhone("01099991111");
        alReturnList.add(listViewItemSearch);

        Collections.sort(alReturnList ,new NameDescCompareSearch());

        alSearch = alReturnList;
        alSearchResult = alSearch;

        Log.v("어디냐1","ㅇ");
        for(ListViewItemSearch a : alSearch) {
            Log.v(a.getTvName(), a.getTvPhone());
        }


        // 예시용 목록 2 지킴이
        ListViewItemGuarders listViewItemGuarders = new ListViewItemGuarders();

        listViewItemGuarders.setTvName("지리구요");
        listViewItemGuarders.setTvPhone("01083832562");
        listViewItemGuarders.setUse(true);
        alGuarders.add(listViewItemGuarders);

        listViewItemGuarders = new ListViewItemGuarders();

        listViewItemGuarders.setTvName("오졌구요");
        listViewItemGuarders.setTvPhone("01099991111");
        listViewItemGuarders.setUse(false);
        alGuarders.add(listViewItemGuarders);

        // 지킴이 어댑터 갱신

        guarderAdapterUpdate();
        searchAdapterUpdate(searchUpdate(alSearch, alGuarders));
        //searchUpdate(alSearch, alGuarders);

    }

    // ArrayList에 지킴이를 추가함 [서버와 연결 후 부터는 서버로부터 받아와야한다.]
    private void guarderAdd(String name, String phone) {
        lvItemGuarders = new ListViewItemGuarders();
        lvItemGuarders.setTvName(name);
        lvItemGuarders.setTvPhone(phone);
        lvItemGuarders.setUse(false);

        alGuarders.add(lvItemGuarders);

        Collections.sort(alGuarders ,new NameDescCompareGuarders());
        // 역방향 시 Collections.reverse 로 해주면 된다
    }

    /**
     * 이름 내림차순
     * @author falbb
     *
     */
    private class NameDescCompareGuarders implements Comparator<ListViewItemGuarders> {
        @Override
        public int compare(ListViewItemGuarders arg0, ListViewItemGuarders arg1) {
            return  arg0.getTvName().compareTo(arg1.getTvName());
        }
    }

    private class NameDescCompareSearch implements Comparator<ListViewItemSearch> {
        @Override
        public int compare(ListViewItemSearch arg0, ListViewItemSearch arg1) {
            return  arg0.getTvName().compareTo(arg1.getTvName());
        }
    }

    /*
*  date     : 2017.11.20
*  author   : Kim Jong-ha
*  title    : guarderSearchUpdate(ArrayList<ListViewItemSearch> alSearch, ArrayList<ListViewItemGuarders> alGuarders 메소드 생성
*  comment  : Activity 로딩 시, 회원 목록과 지킴이 목록의 중첩을 검색 후, 회원목록에서 삭제한다.
*  return   : ArrayList<ListViewItemSearch> 형태
* */
    private ArrayList<ListViewItemSearch> searchUpdate(ArrayList<ListViewItemSearch> searchs, ArrayList<ListViewItemGuarders> guarders) {

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
    }


    // 회원 리스트에서 등록 버튼을 눌렀을 시
    /*
    *  date     : 2017.11.20
    *  author   : Kim Jong-ha
    *  title    : onSearchListBtnClick(int position, ListViewItemSearch lvItemSearch) 메소드 생성
    *  comment  : 회원 목록에 있는 '등록' Button을 눌렀을 시의 작동 코딩.
    * */
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
                /*alSearchResult.remove(position); <- 이게 왜 alSearch까지 지우는지 모르겠음
                searchAdapterUpdate(alSearchResult);*/
                break;
            }
        }

        // 회원 어댑터 갱신
        searchAdapterUpdate(alSearch);
    }


    // 지킴이 리스트에서 슬라이딩 버튼을 눌렀을 시
    /*
    *  date     : 2017.11.20
    *  author   : Kim Jong-ha
    *  title    : onGuardersListBtnClick(int position, int count) 메소드 생성
    *  comment  : 지킴이 목록에 있는 SlidingButton을 눌렀을 경우의 작동 코딩
    *             지킴이는 1명만 지정이 가능하기 때문에, 지킴이로 지정된 ListView의 item을 제외하고는 모두 Button 상태가 false가 되어야한다.
    * */
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

    // 회원 목록의 어댑터 갱신
    private void searchAdapterUpdate(ArrayList<ListViewItemSearch> list) {
        // Adapter 생성 (implements ListViewAdapterSearch.ListBtnClickListener를 하였기때문에, 마지막에 this해도 오류 안남)
        //lvAdapterSearch = new ListViewAdapterSearch(this, R.layout.listview_item_search, alSearch, this);
        lvAdapterSearch = new ListViewAdapterSearch(this, R.layout.listview_item_search, list, this);
        // 변경된 Adapter 적용
        lvAdapterSearch.notifyDataSetChanged();
        // 리스트뷰 참조 및 Adapter 달기
        lvSearch.setAdapter(lvAdapterSearch);
    }

    // 지킴이 목록의 어댑터 갱신
    private void guarderAdapterUpdate() {
        // Adapter 생성 (implements ListViewAdapterSearch.ListBtnClickListener를 하였기때문에, 마지막에 this해도 오류 안남)
        lvAdapterGuarders = new ListViewAdapterGuarders(this, R.layout.listview_item_guarders, alGuarders, this);
        // 변경된 Adapter 적용
        lvAdapterGuarders.notifyDataSetChanged();
        // 리스트뷰 참조 및 Adapter 달기
        lvGuarders.setAdapter(lvAdapterGuarders);
    }

    private void getGuarderVO() {

    }

    private void setGuarderVO() {

    }
}
