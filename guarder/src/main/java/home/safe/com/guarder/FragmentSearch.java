package home.safe.com.guarder;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by hotki on 2017-12-17.
 */

public class FragmentSearch extends Fragment implements ListViewAdapterSearch.SearchListBtnClickListener{

    private ListViewAdapterSearch lvAdapterSearch;
    private ListView lvSearch;
    private ArrayList<ListViewItemSearch> alSearch;
    private EditText etSearch;
    private Button btnSearch;
    ViewGroup rootView = null;
    private ArrayList<ListViewItemSearch> alSearchResult;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);

        lvSearch = (ListView) rootView.findViewById(R.id.lvSearch);
        alSearch = new ArrayList<ListViewItemSearch>();
        //lvAdapterSearch = new ListViewAdapterSearch(rootView.getContext(), R.layout.listview_item_search, alSearch, this);

        etSearch = (EditText) rootView.findViewById(R.id.etSearch);
        btnSearch = (Button) rootView.findViewById(R.id.btnSearch);

        lvSearch = (ListView) rootView.findViewById(R.id.lvSearch);
        // EditText에서 키보드의 검색 버튼을 눌렀을 시, 취하는 행동

        vertualServer();


        etSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH) {
                    // 검색 버튼을 눌렀을 시 해야할 행동

                    sendToServerText(etSearch.getText().toString());
                    searchAdapterUpdate(rootView.getContext(), resultSearch());
                }
                return false;
            }
        });

        // 검색 Button을 눌렀을 시, 취하는 행동
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 검색 버튼을 눌렀을 시 해야할 행동

                sendToServerText(etSearch.getText().toString());
                searchAdapterUpdate(rootView.getContext(), resultSearch());

            }
        });

        return rootView;
    }

    private void sendToServerText(String phone) {
        // 해당 텍스트를 서버로 보내서, 현재 피지킴이의 지킴이가 아닌 상태의 회원 중, 텍스트를 포함한 회원들을 보내준다.
        // 해당 회원의 전화번호or아이디와 텍스트를 보낸다.
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
        ArrayList<ListViewItemSearch> alSearchResult = new ArrayList<ListViewItemSearch>();

        // EditText로부터 필터링할 값 가져옴
        String strSearch = etSearch.getText().toString();

        // 필터링된 값들을 초기화된 alSearchResult에 담는다.
        if(!strSearch.equals("")) {
            for(ListViewItemSearch a : alSearch) {
                if(a.getTvPhone().contains(strSearch) == true) {
                    alSearchResult.add(a);
                }
            }
        }

        // 필터링된 결과값 반환
        return alSearchResult;
    }

    // 회원 목록의 어댑터 갱신
    private void searchAdapterUpdate(Context context, ArrayList<ListViewItemSearch> list) {
        // Adapter 생성 (implements ListViewAdapterSearch.ListBtnClickListener를 하였기때문에, 마지막에 this해도 오류 안남)
        //lvAdapterSearch = new ListViewAdapterSearch(this, R.layout.listview_item_search, alSearch, this);
        lvAdapterSearch = new ListViewAdapterSearch(context, R.layout.listview_item_search, list, this);
        // 변경된 Adapter 적용
        lvAdapterSearch.notifyDataSetChanged();
        // 리스트뷰 참조 및 Adapter 달기
        lvSearch.setAdapter(lvAdapterSearch);
    }

    @Override
    public void onSearchListBtnClick(int position) {
        String phone = alSearch.get(position).getTvPhone();

        // 추가된 것을 회원 전체 목록에서 삭제한다.
        for(ListViewItemSearch a : alSearch) {
            if(a.getTvPhone().equals(phone)){
                alSearch.remove(a);
                /*alSearchResult.remove(position); <- 이게 왜 alSearch까지 지우는지 모르겠음
                searchAdapterUpdate(alSearchResult);*/
                break;
            }
        }

        // 서버로 전화번호를 보내서, 지킴이 목록에 추가한다.

        // 회원 어댑터 갱신
        searchAdapterUpdate(rootView.getContext(), alSearch);
        etSearch.setText("");
        Log.v("나누룸?", String.valueOf(position));
    }


    /*
    *  date     : 2017.11.22
    *  author   : Kim Jong-ha
    *  title    : NameDescCompareGuarders, NameDescCompareSearch 메소드 생성
    *  comment  : 이름순 정렬
    * */
    private class NameDescCompareSearch implements Comparator<ListViewItemSearch> {
        @Override
        public int compare(ListViewItemSearch arg0, ListViewItemSearch arg1) {
            return  arg0.getTvName().compareTo(arg1.getTvName());
        }
    }

    public void vertualServer() {
        //ArrayList<ListViewItemSearch> alSearch = new ArrayList<ListViewItemSearch>();

        ListViewItemSearch listViewItemSearch = new ListViewItemSearch();

        listViewItemSearch.setTvName("지립니다");
        listViewItemSearch.setTvPhone("01034561234");
        alSearch.add(listViewItemSearch);

        listViewItemSearch = new ListViewItemSearch();
        listViewItemSearch.setTvName("오집니다");
        listViewItemSearch.setTvPhone("01083835465");
        alSearch.add(listViewItemSearch);

        listViewItemSearch = new ListViewItemSearch();
        listViewItemSearch.setTvName("개년잌");
        listViewItemSearch.setTvPhone("01085901234");
        alSearch.add(listViewItemSearch);

        listViewItemSearch = new ListViewItemSearch();
        listViewItemSearch.setTvName("우성이잘가");
        listViewItemSearch.setTvPhone("01000901123");
        alSearch.add(listViewItemSearch);

        listViewItemSearch = new ListViewItemSearch();
        listViewItemSearch.setTvName("짜이찌엔우성띄");
        listViewItemSearch.setTvPhone("01080450912");
        alSearch.add(listViewItemSearch);

        listViewItemSearch = new ListViewItemSearch();
        listViewItemSearch.setTvName("별들에게물어봐");
        listViewItemSearch.setTvPhone("01034567123");
        alSearch.add(listViewItemSearch);

        listViewItemSearch = new ListViewItemSearch();
        listViewItemSearch.setTvName("좀가자스트라");
        listViewItemSearch.setTvPhone("01046327401");
        alSearch.add(listViewItemSearch);

        listViewItemSearch = new ListViewItemSearch();
        listViewItemSearch.setTvName("뉴이코씨발");
        listViewItemSearch.setTvPhone("01043441234");
        alSearch.add(listViewItemSearch);

        listViewItemSearch = new ListViewItemSearch();
        listViewItemSearch.setTvName("히히히");
        listViewItemSearch.setTvPhone("01011112222");
        alSearch.add(listViewItemSearch);

        listViewItemSearch = new ListViewItemSearch();
        listViewItemSearch.setTvName("니나노");
        listViewItemSearch.setTvPhone("01033334444");
        alSearch.add(listViewItemSearch);

        listViewItemSearch = new ListViewItemSearch();
        listViewItemSearch.setTvName("릴리리");
        listViewItemSearch.setTvPhone("0168887777");
        alSearch.add(listViewItemSearch);

        listViewItemSearch = new ListViewItemSearch();
        listViewItemSearch.setTvName("지리구요");
        listViewItemSearch.setTvPhone("01083832562");
        alSearch.add(listViewItemSearch);

        listViewItemSearch = new ListViewItemSearch();
        listViewItemSearch.setTvName("오졌구요");
        listViewItemSearch.setTvPhone("01099991111");
        alSearch.add(listViewItemSearch);

        // 정렬
        Collections.sort(alSearch ,new FragmentSearch.NameDescCompareSearch());
    }
}
