package home.safe.com.trans;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
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

*/


public class FragmentTransReg extends Fragment implements View.OnClickListener{

    ActivityTrans mainActivity;
    TextView tvtransstat;
    TextView tvtranskind;
    EditText etTextTrans;
    Button btnRegTrans;
    InputMethodManager imm;
    String num;
    String kind;
    String text;


    int fragmentStat;



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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_transreg, container, false);

        tvtransstat = (TextView) rootView.findViewById(R.id.tvtransstat);
        tvtranskind = (TextView) rootView.findViewById(R.id.tvtranskind);
        etTextTrans = (EditText) rootView.findViewById(R.id.etTextTrans);
        btnRegTrans = (Button) rootView.findViewById(R.id.btnRegTrans);

        if(fragmentStat == 0){
            tvtransstat.setText("피지킴이 입니다 : 자신의 이동수단을 등록하세요");
        }

        //상대방의 이동수단을 등록할 때는, 상대방의 아이디를 받아와서 화면에 표시해주어야 한다 한다!!
        //ex) 지킴이 입니다: ???님의 이동수단을 등록하세요
        if(fragmentStat == 1){
            tvtransstat.setText("지킴이 입니다: 상대방의 이동수단을 등록하세요");
        }


        imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        tvtranskind.setOnClickListener(this);
        btnRegTrans.setOnClickListener(this);

        return rootView;
    }



    @Override
    public void onClick(View view) {
        //에디트텍스트 영역 이외의 곳을 클릭시 키보드 닫음
        hideKeyboard();

        if(view == tvtranskind){

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
                        tvtranskind.setText("지하철");

                    } else if (i == R.id.transEtc) {
                        tvtranskind.setText("기타");

                    }
                    return false;
                }
            });

            popupMenu.show();

        }

        if(view == btnRegTrans){

            if(tvtranskind.getText().equals("이동수단 종류 선택")){
                Toast.makeText(getContext().getApplicationContext(), "이동수단이 선택되지 않음", Toast.LENGTH_LONG).show();
            }else{
                kind = tvtranskind.getText().toString();
                text = etTextTrans.getText().toString();

                AlertDialog.Builder regAlert = new AlertDialog.Builder(getActivity());
                regAlert.setTitle("이동수단 등록");

                //얼럿 박스에 긴 내용이 들어가는 이슈 !!
                regAlert.setMessage("이동수단: " + kind.trim() +"\n" + "부가정보: " + text.trim() + "\n" + "\n" + "이 정보로 저장 하시겠습니까?");

                regAlert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //서버디비에 인서트
                        toServTransReg(num, kind, text);

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
    public void hideKeyboard(){

        imm.hideSoftInputFromWindow(etTextTrans.getWindowToken(), 0);

    }


    //이 메소드에서 서버에 인서트를 해야 함.
    //생각해 볼 것 : 가상으로 콘트롤러를 거쳐야 함
    public void toServTransReg(String makeNum, String makeKind, String makeText){

        ArrayList reg = new TestTransFakeDB().insertDB(makeNum, makeKind, makeText);

    }

    //toSharedPreference를 만드는 메소드
    public void toShared(String sharedkind, String sharedtext){

        SharedPreferences preferences = getContext().getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("TransName", sharedkind);
        editor.putString("TransMemo", sharedtext);
        editor.commit();
    }

    public void fragStat (int fragRecvStat){
        this.fragmentStat = fragRecvStat;

        String a = Integer.toString(fragRecvStat);
        Log.v("받은 스탯2", a);

    }

}
