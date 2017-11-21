package home.safe.com.member;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMemberModify extends AppCompatActivity {

    private TextView tvID;

    private EditText etPWD;
    private EditText etName;
    private EditText etPhone;
    private EditText etBirth;
    private EditText etEMail;

    private RadioButton rbUndefine;
    private RadioButton rbFemale;
    private RadioButton rbMale;

    private Button btnPWDModify;
    private Button btnPhoneCertification;
    private Button btnModify;

    private boolean checkPermission = false;
    private String myNumber = "";

    final String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_modify_check);

        tvID = (TextView) findViewById(R.id.tvID);

        etPWD = (EditText)findViewById(R.id.etPWD);
        etName = (EditText)findViewById(R.id.etName);
        etPhone = (EditText)findViewById(R.id.etPhone);
        etBirth = (EditText)findViewById(R.id.etBirth);
        etEMail = (EditText)findViewById(R.id.etEmail);

        rbUndefine = (RadioButton)findViewById(R.id.rbUndefine);
        rbFemale = (RadioButton)findViewById(R.id.rbFemale);
        rbMale = (RadioButton)findViewById(R.id.rbMale);

        btnPWDModify = (Button)findViewById(R.id.btnPWDModify);
        btnPhoneCertification = (Button)findViewById(R.id.btnPhoneCertification);
        btnModify = (Button)findViewById(R.id.btnModify);
    }

    /*
*  date     : 2017.11.12
*  author   : Kim Jong-ha
*  title    : checkPermission 메소드 생성
*  comment  : 권한이 부여되었는지, 없다면 권한 재요청인지, 첫요청인지를 판단함
*             첫요청인지 재요청인지를 판단하는 부분은 당장은 필요한 부분이 아니나, 남겨둠
* */
    private void checkPermission()
    {
        Log.v(TAG, "checkPermission들어옴");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //퍼미션이 없는 경우
            //최초로 퍼미션을 요청하는 것인지 사용자가 취소되었던것을 다시 요청하려는건지 체크
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                //퍼미션을 재요청 하는 경우 - 왜 이 퍼미션이 필요한지등을 대화창에 넣어서 사용자를 설득할 수 있다.
                //대화상자에 '다시 묻지 않기' 체크박스가 자동으로 추가된다.
                Log.v(TAG, "퍼미션을 재요청 합니다.");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);

            } else {
                //처음 퍼미션을 요청하는 경우
                Log.v(TAG, "첫 퍼미션 요청입니다.");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            }
        } else {
            //퍼미션이 있는 경우 - 쭉 하고 싶은 일을 한다.
            checkPermission = true;

            Log.v(TAG, "Permission is granted");
            Toast.makeText(this, "\"이미 퍼미션이 허용되었습니다.\"", Toast.LENGTH_SHORT).show();
        }
        Log.v(TAG, "checkPermission나감");
    }

    /*
    *  date     : 2017.11.12
    *  author   : Kim Jong-ha
    *  title    : onRequestPermissionResult 메소드 불러옴
    *  comment  : 권한 사용or거부 요청창을 띄워 사용자가 권한을 동의, 비동의 때의 Perform을 둠
    * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: //
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //사용자가 동의했을때
                    Toast.makeText(this, "퍼미션 동의", Toast.LENGTH_SHORT).show();
                    checkPermission = true;
                    getMemberPhone();
                } else {
                    //사용자가 거부 했을때
                    Toast.makeText(this, "거부 - 동의해야 사용가능합니다.", Toast.LENGTH_SHORT).show();

                    /*new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000);*/
                }
                return;
        }
    }
    /*
    *  date     : 2017.11.12
    *  author   : Kim Jong-ha
    *  title    : getMemnerPhone 메소드 생성
    *  comment  : 권한이 부여가 되어있다면 User의 기기에서 PhoneNumber을 불러옴
    * */
    private void getMemberPhone()
    {
        checkPermission();

        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);

        if(checkPermission == true) {
            String phoneNum = telephonyManager.getLine1Number();

            TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            try {
                myNumber = mgr.getLine1Number();
                myNumber = myNumber.replace("+82", "0");
                Toast.makeText(this, myNumber, Toast.LENGTH_SHORT).show();
                etPhone.setText(myNumber);
            } catch (Exception e) {
                Toast.makeText(this, "전화번호 가져오기 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
