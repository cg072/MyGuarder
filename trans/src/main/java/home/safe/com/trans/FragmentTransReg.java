package home.safe.com.trans;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
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

/**
 * Created by plupin724 on 2017-11-30.
 * 이동수단 등록을 위한 프래그먼트 클래스 작성
 */

public class FragmentTransReg extends Fragment implements View.OnClickListener{
    ActivityTrans mainActivity;
    TextView tvtranskind;
    EditText etTextTrans;
    Button btnRegTrans;



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

                    switch (menuItem.getItemId()){
                        case R.id.transWalk:
                            tvtranskind.setText("도보");
                            break;

                        case R.id.transBus:
                            tvtranskind.setText("버스");
                            break;

                        case R.id.transTaxi:
                            tvtranskind.setText("택시");
                            break;

                        case R.id.transSub:
                            tvtranskind.setText("지하철");
                            break;

                        case R.id.transEtc:
                            tvtranskind.setText("기타");
                            break;

                    }
                    return false;
                }
            });

            popupMenu.show();

        }

        if(view == btnRegTrans){
            String msg = tvtranskind.getText() + ":" + etTextTrans.getText();

            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();

        }

    }

}
