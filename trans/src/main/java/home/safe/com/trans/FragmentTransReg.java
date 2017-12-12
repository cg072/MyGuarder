package home.safe.com.trans;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

5.

*/

public class FragmentTransReg extends Fragment implements View.OnClickListener{
    ArrayList<TestListViewDTO> dtoList = new ArrayList<TestListViewDTO>();
    TestListViewDTO testDto;
    ActivityTrans mainActivity;
    TextView tvtranskind;
    EditText etTextTrans;
    Button btnRegTrans;
    InputMethodManager imm;

    String kind;
    String text;



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

        tvtranskind = (TextView) rootView.findViewById(R.id.tvtranskind);
        etTextTrans = (EditText) rootView.findViewById(R.id.etTextTrans);
        btnRegTrans = (Button) rootView.findViewById(R.id.btnRegTrans);

        imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);


        tvtranskind.setOnClickListener(this);
        btnRegTrans.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {

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
                AlertDialog.Builder warnAlert = new AlertDialog.Builder(getActivity());
                warnAlert.setTitle("이동수단을 등록하지 않음");
                warnAlert.setNeutralButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        etTextTrans.setText(null);

                    }
                });

                warnAlert.create();
                warnAlert.show();
            }



        }

    }

    public void hideKeyboard(){

        imm.hideSoftInputFromWindow(etTextTrans.getWindowToken(), 0);

    }

}
