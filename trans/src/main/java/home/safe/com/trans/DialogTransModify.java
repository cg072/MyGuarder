package home.safe.com.trans;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by plupin724 on 2018-01-21.
 */

public class DialogTransModify extends Dialog implements View.OnClickListener{

    TextView tvtranstype;
    EditText ettranstmemo;
    Button btnDialCancel;
    Button btnDialConfirm;

    String ttype;
    String tmemo;

    public DialogTransModify(@NonNull Context context, String ttype, String tmemo) {
        super(context);
        this.ttype = ttype;
        this.tmemo = tmemo;
    }

    public DialogTransModify(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_trans_modify);

        tvtranstype = (TextView)findViewById(R.id.tvtranstype);
        ettranstmemo = (EditText)findViewById(R.id.ettranstmemo);
        btnDialCancel = (Button)findViewById(R.id.btnDialCancel);
        btnDialConfirm = (Button)findViewById(R.id.btnDialConfirm);

        setInfo(ttype, tmemo);

        tvtranstype.setOnClickListener(this);
        ettranstmemo.setOnClickListener(this);
        btnDialCancel.setOnClickListener(this);
        btnDialConfirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == tvtranstype){
            PopupMenu popupMenu = new PopupMenu(getContext().getApplicationContext(), view);
            MenuInflater menuInflater = popupMenu.getMenuInflater();
            Menu menu = popupMenu.getMenu();
            menuInflater.inflate(R.menu.menu_transselect, menu);

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    int i = menuItem.getItemId();

                    if(i == R.id.transWalk){
                        tvtranstype.setText("도보");
                    }else if(i == R.id.transBus){
                        tvtranstype.setText("버스");

                    }else if (i == R.id.transTaxi) {
                        tvtranstype.setText("택시");

                    }else if (i == R.id.transSub) {
                        tvtranstype.setText("전철");

                    }else if (i == R.id.transEtc) {
                        tvtranstype.setText("기타");

                    }

                    return false;
                }
            });

            popupMenu.show();

        }

        if(view == ettranstmemo){

        }

        if(view == btnDialCancel){
            this.dismiss();

        }


        /*
        *
        * 해야할일 :
        *
        * 1. 입력된 정보가 기존의 정보와 같다면 수정된 내용이 없다는 표시를 해주기
        * 입력된 정보가 하나라도 다르다면 디비에 업데이트 해주기
        *
        * 1-1. 이동수단이 다를 시,
        * 1-2. 메모 내용이 다를 시 구분 해주기
        *
        * 2. gettext시 "(클릭)" 문자를 없애주기
        *
        * 3. gettext시 힌트를 얻어오기 ex) getHint();
        *
        *
        * */
        if(view == btnDialConfirm){

            String ttype = tvtranstype.getText().toString();



            if(ttype.contains("클릭")){
                //클릭 문자를 제거
                Toast.makeText(getContext().getApplicationContext(), ttype + "클릭포함", Toast.LENGTH_LONG).show();
            }

            Toast.makeText(getContext().getApplicationContext(), ttype + "클릭미포함", Toast.LENGTH_LONG).show();

            String ttemo = ettranstmemo.getText().toString();

            this.dismiss();

        }
    }

    public void setInfo(String ttype, String tmemo){
        tvtranstype.setText(ttype + " (클릭)");
        ettranstmemo.setText(tmemo);

    }
}
