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
    private ViewGroup rootView;
    String nowGuarderName = "";
    String nowGuarderPhone = "";
    GuarderManager guarderManager;
    TextView tvGuarderName;
    TextView tvGuarderPhone;
    int getPostion;
    GuarderVO guarderVO;
    final static String dialogRegText = "지킴이로 등록하시겠습니까?";
    final static String dialogDisText = "지킴이에서 해제하시겠습니까?";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_guarders, container, false);
        tvGuarderName = rootView.findViewById(R.id.tvGuarderName);
        tvGuarderPhone = rootView.findViewById(R.id.tvGuarderPhone);

        lvGuarders = (ListView) rootView.findViewById(R.id.lvGuarders);
        alGuarders = new ArrayList<GuarderVO>();
        Log.v("지킴이", "최초 어댑터1");
        if(loadDB() != null) {
            Log.v("셋리스트", "시작");
            setList(loadDB());
            Log.v("셋리스트", "끝");
        }

        lvAdapterGuarders = new ListViewAdapterGuarders(rootView.getContext(), R.layout.listview_item_search, alGuarders, this);
        lvGuarders.setAdapter(lvAdapterGuarders);

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

        int preCheck = 0;
        int postCheck = 0;

        ArrayList<GuarderVO> resultList = new ArrayList<GuarderVO>();

        guarderVO = new GuarderVO();
        guarderVO.setGstate(1);
        resultList = guarderManager.select("part", guarderVO);

        Log.v("지킴이 리셋", "체크");

        GuarderVO saveGuarderVO = null;

        for(GuarderVO gv: resultList) {
            saveGuarderVO = gv;
            saveGuarderVO.setGstate(0);
        }

        getPostion = position;










        if(saveGuarderVO != null) {
            preCheck = guarderManager.update(saveGuarderVO);
        }

        guarderVO = new GuarderVO();
        guarderVO.setGmcname(alGuarders.get(getPostion).getGmcname());
        guarderVO.setGmcphone(alGuarders.get(getPostion).getGmcphone());
        if(alGuarders.get(getPostion).getGstate() == 1) {
            guarderVO.setGstate(0);
            nowGuarderName = "";
            nowGuarderPhone = "";
        } else {
            nowGuarderName = guarderVO.getGmcname();
            nowGuarderPhone = guarderVO.getGmcphone();
            guarderVO.setGstate(1);

        }

        postCheck = guarderManager.update(guarderVO);
        setNowGuarder(nowGuarderName, nowGuarderPhone);
        alGuarders = guarderManager.select("all", guarderVO);
        guarderAdapterUpdate();

    }

    private boolean sendToServer(String name, String phone) {
        boolean check = true;

        return check;
    }

    // 지킴이 목록의 어댑터 갱신
    private void guarderAdapterUpdate() {
        // Adapter 생성 (implements ListViewAdapterSearch.ListBtnClickListener를 하였기때문에, 마지막에 this해도 오류 안남)
        lvAdapterGuarders = new ListViewAdapterGuarders(rootView.getContext(), R.layout.listview_item_guarders, alGuarders, this);
        // 변경된 Adapter 적용
        lvAdapterGuarders.notifyDataSetChanged();
        // 리스트뷰 참조 및 Adapter 달기
        lvGuarders.setAdapter(lvAdapterGuarders);
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
        guarderVO.setGstate(0);

        check = guarderManager.insert(guarderVO);

        if(check != 0) {
            alGuarders.add(guarderVO);
            Collections.sort(alGuarders, new FragmentGuarders.NameDescCompareGuarders());
            String title = "지킴이 추가";
            guarderAdapterUpdate();
        }
    }

    public void setList(ArrayList<GuarderVO> list) {
        alGuarders = list;
        guarderAdapterUpdate();
    }

    private ArrayList<GuarderVO> loadDB() {

        ArrayList<GuarderVO> resultList = guarderManager.select("all", new GuarderVO());
        for (GuarderVO gv : resultList) {
            if(gv.getGstate() == 1) {
                nowGuarderName = gv.getGmcname();
                nowGuarderPhone = gv.getGmcphone();
                setNowGuarder(nowGuarderName, nowGuarderPhone);
            }
        }

        return resultList;
    }

    public void setGuarderManager(GuarderManager guarderManager) {
        this.guarderManager = guarderManager;
    }

    public ArrayList<GuarderVO> getList() {
        return alGuarders;
    }

    private void setNowGuarder(String name, String phone) {
        tvGuarderName.setText(name);
        tvGuarderPhone.setText(phone);
    }

    // 1. ArrayList에 있는 것을 저장.
    // 2. ArrayList을 받은 것을 셋팅.
}
