package home.safe.com.member;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMemberCertPhone extends AppCompatActivity implements View.OnClickListener{

    private TextView tvPhone;
    private String myNumber = "";
    final private static String TAG = "전화번호 인증";
    private Button btnCert;
    private int SH_JOB_OK = 200;
    private int SH_JOB_FALSE = 400;
    private boolean certCheck = false;
    private boolean checkPermission = false;
    private ActivityMemberCertDialog certDialog;

    // 예제용
    private String settingCode = "200";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_cert_phone);

        tvPhone = (TextView) findViewById(R.id.tvPhone);
        btnCert = (Button) findViewById(R.id.btnCert);

        getMemberPhone();

        btnCert.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnCert) {
            certDialog = new ActivityMemberCertDialog(ActivityMemberCertPhone.this);

            certDialog.setOnShowListener((new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    // 서버로부터 받은 값을 셋팅하여 준다. [후에 code에 값을 서버로부터 받은 값으로~!]
                    certDialog.setRecvCode(settingCode);

                }
            }));
            certDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if(settingCode.equals(certDialog.getSendCode()))
                    {
                        //Toast.makeText(ActivityMemberCertPhone.this, settingCode + "같아" + certDialog.getSendCode(), Toast.LENGTH_SHORT).show();
                        certCheck = true;
                    }
                    else
                    {
                        Toast.makeText(ActivityMemberCertPhone.this, "전화번호 인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent();
                    intent.putExtra("phone", myNumber);
                    if(certCheck == true) {
                        setResult(SH_JOB_OK, intent);
                        //setResult(SH_JOB_OK);
                    } else {
                        setResult(SH_JOB_FALSE, intent);
                        //setResult(SH_JOB_FALSE);
                    }
                    finish();
                }
            });
            certDialog.show();
        }
    }

    /*
    *  date     : 2017.11.12
    *  author   : Kim Jong-ha
    *  title    : checkPermission 메소드 생성
    *  comment  : 권한이 부여되었는지, 없다면 권한 재요청인지, 첫요청인지를 판단함
    *             첫요청인지 재요청인지를 판단하는 부분은 당장은 필요한 부분이 아니나, 남겨둠
    * */
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //퍼미션이 없는 경우
            //최초로 퍼미션을 요청하는 것인지 사용자가 취소되었던것을 다시 요청하려는건지 체크
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_PHONE_STATE)) {
                //퍼미션을 재요청 하는 경우 - 왜 이 퍼미션이 필요한지등을 대화창에 넣어서 사용자를 설득할 수 있다.
                //대화상자에 '다시 묻지 않기' 체크박스가 자동으로 추가된다.
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 1);

            } else {
                //처음 퍼미션을 요청하는 경우
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 1);
            }
        } else {
            //퍼미션이 있는 경우 - 쭉 하고 싶은 일을 한다.
            checkPermission = true;
            Toast.makeText(this, "\"이미 퍼미션이 허용되었습니다.\"", Toast.LENGTH_SHORT).show();
        }
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
                tvPhone.setText(addHyphen(myNumber));
            } catch (Exception e) {
                Toast.makeText(this, "전화번호 가져오기 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
     *  date     : 2017.11.22
     *  author   : Kim Jong-ha
     *  title    : addHyphen() 메소드 생성
     *  comment  : 전화 번호 사이의 '-' 를 추가한다
     *  return   : String 형태
     * */
    private String addHyphen(String phone) {

        String resultString = phone;

        switch(resultString.length()) {
            case 10 :
                resultString =  resultString.substring(0,3) + "-" +
                        resultString.substring(3,6) + "-" +
                        resultString.substring(6,10);
                break;

            case 11 :
                resultString =  resultString.substring(0,3) + "-" +
                        resultString.substring(3,7) + "-" +
                        resultString.substring(7,11);
                break;
            default :
                resultString = "Error";
        }
        return resultString;
    }
}
