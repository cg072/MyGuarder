package home.safe.com.trans;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import home.safe.com.trans.R;

/**
 * Created by plupin724 on 2017-11-30.
 * 이동수단 등록을 위한 프래그먼트 클래스 작성
 */

/*해야할일
1. fragment_transreg.xml에서 얼럿버튼 클릭시 에디트 텍스트 부분이 초기화 됨!! 수정해야함
=>>안되면 토스트메시지로 대체하고 finish가 안되게 한다.

2. 이동수단내역으로 탭이 옮겨질 때, 키보드를 사라지게 해야한다!!

3. 에디트텍스트에 자동으로 포커싱 방지

4. 이동수단 변경시 창현이 화면에 나타나야 하는데, 그것에 대한 협의가 필요!!
=>>
getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
(preferences.getString("TransName","택시(기본값)");
preferences.getString("TransMemo","기본값");

5. 얼럿박스에 앞의 한 줄만 보여주게끔 만들어야 함!!!! 맥스라인도 잡아주셈!!!!

6. 메모는 100자 한정으로 한다!!!!!


!!!!!!!!!!!! 이동수단 내역에서 수정 버튼을 추가하고 수정이 되게 만들기
필드변수 줄이기 !!!!!

*/


public class FragmentTransReg extends Fragment implements View.OnClickListener {

    ActivityTrans mainActivity;
    TextView tvtransstat;
    TextView tvtranskind;
    EditText etTextTrans;
    Button btnRegTrans;
    InputMethodManager imm;

    String tseq = null;
    String ttype = null;
    String tname = null;

    TestListViewDTO regDto;
    ArrayList<TestListViewDTO> regArrDto = new ArrayList<TestListViewDTO>();

    TransIntegratedVO regTransIntegratedVO ;
    ArrayList<TransIntegratedVO> regArrTransIntegratedVO = new ArrayList<TransIntegratedVO>();

    FragmentTransList fragmentTransList;
    TransManager transManager;

    TransController transController;
    TransDBHelper transDBHelper;



/*    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mainActivity = (ActivityTrans) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mainActivity = null;
    }*/

    //빈생성자
    public FragmentTransReg(){

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_transreg, container, false);

        tvtransstat = (TextView) rootView.findViewById(R.id.tvtransstat);
        tvtranskind = (TextView) rootView.findViewById(R.id.tvtranskind);
        etTextTrans = (EditText) rootView.findViewById(R.id.etTextTrans);
        btnRegTrans = (Button) rootView.findViewById(R.id.btnRegTrans);


        tvtransstat.setText("피지킴이 입니다 : 자신의 이동수단을 등록하세요");

        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        tvtranskind.setOnClickListener(this);
        btnRegTrans.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onClick(View view) {
        //에디트텍스트 영역 이외의 곳을 클릭시 키보드 닫음
        hideKeyboard();

        if (view == tvtranskind) {

            PopupMenu popupMenu = new PopupMenu(getContext().getApplicationContext(), view);
            MenuInflater menuInflater = popupMenu.getMenuInflater();
            Menu menu = popupMenu.getMenu();
            menuInflater.inflate(R.menu.menu_transselect, menu);

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    int i = menuItem.getItemId();

                    if (i == R.id.transWalk) {
                        tvtranskind.setText("도보");

                    } else if (i == R.id.transBus) {
                        tvtranskind.setText("버스");

                    } else if (i == R.id.transTaxi) {
                        tvtranskind.setText("택시");

                    } else if (i == R.id.transSub) {
                        tvtranskind.setText("전철");

                    } else if (i == R.id.transEtc) {
                        tvtranskind.setText("기타");

                    }
                    return false;
                }
            });

            popupMenu.show();

        }

        if (view == btnRegTrans) {

            if (tvtranskind.getText().equals("이동수단 종류 선택")) {
                Toast.makeText(getContext().getApplicationContext(), "이동수단이 선택되지 않음", Toast.LENGTH_LONG).show();
            } else {
                ttype = tvtranskind.getText().toString();
                tname = etTextTrans.getText().toString();

                if (tname.length() >= 10) {
                    tname = tname.substring(0, 10);
                }

                tname = tname.trim();

                Log.v("텍스트입니다", tname);

                AlertDialog.Builder regAlert = new AlertDialog.Builder(getActivity());
                regAlert.setTitle("이동수단 등록");

                //엔터키 후 숫자가 먼저 들어갈 경우 에러 발생 해결해야 함!!!!!
                //regAlert.setMessage("이동수단: " + kind.trim() +"\n" + "부가정보: " + text.trim() + "\n" + "\n" + "이 정보로 저장 하시겠습니까?");

                regAlert.setMessage("이동수단: " + ttype.trim() + "\n" + "부가정보: " + tname + "\n" + "\n" + "이 정보로 저장 하시겠습니까?");

                regAlert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //서버디비에 인서트
                        //setDto(tseq, ttype, tname);
                        intsertDB(ttype, tname);

                        //sharedpreference를 호출
                        //toShared(kind, text);

                        Toast.makeText(getContext().getApplicationContext(), "등록되었습니다", Toast.LENGTH_LONG).show();
                        tvtranskind.setText("이동수단 종류 선택");
                        etTextTrans.setText(null);

                    }
                });

                regAlert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                regAlert.create();
                regAlert.show();
            }

        }

    }

    //키보드를 닫는 메소드
    public void hideKeyboard() {
        imm.hideSoftInputFromWindow(etTextTrans.getWindowToken(), 0);

    }


    //이 메소드에서 서버에 인서트를 해야 함.
    //생각해 볼 것 : 가상으로 콘트롤러를 거쳐야 함
    /*public void insertToServer(String n, String k, String t) {

        ContentValues contentValues = new ContentValues();

    }*/


    public void setDto(String makeNum, String makeKind, String makeText) {

        regDto = new TestListViewDTO();

        regDto.setNum(makeNum);
        regDto.setTranName(makeKind);
        regDto.setText(makeText);

        Log.v("regDtoName" , "확인5");

        Log.v("regArrDto" , "확인6");

        regArrDto.add(regDto);
        fragmentTransList.setArryDtoFragList(regArrDto);

        Log.v("regArrDto" , "확인8");
    }

    public void intsertDB(String makettype, String maketname){

        int check = 0;

        TransManager transManager = new TransManager(getContext().getApplicationContext());
       // transManager = new TransManager(getContext());

        regTransIntegratedVO = new TransIntegratedVO();

        regTransIntegratedVO.setTtype(makettype);
        regTransIntegratedVO.setTmemo(maketname);

        Log.d("들어갑니까?", "확인");

        //regArrTransIntegratedVO.add(regTransIntegratedVO);

        check = transManager.insert(regTransIntegratedVO);

        Log.d("들어갑니까?", "확인2222");
        Log.d("성공여부", Integer.toString(check));
        Log.d("값확인1", regTransIntegratedVO.getTtype());
        Log.d("값확인2", regTransIntegratedVO.getTmemo());
        Log.d("값확인3", Integer.toString(regTransIntegratedVO.getTseq()));


    }

    public void setFragReg(FragmentTransList fraglist) {
        this.fragmentTransList = fraglist;
        Log.v("regArrDto" , "확인1");

    }

    public void setTransManager(TransManager transManager){
        this.transManager = transManager;

    }

}
