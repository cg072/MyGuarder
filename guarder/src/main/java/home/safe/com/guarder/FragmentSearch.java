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

/**
 * Created by hotki on 2017-12-17.
 */

public class FragmentSearch extends Fragment implements ListViewAdapterSearch.SearchListBtnClickListener, View.OnClickListener, TextView.OnEditorActionListener {

    private ListViewAdapterSearch lvAdapterSearch;
    private ListView lvSearch;
    private ArrayList<GuarderVO> alSearch;
    private ArrayList<GuarderVO> alGuarders;
    private ArrayList<GuarderVO> alSend = new ArrayList<GuarderVO>();
    private EditText etSearch;
    private Button btnSearch;
    ViewGroup rootView = null;
    private ArrayList<GuarderVO> alSearchResult;
    private FragmentGuarders fragmentGuarders;
    GuarderManager guarderManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);

        lvSearch = (ListView) rootView.findViewById(R.id.lvSearch);
        etSearch = (EditText) rootView.findViewById(R.id.etSearch);
        btnSearch = (Button) rootView.findViewById(R.id.btnSearch);

        lvAdapterSearch = new ListViewAdapterSearch(rootView.getContext(), R.layout.listview_item_search, alSend, this);
        lvSearch.setAdapter(lvAdapterSearch);

        etSearch.setOnEditorActionListener(this);
        btnSearch.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onSearchListBtnClick(int position) {

        alSearch.remove(alSearchResult.get(position));      // 전체 관리 리스트에서 삭제

        String name = alSearchResult.get(position).getGmcname();       // 삭제 전, 보낼 내용 저장
        String phone = alSearchResult.get(position).getGmcphone();

        GuarderVO guarderVO = new GuarderVO();
        guarderVO.setGmcname(name);
        guarderVO.setGmcphone(phone);

        // 서버로 전화번호를 보내서, 지킴이 목록에 추가한다.
        fragmentGuarders.guarderAdd(name, phone);           // 지킴이 리스트에 전달

        alSearchResult.remove(position);                    // 결과 관리 리스트에서 삭제

        inputAlSend(alSearchResult);                        // 전달 관리 리스트 갱신
        lvAdapterSearch.notifyDataSetChanged();             // 어댑터 갱신
    }

    @Override
    public void onClick(View view) {
        // 검색 버튼을 눌렀을 시 해야할 행동
        if(btnSearch.getId() == view.getId()) {
            resultSearch();
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

        if(etSearch.getId() == textView.getId() && i == EditorInfo.IME_ACTION_SEARCH) {
            resultSearch();
        }
        return false;
    }

    /*
     *  date     : 2017.11.27
     *  author   : Kim Jong-ha
     *  title    : alSearchResult() 메소드 생성
     *  comment  : 단어를 필터링해주고, 필터링된 ArrayList를 반환한다.
     *             본래는 서버로 String를 보내고, 필터링된 값들을 리스트에 담아야한다.
     *  return   : ArrayList<ListViewItemSearch>
     * */
    private void resultSearch() {
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

        alSearchResult = duplicationRemove(resultList, fragmentGuarders.getList());
        inputAlSend(alSearchResult);
        lvAdapterSearch.notifyDataSetChanged();
    }

    private void inputAlSend (ArrayList<GuarderVO> list) {
        alSend.clear();
        alSend.addAll(list);
    }

    /*
    *  date     : 2017.11.20
    *  author   : Kim Jong-ha
    *  title    : guarderSearchUpdate(ArrayList<ListViewItemSearch> alSearch, ArrayList<ListViewItemGuarders> alGuarders 메소드 생성
    *  comment  : Activity 로딩 시, 회원 목록과 지킴이 목록의 중첩을 검색 후, 회원목록에서 삭제한다.
    *  return   : ArrayList<ListViewItemSearch> 형태
    * */
    private ArrayList<GuarderVO> duplicationRemove(ArrayList<GuarderVO> searchs, ArrayList<GuarderVO> guarders) {

        ArrayList<GuarderVO> tempSearch = new ArrayList<GuarderVO>();
        tempSearch.addAll(searchs);

        if(guarders != null) {
            for (GuarderVO searchVO : tempSearch) {
                for (GuarderVO guarderVO : guarders) {
                    if (searchVO.getGmcphone().equals(guarderVO.getGmcphone())) {
                        searchs.remove(searchVO);
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

        this.fragmentGuarders = fragmentGuarders;
        alGuarders = this.fragmentGuarders.getList();
        // 첫 로딩 때, 전화번호부에서 지킴이 리스트에 있는 사람들을 제외하고 띄운다.
        alSearch = duplicationRemove(list, alGuarders);
        alSearch = list;
    }

    public void setGuarderManager(GuarderManager guarderManager) {
        this.guarderManager = guarderManager;
    }
}
