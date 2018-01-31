package home.safe.com.guarder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Comparator;

public class FragmentGuarders extends Fragment implements ListViewAdapterGuarders.GuardersListBtnClickListener {

    // 지킴이로 설정되어있다면 use = 1
    // 지킴이로 설정되어있지 않다면 use = 0

    final static String TAG = "GURDERS_LIST";

    private ListViewAdapterGuarders lvAdapterGuarders;
    private ListView lvGuarders;
    private ArrayList<GuarderVO> guarderList = new ArrayList<GuarderVO>();
    GuarderManager guarderManager;
    TextView tvGuarderName;
    TextView tvGuarderPhone;
    int nowPosition = -1;

    GuarderVO selectedGuarderVO = new GuarderVO();

    final static String DIALOG_TEXT[] = {"지킴이에서 해제하시겠습니까?", "지킴이로 등록하시겠습니까?"} ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_guarders, container, false);

        guarderManager = new GuarderManager(rootView.getContext());

        tvGuarderName = rootView.findViewById(R.id.tvGuarderName);
        tvGuarderPhone = rootView.findViewById(R.id.tvGuarderPhone);

        lvGuarders = (ListView) rootView.findViewById(R.id.lvGuarders);

        lvAdapterGuarders = new ListViewAdapterGuarders(rootView.getContext(), R.layout.listview_item_search, guarderList, this);
        lvGuarders.setAdapter(lvAdapterGuarders);

        if(loadGuarderListFromDB() != null) {
            changeGurderList(loadGuarderListFromDB());
            lvAdapterGuarders.notifyDataSetChanged();
        }

        return rootView;
    }


    // 지킴이 리스트에서 슬라이딩 버튼을 눌렀을 시
    /*
    *  date     : 2017.11.20
    *  author   : Kim Jong-ha
    *  title    : onGuardersListBtnClick(int position, int count) 메소드 생성
    *  comment  : 지킴이 목록에 있는 SlidingButton을 눌렀을 경우의 작동 코딩
    *             지킴이는 1명만 지정이 가능하기 때문에, 지킴이로 지정된 ListView의 item을 제외하고는 모두 Button 상태가 false가 되어야한다.
    */
    @Override
    public void onGuardersListBtnClick(int position, int count) {

        GuarderVO saveGuarderVO = new GuarderVO();

        AlertDialog.Builder regAlert = new AlertDialog.Builder(getActivity());  // 다이얼로그 생성

        if(guarderList.get(position).getGstate() == 1) {                // 해당 포지션의 지킴이가 등록이 된 상태라면
            setDialog(0, position, regAlert);                                   //  확인 버튼 클릭시 해제시킨다.
        } else {                                                           // 해당 포지션의 지킴이가 등록이 안된 상태라면
            setDialog(1, position, regAlert);                                   //  확인 버튼 클릭시 등록시킨다.
        }

        regAlert.create();          // 해당 정보로 다이얼로그 생성
        regAlert.show();            // 해당 정보로 다이얼로그 보여줌
    }

    // 회원 리스트에서 등록 버튼을 눌렀을 시에, 서버로 보내는 메소드
    private boolean sendToServer(String name, String phone) {
        boolean check = true;

        return check;
    }

    /*
    *  date     : 2017.11.22
    *  author   : Kim Jong-ha
    *  title    : sortOrderList 메소드 생성
    *  comment  : 이름순 정렬
    * */
    private class sortOrderList implements Comparator<GuarderVO> {
        @Override
        public int compare(GuarderVO arg0, GuarderVO arg1) {
            return  arg0.getGmcname().compareTo(arg1.getGmcname());
        }
    }

    /*
    *  date     : 2017.01.03
    *  author   : Kim Jong-ha
    *  title    : loadGuarderListFromDB 메소드 생성
    *  comment  : DB로부터 지킴이Fragment를 초기 구성할 지킴이 리스트를 가져온다.
    *             현재 지킴이 상태를 나타내는 메소드 사용(setNowGuarder)
    * */
    private ArrayList<GuarderVO> loadGuarderListFromDB() {                                                // DB로 부터 초기 목록화면을 가져온다.
        ArrayList<GuarderVO> resultList = guarderManager.select(GuarderShareWord.TARGET_DB,GuarderShareWord.TYPE_SELECT_ALL, null);    // DB selectAll을 위한 매니저로 전송

        for (int i = 0 ; i < resultList.size() ; i++) {                                                           // 리스트중 현재 지킴이 상태인 회원을 뽑는다.
            if(resultList.get(i).getGstate() == 1) {
                setNowGuarder(resultList.get(i).getGmcname(), resultList.get(i).getGmcphone());     // Context 및 텍스트뷰에 현재 지킴이로 등록
                nowPosition = i;
            }
        }

        return resultList;
    }

    public void updateGuarderList() {
        changeGurderList(loadGuarderListFromDB());
        lvAdapterGuarders.notifyDataSetChanged();
    }

    /*
    *  date     : 2017.01.05
    *  author   : Kim Jong-ha
    *  title    : getGuarderList 메소드 생성
    *  comment  : 외부로 지킴이 리스트 전송( FragmentSearch에서 중복 제거를 위해 사용중)
    * */
    public ArrayList<GuarderVO> getGuarderList() {

        if(guarderManager != null) {
            changeGurderList(guarderManager.select(GuarderShareWord.TARGET_DB, GuarderManager.TYPE_SELECT_ALL, null));  // 지킴이 목록 업데이트
        } else {
            Log.v("매니저값", "널");
        }
        return guarderList;
    }

    /*
    *  date     : 2017.01.05
    *  author   : Kim Jong-ha
    *  title    : setNowGuarder 메소드 생성
    *  comment  : // 상단의 텍스트뷰에 현재 지킴이 정보 표시
    * */
    private void setNowGuarder(String name , String phone) {
        tvGuarderName.setText(name);
        if(phone.equals("")) {
            tvGuarderPhone.setText(phone);
        }else {
            tvGuarderPhone.setText(addHyphen(phone));
        }
    }

    /*
    *  date     : 2017.01.05
    *  author   : Kim Jong-ha
    *  title    : inputAlguarder 메소드 생성
    *  comment  : finalGuarderList = OTHER ArrayList<GuarderVO>(); 할 경우,
    *             adapter에 setAdapter로 적용된 finalGuarderList의 주소값이 바뀌므로,
    *             비우고, 추가하는 형태로 작성
    *             이 메소드뒤에 어댑터 갱신이 대부분 붙어있기때문에, 같이 넣음
    * */
    private void changeGurderList(ArrayList<GuarderVO> list) {
        guarderList.clear();
        guarderList.addAll(list);
    }

    /*
    *  date     : 2017.01.05
    *  author   : Kim Jong-ha
    *  title    : setList 메소드 생성
    *  comment  : 외부에서 리스트를 셋팅
    * */
    public void setList(ArrayList<GuarderVO> list){
        changeGurderList(list);
        lvAdapterGuarders.notifyDataSetChanged();
    }

    // GuarderManager 를 셋팅해주어야, creatView 전에 이루어지는 db작업이 가능
    public void setGuarderManager(Context context) {
        guarderManager = new GuarderManager(context);
    }

    /*
    *  date     : 2017.01.07
    *  author   : Kim Jong-ha
    *  title    : setDialog 메소드 생성
    *  comment  : 다이얼로그를 나타내는 메소드를 구성한다.
    *             확인 버튼을 눌렀을 시,
    *             지킴이 상태일때, 지킴이 상태가 아닐때로 두가지의 경우가 필요하다.
    *             1. 모든 지킴이 상태를 (0 으로 update)
    *             2. 해당 포지션의 지킴이의 상태를 update
    *             3. DB로 부터 갱신이 완료된 지킴이리스트를 Load
    * */
    private void setDialog(int check, final int position, AlertDialog.Builder regAlert) {

        regAlert.setTitle("지킴이 등록");
        regAlert.setMessage("이름: " + guarderList.get(position).getGmcname().trim() + "\n" +
                "전화번호: " + addHyphen(guarderList.get(position).getGmcphone()) + "\n" + "\n" + DIALOG_TEXT[check]);

        final GuarderVO guarderVO;

        if(nowPosition == -1) {
            guarderVO = guarderList.get(position);
        } else {
            guarderVO = guarderList.get(nowPosition);
        }


        switch (check) {
            case 0 :
                // 지킴이 해제
                regAlert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        guarderVO.setGstate(0);

                        int check = guarderManager.update(GuarderShareWord.TARGET_DB, guarderVO);

                        changeGurderList(guarderManager.select(GuarderShareWord.TARGET_DB,"all", null));  // 지킴이 목록 업데이트
                        setNowGuarder("", "");

                        lvAdapterGuarders.notifyDataSetChanged();

                        nowPosition = -1;

                        Toast.makeText(getContext(), "현재 등록된 지킴이가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 1 :
                // 지킴이 등록
                regAlert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        guarderVO.setGstate(0);

                        int check = guarderManager.update(GuarderShareWord.TARGET_DB, guarderVO);          // 모든 지킴이 해제

                        GuarderVO selectedGuarderVO = guarderList.get(position);
                        selectedGuarderVO.setGstate(1);

                        check = guarderManager.update(GuarderShareWord.TARGET_DB, selectedGuarderVO);

                        setNowGuarder(selectedGuarderVO.getGmcname(), selectedGuarderVO.getGmcphone());                      // 현재 지킴이 정보 등록

                        changeGurderList(guarderManager.select(GuarderShareWord.TARGET_DB,GuarderManager.TYPE_SELECT_ALL, null));  // 지킴이 목록 업데이트
                        lvAdapterGuarders.notifyDataSetChanged();
                        nowPosition = position;

                        Toast.makeText(getContext(), selectedGuarderVO.getGmcname() + " " + selectedGuarderVO.getGmcphone(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }

        regAlert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 취소를 누를땐 별 다른 내용이 없다.
            }
        });
    }

    /*
 *  date     : 2017.11.22
 *  author   : Kim Jong-ha
 *  title    : addHyphen() 메소드 생성
 *  comment  : 전화 번호 사이의 '-' 를 추가한다
 *  return   : String 형태
 * */
    private String addHyphen(String phone) {

        String resultString = phone;

        switch(resultString.length()) {
            case 10 :
                resultString =  resultString.substring(0,3) + "-" +
                        resultString.substring(3,6) + "-" +
                        resultString.substring(6,10);
                break;

            case 11 :
                resultString =  resultString.substring(0,3) + "-" +
                        resultString.substring(3,7) + "-" +
                        resultString.substring(7,11);
                break;
            default :
                resultString = "Error";
        }
        return resultString;
    }

    // 1. ArrayList에 있는 것을 저장.
    // 2. ArrayList을 받은 것을 셋팅.
}
