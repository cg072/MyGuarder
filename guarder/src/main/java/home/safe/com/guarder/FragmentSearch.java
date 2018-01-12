package home.safe.com.guarder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    private ArrayList<GuarderVO> allList;
    private ArrayList<GuarderVO> guarderList;
    private ArrayList<GuarderVO> finalSearchList = new ArrayList<GuarderVO>();
    private EditText etSearch;
    private Button btnSearch;
    ViewGroup rootView = null;
    private ArrayList<GuarderVO> resultSearchList;
    private ArrayList<GuarderVO> addGuarderList = new ArrayList<GuarderVO>();
    GuarderManager guarderManager;
    GuarderVO addGuarderVO = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);

        guarderManager = new GuarderManager(rootView.getContext());

        lvSearch = (ListView) rootView.findViewById(R.id.lvSearch);
        etSearch = (EditText) rootView.findViewById(R.id.etSearch);
        btnSearch = (Button) rootView.findViewById(R.id.btnSearch);

        lvAdapterSearch = new ListViewAdapterSearch(rootView.getContext(), R.layout.listview_item_search, finalSearchList, this);
        lvSearch.setAdapter(lvAdapterSearch);

        etSearch.setOnEditorActionListener(this);
        btnSearch.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onSearchListBtnClick(int position) {

        allList.remove(resultSearchList.get(position));      // 전체 관리 리스트에서 삭제

        String name = resultSearchList.get(position).getGmcname();       // 삭제 전, 보낼 내용 저장
        String phone = resultSearchList.get(position).getGmcphone();

        GuarderVO guarderVO = new GuarderVO(name, phone, 0);

        // 서버로 전화번호를 보내서, 승인 받았을 경우 addGuarderVO 에 넣는다.
        addGuarderList.add(guarderVO);

        resultSearchList.remove(position);                    // 결과 관리 리스트에서 삭제

        changefinalSearchList(resultSearchList);                        // 전달 관리 리스트 갱신
        lvAdapterSearch.notifyDataSetChanged();             // 어댑터 갱신
    }

    @Override
    public void onClick(View view) {
        // 검색 버튼을 눌렀을 시 해야할 행동
        if(btnSearch.getId() == view.getId()) {
            searchResult();
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

        if(etSearch.getId() == textView.getId() && i == EditorInfo.IME_ACTION_SEARCH) {
            searchResult();
        }
        return false;
    }

    /*
     *  date     : 2017.11.27
     *  author   : Kim Jong-ha
     *  title    : resultSearchList() 메소드 생성
     *  comment  : 단어를 필터링해주고, 필터링된 ArrayList를 반환한다.
     *             본래는 서버로 String를 보내고, 필터링된 값들을 리스트에 담아야한다.
     *  return   : ArrayList<ListViewItemSearch>
     * */
    private void searchResult() {
        // 결과 담는 ArrayList 초기화
        ArrayList<GuarderVO> resultList = new ArrayList<GuarderVO>();

        // EditText로부터 필터링할 값 가져옴
        String strSearch = etSearch.getText().toString();

        // 필터링된 값들을 초기화된 resultList 담는다.
        if(!strSearch.equals("")) {
            for(GuarderVO a : allList) {
                if(a.getGmcphone().contains(strSearch) == true) {
                    resultList.add(a);
                }
            }
        }

        resultSearchList = removeDuplication(resultList, guarderList);
        changefinalSearchList(resultSearchList);
        lvAdapterSearch.notifyDataSetChanged();
    }

    /*
   *  date     : 2017.11.20
   *  author   : Kim Jong-ha
   *  title    : changefinalSearchList 메소드 생성
   *  comment  : finalSearchList의 내용을 갱신하고, 어댑터 갱신
   * */
    private void changefinalSearchList(ArrayList<GuarderVO> list) {
        finalSearchList.clear();
        finalSearchList.addAll(list);
        lvAdapterSearch.notifyDataSetChanged();
    }

    /*
    *  date     : 2017.11.20
    *  author   : Kim Jong-ha
    *  title    : removeDuplication 메소드 생성
    *  comment  : 회원 목록과 지킴이 목록의 중첩을 검색 후, 회원목록에서 삭제한다.
    *  return   : ArrayList<GuarderVO> 형태
    * */
    private ArrayList<GuarderVO> removeDuplication(ArrayList<GuarderVO> searchs, ArrayList<GuarderVO> guarders) {

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


    // 외부로부터 받은 지킴이목록을 셋팅
    public void setGuarderList(ArrayList<GuarderVO> list) {
        guarderList = list;
    }

    // 외부로부터 받은 전화부, 지킴이목록을 셋팅(이후에 중복제거)
    public void setList(ArrayList<GuarderVO> searchList, ArrayList<GuarderVO> guarderList) {
        this.guarderList = guarderList;
        allList = removeDuplication(searchList, guarderList);
    }

    // 외부로 지킴이목록 전달
    public ArrayList<GuarderVO> getAddGuarderList() {
        return addGuarderList;
    }

    // 외부에서 지킴이 목록을 초기화
    public void resetAddGuarderList(){
        addGuarderList.clear();
    }


}
