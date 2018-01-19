package home.safe.com.guarder;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    int getpostion;
    GuarderVO getGuarderVO;

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

        String name = resultSearchList.get(position).getGmcname();       // 삭제 전, 보낼 내용 저장
        String phone = resultSearchList.get(position).getGmcphone();

        getGuarderVO = new GuarderVO(name, phone, 0);

        getpostion = position;

        AlertDialog.Builder regAlert = new AlertDialog.Builder(getActivity());  // 다이얼로그 생성
        setDialog(regAlert);        // 다이얼로그 셋팅
        regAlert.create();          // 해당 정보로 다이얼로그 생성
        regAlert.show();            // 해당 정보로 다이얼로그 보여줌
    }

    private void setDialog(AlertDialog.Builder regAlert) {

        regAlert.setTitle("지킴이 추가");
        regAlert.setMessage("이름: " + getGuarderVO.getGmcname() + "\n" +
                "전화번호: " + getGuarderVO.getGmcphone() + "\n" + "\n" + "해당 정보로 지킴이 목록에 추가하시겠습니까?");

        // 지킴이 추가
        regAlert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // 서버로 전화번호를 보내서, 승인 받았을 경우 addGuarderVO 에 넣는다.
                if(sendDataToServerForSearch(getGuarderVO) == 1) {
                    int returnDB = 0;

                    GuarderManager guarderManager = new GuarderManager(getContext());

                    returnDB = guarderManager.insert(GuarderShareWord.TARGET_DB, getGuarderVO);

                    if(returnDB != 0) {
                        addGuarderList.add(getGuarderVO);                       // 지킴이 목록 관리 리스트에 추가

                        allList.remove(resultSearchList.get(getpostion));      // 전체 관리 리스트에서 삭제

                        resultSearchList.remove(getpostion);                    // 결과 관리 리스트에서 삭제

                        changefinalSearchList(resultSearchList);                 // 전달 관리 리스트 갱신
                        lvAdapterSearch.notifyDataSetChanged();                  // 어댑터 갱신

                        Toast.makeText(getContext(), getGuarderVO.getGmcname() + " 님을 지킴이 목록에 추가하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        regAlert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 취소를 누를땐 별 다른 내용이 없다.
            }
        });
    }

    private int sendDataToServerForSearch(GuarderVO guarderVO) {
        int check = 0;

        GuarderManager guarderManager = new GuarderManager(getContext());

        ArrayList<GuarderVO> resultList = new ArrayList<GuarderVO>();

        resultList = guarderManager.select(GuarderShareWord.TARGET_SERVER, GuarderManager.TYPE_SELECT_CON, getGuarderVO);

        for(GuarderVO g : resultList) {
            if(g.getGmcname().equals(guarderVO.getGmcname()) && g.getGmcphone().equals(guarderVO.getGmcphone())) {
                check = 1;
            }
        }

        //서버에 다녀온 후, 결과가 옳다면 insert(삭제 요망)
        check = 1;
        // 서버로 다녀온 리턴값(check)이 1이면 서버 체크 성공, 0이면 실패

        return check;
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
