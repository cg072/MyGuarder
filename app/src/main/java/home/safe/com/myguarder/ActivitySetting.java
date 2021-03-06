package home.safe.com.myguarder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import home.safe.com.guarder.ActivityGuarder;
import home.safe.com.member.ActivityMemberLogin;
import home.safe.com.member.ActivityMemberModifyCheck;
import home.safe.com.notice.ActivityNotice;
import home.safe.com.trans.ActivityTrans;

/**
*
* @author 경창현
* @version 1.0.0
* @text Setting
 * - 전송주기 : 주기 받아서 오는것까지
 * - 지킴이관리 : 버튼 눌림
* @since 2017-11-30 오후 4:58
**/
public class ActivitySetting extends AppCompatActivity implements View.OnClickListener{

    Button btnCycleSetting;
    Button btnGuarderSetting;
    Button btnTransSetting;
    Button btnMyInfoSetting;
//    Button btnNoticeSetting;
    Button btnSignOutSetting;

    ToggleButton tbCivilianMode;
    ToggleButton tbGuarderMode;

    //주기 시간
    private int cycleNum = 5;
    //메인 상태 확인
    private int MainstateFrag = 0;

    //intent PopupCycle key code
    private final static int MY_REQUEST_CODE = 1111;
    private final static int MY_LOGOUT_CODE = 300;
    public final static int MY_CIVILIAN_CODE = 601;
    public final static int MY_GUARDER_CODE = 602;
    private final static String DATA_NAME = "cycle";
    private final static int DEFAULT_NUMBER = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Log.d("Setting","onCreate()");

        btnCycleSetting = (Button)findViewById(R.id.btnCycleSetting);
        btnGuarderSetting = (Button)findViewById(R.id.btnGuarderSetting);
        btnTransSetting = (Button)findViewById(R.id.btnTransSetting);
        btnMyInfoSetting = (Button)findViewById(R.id.btnMyInfoSetting);
//        btnNoticeSetting = (Button)findViewById(R.id.btnNoticeSetting);
        btnSignOutSetting = (Button)findViewById(R.id.btnSignOutSetting);

        tbCivilianMode = (ToggleButton)findViewById(R.id.tbCivilianMode);
        tbGuarderMode = (ToggleButton)findViewById(R.id.tbGuarderMode);

        if(".ActivityCivilian".equals(getCallingActivity().getShortClassName()))
        {
            tbCivilianMode.setChecked(true);
            tbGuarderMode.setChecked(false);
            btnCycleSetting.setEnabled(true);
            btnGuarderSetting.setEnabled(true);
        }
        else if(".ActivityMyGuarder".equals(getCallingActivity().getShortClassName()))
        {
            tbCivilianMode.setChecked(false);
            tbGuarderMode.setChecked(true);
            btnCycleSetting.setEnabled(false);
            btnGuarderSetting.setEnabled(false);
        }

        btnCycleSetting.setOnClickListener(this);
        btnGuarderSetting.setOnClickListener(this);
        btnTransSetting.setOnClickListener(this);
        btnMyInfoSetting.setOnClickListener(this);
//        btnNoticeSetting.setOnClickListener(this);
        btnSignOutSetting.setOnClickListener(this);

        tbCivilianMode.setOnClickListener(this);
        tbGuarderMode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId())
        {
            case R.id.btnCycleSetting:
                intent = new Intent(this, ActivityPopupCycle.class);
                startActivityForResult(intent,MY_REQUEST_CODE);
                break;
            case R.id.btnGuarderSetting:
                intent = new Intent(this, ActivityGuarder.class);
                startActivity(intent);
                break;
            case R.id.btnTransSetting:
                intent = new Intent(this, ActivityTrans.class);
                startActivity(intent);
                break;
            case R.id.btnMyInfoSetting:
                intent = new Intent(this, ActivityMemberModifyCheck.class);
                startActivity(intent);
                break;
//            case R.id.btnNoticeSetting:
//                intent = new Intent(this, ActivityNotice.class);
//                startActivity(intent);
//                break;
            case R.id.tbCivilianMode:


                if(tbGuarderMode.isChecked()) {
                    tbGuarderMode.setChecked(false);
                    Intent intentData = new Intent();
                    setResult(MY_CIVILIAN_CODE, intentData);
                }
                else
                {
                    tbCivilianMode.setChecked(true);

                }

//                intent = new Intent(this, ActivityCivilian.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);

                finish();

                break;
            case R.id.tbGuarderMode:


                if(tbCivilianMode.isChecked()) {
                    tbCivilianMode.setChecked(false);
                    Intent intentData = new Intent();
                    setResult(MY_GUARDER_CODE, intentData);
                }
                else
                {
                    tbGuarderMode.setChecked(true);
                }

//                intent = new Intent(this, ActivityMyGuarder.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);

                finish();

                break;
            case R.id.btnSignOutSetting:

                saveData();

                Intent intentData = new Intent();
                setResult(MY_LOGOUT_CODE, intentData);
                finish();
                break;
        }

    }

    private void saveData()
    {
        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("MemberAuto",false);
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MY_REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                cycleNum = data.getIntExtra(DATA_NAME, DEFAULT_NUMBER);
                btnCycleSetting.setText("전송주기 - "+cycleNum+"분");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Setting","onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Setting","onResume()");

        Intent intentData = new Intent();
        intentData.putExtra(DATA_NAME, cycleNum);
        setResult(RESULT_OK, intentData);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Setting","onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Setting","onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Setting","onDestroy()");
    }
}
