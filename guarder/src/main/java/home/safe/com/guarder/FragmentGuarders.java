package home.safe.com.guarder;

import android.app.AlertDialog;
import android.content.ContentValues;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragmentGuarders extends Fragment implements ListViewAdapterGuarders.GuardersListBtnClickListener {

    // 지킴이로 설정되어있다면 use = 1
    // 지킴이로 설정되어있지 않다면 use = 0
    final static String TAG = "MEMBER";

    private ListViewAdapterGuarders lvAdapterGuarders;
    private ListView lvGuarders;
    private ArrayList<GuarderVO> alGuarders;
    String nowGuarderName = "";
    String nowGuarderPhone = "";
    GuarderManager guarderManager;
    TextView tvGuarderName;
    TextView tvGuarderPhone;
    int getPostion;
    GuarderVO dialogSaveGuarderVO;
    GuarderVO dialogGuarderVO;
    final static String dialogText[] = {"지킴이에서 해제하시겠습니까?", "지킴이로 등록하시겠습니까?"} ;
    int preCheck = 0;
    int postCheck = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_guarders, container, false);
        tvGuarderName = rootView.findViewById(R.id.tvGuarderName);
        tvGuarderPhone = rootView.findViewById(R.id.tvGuarderPhone);

        lvGuarders = (ListView) rootView.findViewById(R.id.lvGuarders);
        alGuarders = new ArrayList<GuarderVO>();

        lvAdapterGuarders = new ListViewAdapterGuarders(rootView.getContext(), R.layout.listview_item_search, alGuarders, this);
        lvGuarders.setAdapter(lvAdapterGuarders);

        setList(loadDB());

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

        ArrayList<GuarderVO> resultList = new ArrayList<GuarderVO>();

        GuarderVO guarderVO = new GuarderVO();
        guarderVO.setGstate(1);
        resultList = guarderManager.select("part", guarderVO);      // 현재 모든 지킴이들을 "해제" 시키기위한 셋팅

        GuarderVO saveGuarderVO = null;

        for(GuarderVO gv: resultList) {
            saveGuarderVO = gv;
            saveGuarderVO.setGstate(0);
        }                                                                   // 등록된 지킴이의 정보에서 상태값을 "해제"로 저장

        AlertDialog.Builder regAlert = new AlertDialog.Builder(getActivity());  // 다이얼로그 생성

        guarderVO = new GuarderVO();
        guarderVO.setGmcname(alGuarders.get(position).getGmcname());
        guarderVO.setGmcphone(alGuarders.get(position).getGmcphone());      // 클릭된 지킴이의 값을 저장
        dialogGuarderVO = guarderVO;
        dialogSaveGuarderVO = saveGuarderVO;                              // 먼저 등록된 지킴이의 정보값을 해제상태로 둔채 저장
        getPostion = position;                                             // 현재 클릭된 곳의 위치값을 저장 (전역변수로)

        if(alGuarders.get(getPostion).getGstate() == 1) {                // 해당 포지션의 지킴이가 등록이 된 상태라면
            setDialog(0,regAlert);                                   //  확인 버튼 클릭시 해제시킨다.
        } else {                                                           // 해당 포지션의 지킴이가 등록이 안된 상태라면
            setDialog(1,regAlert);                                   //  확인 버튼 클릭시 등록시킨다.
        }

        regAlert.create();          // 해당 정보로 다이얼로그 생성
        regAlert.show();            // 해당 정보로 다이얼로그 보여줌
    }

    private boolean sendToServer(String name, String phone) {
        boolean check = true;

        return check;
    }

    /*
    *  date     : 2017.11.22
    *  author   : Kim Jong-ha
    *  title    : NameDescCompareGuarders, NameDescCompareSearch 메소드 생성
    *  comment  : 이름순 정렬
    * */
    private class NameDescCompareGuarders implements Comparator<GuarderVO> {
        @Override
        public int compare(GuarderVO arg0, GuarderVO arg1) {
            return  arg0.getGmcname().compareTo(arg1.getGmcname());
        }
    }

    // ArrayList에 지킴이를 추가함 [서버와 연결 후 부터는 서버로부터 받아와야한다.]
    public void guarderAdd(String name, String phone) {
        Log.v("지킴이 추가","진입");
        int check = 0;

        GuarderVO guarderVO = new GuarderVO();
        guarderVO.setGmcname(name);
        guarderVO.setGmcphone(phone);
        guarderVO.setGstate(0);                     // 지킴이 포장( 형태 GuarderVO)

        check = guarderManager.insert(guarderVO); // guarderManager로 전송

        if(check != 0) {                                                                    // 결과값이 성공적이라면 ( 0 이 아니라면)
            alGuarders.add(guarderVO);                                                     // 지킴이 리스트에 추가
            Collections.sort(alGuarders, new FragmentGuarders.NameDescCompareGuarders());  // 지킴이 리스트 이름순 정렬
            lvAdapterGuarders.notifyDataSetChanged();                                      // 어댑터 갱신
        }
    }

    public void setList(ArrayList<GuarderVO> list) {    // 지킴이 목록을 보여주는 현재 화면에 외부에서 리스트를 전송하여 어댑터 갱신(외부전용)
        inputAlGuarders(list);
        lvAdapterGuarders.notifyDataSetChanged();
    }

    private ArrayList<GuarderVO> loadDB() {         // DB로 부터 초기 목록화면을 가져온다.

        ArrayList<GuarderVO> resultList = guarderManager.select("all", new GuarderVO());    // DB selectAll을 위한 매니저로 전송
        for (GuarderVO gv : resultList) {           // 리스트중 현재 지킴이 상태인 회원을 뽑는다.
            if(gv.getGstate() == 1) {
                nowGuarderName = gv.getGmcname();
                nowGuarderPhone = gv.getGmcphone();
                setNowGuarder(nowGuarderName, nowGuarderPhone);     // Context 및 텍스트뷰에 현재 지킴이로 등록
            }
        }

        return resultList;
    }

    public void setGuarderManager(GuarderManager guarderManager) {      // guarderManager 셋팅
        this.guarderManager = guarderManager;
    }

    public ArrayList<GuarderVO> getList() {         // 외부로 지킴이 리스트 전송( FragmentSearch에서 사용중 - 중복 제거를 위한)
        return alGuarders;
    }

    private void setNowGuarder(String name, String phone) {     // 상단의 텍스트뷰에 현재 지킴이 정보 표시
        tvGuarderName.setText(name);
        tvGuarderPhone.setText(phone);
    }

    private void inputAlGuarders(ArrayList<GuarderVO> list) {
        alGuarders.clear();
        alGuarders.addAll(list);
    }

    private void setDialog(int check, AlertDialog.Builder regAlert) {

        regAlert.setTitle("지킴이 등록");
        regAlert.setMessage("이름: " + alGuarders.get(getPostion).getGmcname().trim() + "\n" +
                "전화번호: " + alGuarders.get(getPostion).getGmcphone() + "\n" + "\n" + dialogText[check]);

        switch (check) {
            case 0 :
                // 지킴이 해제
                regAlert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(dialogSaveGuarderVO != null) {
                            preCheck = guarderManager.update(dialogSaveGuarderVO);            // 모든 지킴이 해제
                        }

                        nowGuarderName = "";
                        nowGuarderPhone = "";
                        setNowGuarder(nowGuarderName, nowGuarderPhone);                        // 현재 지킴이 정보 삭제

                        dialogGuarderVO.setGstate(0);
                        postCheck = guarderManager.update(dialogGuarderVO);                   // 해당 포지션 업데이트

                        inputAlGuarders(guarderManager.select("all", dialogGuarderVO));  // 지킴이 목록 업데이트
                        lvAdapterGuarders.notifyDataSetChanged();                                // 지킴이 목록을 화면에 갱신
                    }
                });
                break;
            case 1 :
                // 지킴이 등록
                regAlert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(dialogSaveGuarderVO != null) {
                            preCheck = guarderManager.update(dialogSaveGuarderVO);          // 모든 지킴이 해제
                        }

                        nowGuarderName = dialogGuarderVO.getGmcname();
                        nowGuarderPhone = dialogGuarderVO.getGmcphone();
                        setNowGuarder(nowGuarderName, nowGuarderPhone);                      // 현재 지킴이 정보 등록

                        dialogGuarderVO.setGstate(1);
                        postCheck = guarderManager.update(dialogGuarderVO);                 // 해당 포지션 업데이트

                        inputAlGuarders(guarderManager.select("all", dialogGuarderVO));  // 지킴이 목록 업데이트
                        lvAdapterGuarders.notifyDataSetChanged();                             // 지킴이 목록을 화면에 갱신
                        Toast.makeText(getContext(), nowGuarderName + " " + nowGuarderPhone, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }

        regAlert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "취소할거면 왜눌러 띱때야", Toast.LENGTH_SHORT).show();// 취소는 없다.
            }
        });
    }

    // 1. ArrayList에 있는 것을 저장.
    // 2. ArrayList을 받은 것을 셋팅.
}
