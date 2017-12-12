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

public class FragmentTransReg extends Fragment implements View.OnClickListener{
    ArrayList<TestListViewDTO> dtoList = new ArrayList<TestListViewDTO>();
    TestListViewDTO testDto;
    ActivityTrans mainActivity;
    TextView tvtranskind;
    EditText etTextTrans;
    Button btnRegTrans;

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

        tvtranskind.setOnClickListener(this);
        btnRegTrans.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
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

}
