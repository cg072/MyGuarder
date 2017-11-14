package home.safe.com.trans;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
    /*2017. 11. 03
    author 박준규
    이동수단 설정 -> 메인
    이동수단의 내역 버튼클릭(이동수단 내역에 대한 새로운 액티비티 띄움)
    이동수단 등록 -> 팝업메뉴(menu_transelect.xml) 띄움
    부가정보 입력 -> 부가정보 내용 저장
    확인버튼 클릭 -> 메인액티비티로 이동
    */

    /*해야할 일 : 1. 이동수단 선택 된것과, 선택 한것 텍스트뷰에 표시해주기!!!
                2. 확인 버튼 클릭시, 부가정보입력 된 텍스트 저장하기!!!
                3. 메모위치 영역 표시해주기!!!
                */

public class ActivityTrans extends AppCompatActivity implements View.OnClickListener {

    //리스트, 선택 영역의 텍스트뷰 선언
    TextView transList;
    TextView transSelect;
    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);

        //리스트, 선택 영역의 아이디를 가져오기
        transList = (TextView) findViewById(R.id.transList);
        transSelect = (TextView) findViewById(R.id.transSelect);
        btnOk = (Button) findViewById(R.id.btnOk);


        //리스트, 선택 영역에 온클릭리스너 세팅
        transList.setOnClickListener(this);
        transSelect.setOnClickListener(this);


    }


    //클릭 이벤트
    @Override
    public void onClick(View view) {
        //리스트버튼 클릭
        if (view == transList) {

            //인텐트로 보내주기 위한 객체 어레이 리스트 생성
            ArrayList<TestListViewDTO> arrtest = new ArrayList<>();
            //어레이 리스트에 dto 값을 넣어둠
            arrtest.add(new TestListViewDTO("1", "택시", "모범"));
            arrtest.add(new TestListViewDTO("2", "버스", "좌석"));
            arrtest.add(new TestListViewDTO("3", "대리", "대리"));
            arrtest.add(new TestListViewDTO("4", "지하철", "지하철"));

            //ActivityTransListPopup.class로 인텐트를 전송
            Intent intentPopup = new Intent(this, ActivityTransListPopup.class);


            //인텐트에 dto객체 어레이 리스트를 부가로 전송
            intentPopup.putExtra("dtotest" , arrtest);

            startActivity(intentPopup);
        }


        //셀렉트버튼 클릭
        if (view == transSelect) {
            //팝업 메뉴 만들어주기
            PopupMenu popupTransSelect = new PopupMenu(this, view);
            //팝업메뉴 인플레이트
            MenuInflater inflater = popupTransSelect.getMenuInflater();
            Menu menu = popupTransSelect.getMenu();
            inflater.inflate(R.menu.menu_transselect, menu);
            popupTransSelect.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.transWalk:
                            Toast.makeText(getApplicationContext(), "도보", Toast.LENGTH_LONG).show();
                            transSelect.setText("도보");
                    }
                    switch (item.getItemId()) {
                        case R.id.transBus:
                            Toast.makeText(getApplicationContext(), "버스", Toast.LENGTH_LONG).show();
                            transSelect.setText("버스");
                    }
                    switch (item.getItemId()) {
                        case R.id.transTaxi:
                            Toast.makeText(getApplicationContext(), "택시", Toast.LENGTH_LONG).show();
                            transSelect.setText("택시");
                    }
                    switch (item.getItemId()) {
                        case R.id.transSub:
                            Toast.makeText(getApplicationContext(), "지하철", Toast.LENGTH_LONG).show();
                            transSelect.setText("지하철");
                    }
                    switch (item.getItemId()) {
                        case R.id.transEtc:
                            Toast.makeText(getApplicationContext(), "기타", Toast.LENGTH_LONG).show();
                            transSelect.setText("기타");
                    }

                    return false;
                }
            });
            popupTransSelect.show();


        }


    }


}
