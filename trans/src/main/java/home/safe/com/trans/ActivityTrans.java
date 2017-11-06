package home.safe.com.trans;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
    /*2017. 11. 03
    author 박준규
    이동수단 설정 -> 메인
    이동수단의 내역 버튼클릭(이동수단 내역에 대한 새로운 액티비티 띄움)
    이동수단 등록 -> 컨텍스트메뉴(menu_transelect.xml) 이동
    메모입력*/

public class ActivityTrans extends AppCompatActivity implements View.OnClickListener{

    //리스트, 선택 영역의 텍스트뷰 선언
    TextView transList;
    TextView transSelect;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);

        //리스트, 선택 영역의 아이디를 가져오기
        transList = (TextView) findViewById(R.id.transList);
        transSelect = (TextView) findViewById(R.id.transSelect);


        //리스트, 선택 영역에 온클릭리스너 세팅
        transList.setOnClickListener(this);
        transSelect.setOnClickListener(this);


    }



    //클릭 이벤트
    @Override
    public void onClick(View view) {
        //리스트버튼 클릭
        if(view == transList){
            Intent intentPopup = new Intent(this, ActivityTransListPopup.class);
            startActivity(intentPopup);
        }




        ///////////todo this
        //셀렉트버튼 클릭
       /* if(view == transSelect){
            PopupMenu transSelectPopup = new PopupMenu(this, view);
            MenuInflater inflater = transSelectPopup.getMenuInflater();
            Menu menu = transSelectPopup.getMenu();
            inflater.inflate(R.menu.menu_transselect, menu);

        }*/

    }



}
