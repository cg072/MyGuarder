package home.safe.com.myguarder;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import java.util.ArrayList;

import home.safe.com.member.ActivityMemberLogin;

/*
 * @author 경창현
 * @version 1.0.0
 * @text  추가해야할일
 *        1. 뷰 부분 부드럽게 수정 30%
 *        2. 지난위치보기 내부 구현
 *        3. 설정 버튼
 *        4. 설정 엑티비티
 *        5. 현재 위치 표시 부터 시작
 * @since 2017-11-15 오전 12:13
 */



public class MainActivity extends ProGuardian implements IProGuardian, View.OnClickListener {

    Button btnMain;
    Button btnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMain = (Button)findViewById(R.id.btnMain);
        btnTest = (Button)findViewById(R.id.btnTest);

        btnMain.setOnClickListener(this);
        btnTest.setOnClickListener(this);

        Intent intent = new Intent(this, ActivityMemberLogin.class);
        startActivityForResult(intent, MAIN_REQUEST_MEMBER_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MAIN_REQUEST_MEMBER_CODE) {

            if(resultCode == MY_LOGIN_SUCCESS_CODE) {
                Intent intent = new Intent(this, ActivityCivilian.class);
                startActivityForResult(intent, MY_MENU_CHANGE_CODE);
            }
        }
        if(requestCode == MY_MENU_CHANGE_CODE) {

            if(resultCode == MY_CIVILIAN_CODE)
            {
                Intent intent = new Intent(this, ActivityCivilian.class);
                startActivityForResult(intent, MY_MENU_CHANGE_CODE);
            }
            else if(resultCode == MY_GUARDER_CODE)
            {
                Intent intent = new Intent(this, ActivityMyGuarder.class);
                startActivityForResult(intent, MY_MENU_CHANGE_CODE);
            }
            else if(resultCode == MY_LOGOUT_CODE) {
                Intent intent = new Intent(this, ActivityMemberLogin.class);
                startActivityForResult(intent, MAIN_REQUEST_MEMBER_CODE);
            }
        }



        if(resultCode == MY_END_CODE) {
            finish();
        }
    }

    @Override
    public ArrayList searchList(ContentValues contentValues) {
        return null;
    }

    @Override
    public int modify(ContentValues contentValues) {
        return 0;
    }

    @Override
    public int insert(ContentValues contentValues) {
        return 0;
    }

    @Override
    public int remove(ContentValues contentValues) {
        return 0;
    }


    @Override
    public void onClick(View view) {

        Intent intent = null;

        if(view.getId() == btnMain.getId())
        {
            intent = new Intent(this, ActivityCivilian.class);
        } else if(view.getId() == btnTest.getId())
        {
            intent = new Intent(this, ActivityTest.class);
        }

        startActivity(intent);

    }
}

