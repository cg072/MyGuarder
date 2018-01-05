package home.safe.com.guarder;

import android.content.ContentValues;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by hotki on 2017-12-17.
 */

public class FragmentSearch extends Fragment implements ListViewAdapterSearch.SearchListBtnClickListener{

    private ListViewAdapterSearch lvAdapterSearch;
    private ListView lvSearch;
    private ArrayList<GuarderVO> alSearch;
    private ArrayList<GuarderVO> alGuarders;
    private EditText etSearch;
    private Button btnSearch;
    ViewGroup rootView = null;
    private ArrayList<GuarderVO> alSearchResult;
    private FragmentGuarders fragmentGuarders;
    GuarderManager guarderManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.v("크레이트뷰", "시작");
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);

        lvSearch = (ListView) rootView.findViewById(R.id.lvSearch);
        etSearch = (EditText) rootView.findViewById(R.id.etSearch);
        btnSearch = (Button) rootView.findViewById(R.id.btnSearch);

        lvSearch = (ListView) rootView.findViewById(R.id.lvSearch);
        // EditText에서 키보드의 검색 버튼을 눌렀을 시, 취하는 행동

        etSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH) {
                    // 검색 버튼을 눌렀을 시 해야할 행동

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


                searchAdapterUpdate(rootView.getContext(), resultSearch());

            }
        });
        Log.v("크레이트뷰", "끝");
        return rootView;
    }

    private boolean sendToServerText(GuarderVO guarderVO) {
        // 해당 텍스트를 서버로 보내서, 현재 피지킴이의 지킴이가 아닌 상태의 회원 중, 텍스트를 포함한 회원들을 보내준다.
        // 해당 회원의 전화번호or아이디와 텍스트를 보낸다.
        boolean check = false;

        // 회원중 검색이라 member에서 검색해야한다고 생각함

        // 서버로부터 값을 받고나서 회원일 경우 check값에 true를 넣는다.
        /*
            미구현 영역
        */
        check = true;

        return check;
    }

    /*
     *  date     : 2017.11.27
     *  author   : Kim Jong-ha
     *  title    : alSearchResult() 메소드 생성
     *  comment  : 단어를 필터링해주고, 필터링된 ArrayList를 반환한다.
     *             본래는 서버로 String를 보내고, 필터링된 값들을 리스트에 담아야한다.
     *  return   : ArrayList<ListViewItemSearch>
     * */
    private ArrayList<GuarderVO> resultSearch() {
        Log.v("필터링", "시작");
        // 결과 담는 ArrayList 초기화
        ArrayList<GuarderVO> resultList = new ArrayList<GuarderVO>();

        // EditText로부터 필터링할 값 가져옴
        String strSearch = etSearch.getText().toString();

        // 필터링된 값들을 초기화된 resultList 담는다.
        if(!strSearch.equals("")) {
            for(GuarderVO a : alSearch) {
                if(a.getGmcphone().contains(strSearch) == true) {
                    resultList.add(a);
                }
            }
        }

        alSearchResult = searchUpdate(resultList, fragmentGuarders.getList());

        // 필터링된 결과값 반환
        return alSearchResult;
    }

    // 회원 목록의 어댑터 갱신
    private void searchAdapterUpdate(Context context, ArrayList<GuarderVO> list) {
        // Adapter 생성 (implements ListViewAdapterSearch.ListBtnClickListener를 하였기때문에, 마지막에 this해도 오류 안남)
        lvAdapterSearch = new ListViewAdapterSearch(context, R.layout.listview_item_search, list, this);
        // 변경된 Adapter 적용
        lvAdapterSearch.notifyDataSetChanged();
        // 리스트뷰 참조 및 Adapter 달기
        lvSearch.setAdapter(lvAdapterSearch);
    }

    @Override
    public void onSearchListBtnClick(int position) {

        String name = alSearchResult.get(position).getGmcname();
        String phone = alSearchResult.get(position).getGmcphone();
        alSearchResult.remove(position);


        GuarderVO guarderVO = new GuarderVO();
        guarderVO.setGmcname(name);
        guarderVO.setGmcphone(phone);

        if(sendToServerText(guarderVO) == true) {
            // 서버로 전화번호를 보내서, 지킴이 목록에 추가한다.
            fragmentGuarders.guarderAdd(name, phone);

            // 추가된 것을 회원 전체 목록에서 삭제한다.
            for(GuarderVO a : alSearch) {
                if(a.getGmcphone().equals(phone)){
                    alSearch.remove(a);
                    Log.v("삭제됨?","ㅇㅇ");
                    break;
                }
            }

            // 회원 어댑터 갱신
            searchAdapterUpdate(rootView.getContext(), alSearchResult);
        }
    }


    /*
    *  date     : 2017.11.22
    *  author   : Kim Jong-ha
    *  title    : NameDescCompareGuarders, NameDescCompareSearch 메소드 생성
    *  comment  : 이름순 정렬
    * */
    private class NameDescCompareSearch implements Comparator<GuarderVO> {
        @Override
        public int compare(GuarderVO arg0, GuarderVO arg1) {
            return  arg0.getGmcname().compareTo(arg1.getGmcname());
        }
    }

    /*
    *  date     : 2017.11.20
    *  author   : Kim Jong-ha
    *  title    : guarderSearchUpdate(ArrayList<ListViewItemSearch> alSearch, ArrayList<ListViewItemGuarders> alGuarders 메소드 생성
    *  comment  : Activity 로딩 시, 회원 목록과 지킴이 목록의 중첩을 검색 후, 회원목록에서 삭제한다.
    *  return   : ArrayList<ListViewItemSearch> 형태
    * */
    private ArrayList<GuarderVO> searchUpdate(ArrayList<GuarderVO> searchs, ArrayList<GuarderVO> guarders) {

        if(guarders != null) {
            for (int i = searchs.size() - 1; i >= 0; i--) {
                for (int j = 0; j < guarders.size(); j++) {
                    if (searchs.get(i).getGmcphone().equals(guarders.get(j).getGmcphone())) {
                        searchs.remove(i);
                        break;
                    }
                }
            }
        }
        return searchs;
    }

    /*
     *  date     : 2017.12.29
     *  author   : Kim Jong-ha
     *  title    : setInstance() 메소드 생성
     *  comment  : FragmentAdapter에서 셋팅할 내용들
     */
    public void setInstance(ArrayList<GuarderVO> list, FragmentGuarders fragmentGuarders) {

        Log.v("셋인스턴스", "시작");

        this.fragmentGuarders = fragmentGuarders;

        alGuarders = this.fragmentGuarders.getList();
        // 첫 로딩 때, 전화번호부에서 지킴이 리스트에 있는 사람들을 제외하고 띄운다.
        alSearch = searchUpdate(list, alGuarders);

        Log.v("셋인스턴스", "끝");
    }

    public void setGuarderManager(GuarderManager guarderManager) {
        this.guarderManager = guarderManager;
    }
}
