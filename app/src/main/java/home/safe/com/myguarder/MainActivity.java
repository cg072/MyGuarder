package home.safe.com.myguarder;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import home.safe.com.member.ActivityMemberLogin;

/*
 * @author 경창현
 * @version 1.0.0
 * @text  로그인, 피지킴이 메인, 지킴이 메인을 관리한다.
 * @since 2017-11-15 오전 12:13
 */



public class MainActivity extends ProGuardian implements IProGuardian, View.OnClickListener {

    Button btnTest;
    Button btnMainChanger;
    Button btnMemberChanger;
    Button btnNoticeChanger;
    Button btnGuarderChanger;
    Button btnTransChanger;

    Button btnMainChangerStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity","onCreate");

        btnTest = (Button)findViewById(R.id.btnTest);
        btnMainChanger = (Button)findViewById(R.id.btnMainChanger);
        btnMemberChanger = (Button)findViewById(R.id.btnMemberChanger);
        btnNoticeChanger = (Button)findViewById(R.id.btnNoticeChanger);
        btnGuarderChanger = (Button)findViewById(R.id.btnGuarderChanger);
        btnTransChanger = (Button)findViewById(R.id.btnTransChanger);
        btnMainChangerStop = (Button)findViewById(R.id.btnMainChangerStop);


        btnTest.setOnClickListener(this);
        btnMainChanger.setOnClickListener(this);
        btnMemberChanger.setOnClickListener(this);
        btnNoticeChanger.setOnClickListener(this);
        btnGuarderChanger.setOnClickListener(this);
        btnTransChanger.setOnClickListener(this);
        btnMainChangerStop.setOnClickListener(this);


        //errer - Performing stop of activity that is not resumed
        //지연시켜서 해결
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, ActivityMemberLogin.class);
                startActivityForResult(intent, MAIN_REQUEST_MEMBER_CODE);
            }
        }, 200);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MAIN_REQUEST_MEMBER_CODE) {
//            1. 로그인시 ActivityCivilian
            if(resultCode == MY_LOGIN_SUCCESS_CODE) {
                Intent intent = new Intent(this, ActivityCivilian.class);
                startActivityForResult(intent, MY_MENU_CHANGE_CODE);
            }
//            2. root계정 로그인시 MainActivity로 이동
            if(resultCode == ROOT_LOGIN_SUCCESS_CODE)
            {
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

        if(view.getId() == btnTest.getId())
        {
            intent = new Intent(this, ActivityTest.class);
            startActivity(intent);

            Toast.makeText(this,""+btnTest.getText(),Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == btnMainChanger.getId())
        {
            //db
//            LocationManage locationManage = new LocationManage(getApplicationContext());
//            MyGuarderVO vo = new MyGuarderVO(0,"37.2316841","127.0548355","2018-01-04","07:39:23","civilianID");

            //insert
//            int res = locationManage.controller.insert(vo.locationDataToContentValues());
//            Log.d("MainActivity", "controller.insert - "+res);

            //update
//            int res = locationManage.controller.update(vo.convertDataToContentValues());
//            Log.d("MainActivity", "controller.update - "+res);

            //delete
//            int res = locationManage.remove(vo.convertDataToContentValues());
//            Log.d("MainActivity", "controller.remove - "+res);

            //search
//            List<ContentValues> list;
//            list = locationManage.controller.search(new ContentValues());
//            Log.d("MainActivity", "controller.search - "+list.size());

            //service 실행
            Toast.makeText(getApplicationContext(),"Service 시작",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, ServiceMyguarder.class);
            startService(i);


            Toast.makeText(this,""+btnMainChanger.getText(),Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == btnMainChangerStop.getId())
        {
            //service 종료
            Toast.makeText(getApplicationContext(),"Service 종료",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, ServiceMyguarder.class);
            stopService(i);
        }
        else if(view.getId() == btnMemberChanger.getId())
        {
            Toast.makeText(this,""+btnMemberChanger.getText(),Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == btnNoticeChanger.getId())
        {
            Toast.makeText(this,""+btnNoticeChanger.getText(),Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == btnGuarderChanger.getId())
        {
            Toast.makeText(this,""+btnGuarderChanger.getText(),Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == btnTransChanger.getId())
        {
            Toast.makeText(this,""+btnTransChanger.getText(),Toast.LENGTH_SHORT).show();
        }

    }

/**
 *
 * @author 경창현
 * @version 1.0.0
 * @text 서비스 중복 여부 판단 및 실행
 * @since 2018-01-12 오후 10:46
**/
    public boolean isServiceRunning()
    {
        ActivityManager manager = (ActivityManager)this.getSystemService(Activity.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            //돌아가는 서비스가 있을 경우
            if("home.safe.com.myguarder.ServiceMyguarder".equals(service.service.getClassName()))
                return true;
        }

        //돌아가는 서비스가 없는 경우
        Toast.makeText(getApplicationContext(),"Service 시작",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, ServiceMyguarder.class);
        startService(i);
        return false;
    }

}

