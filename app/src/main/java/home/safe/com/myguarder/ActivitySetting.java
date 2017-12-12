package home.safe.com.myguarder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import home.safe.com.guarder.ActivityGuarder;
import home.safe.com.member.ActivityMemberModify;
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
    Button btnNoticeSetting;
    Button btnSignOutSetting;

    ToggleButton tbCivilianMode;
    ToggleButton tbGuarderMode;

    //주기 시간
    private int cycleNum = 5;
    //메인 상태 확인
    private int MainstateFrag = 0;

    //intent PopupCycle key code
    private final static int MY_REQUEST_CODE = 1111;
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
        btnNoticeSetting = (Button)findViewById(R.id.btnNoticeSetting);
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
        btnNoticeSetting.setOnClickListener(this);
        btnSignOutSetting.setOnClickListener(this);

        tbCivilianMode.setOnClickListener(this);
        tbGuarderMode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnCycleSetting:
                Intent intent = new Intent(this, ActivityPopupCycle.class);
                startActivityForResult(intent,MY_REQUEST_CODE);
                break;
            case R.id.btnGuarderSetting:
                Intent intent2 = new Intent(this, ActivityGuarder.class);
                startActivity(intent2);
                break;
            case R.id.btnTransSetting:
                Intent intent3 = new Intent(this, ActivityTrans.class);
                startActivity(intent3);
                break;
            case R.id.btnMyInfoSetting:
                Intent intent4 = new Intent(this, ActivityMemberModify.class);
                startActivity(intent4);
                break;
            case R.id.btnNoticeSetting:
                Intent intent5 = new Intent(this, ActivityNotice.class);
                startActivity(intent5);
                break;
            case R.id.tbCivilianMode:
                if(!tbCivilianMode.isChecked())
                {
                    finish();
                }
                else {
                    tbCivilianMode.setChecked(true);
                    tbGuarderMode.setChecked(false);
                }

                break;
            case R.id.tbGuarderMode:
                if(!tbGuarderMode.isChecked())
                {
                    finish();
                }
                else
                {
                    tbCivilianMode.setChecked(false);
                    tbGuarderMode.setChecked(true);

                    Intent intent7 = new Intent(this, ActivityMyGuarder.class);
                    startActivity(intent7);
                }

                break;
            case R.id.btnSignOutSetting:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            if(requestCode == MY_REQUEST_CODE)
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
