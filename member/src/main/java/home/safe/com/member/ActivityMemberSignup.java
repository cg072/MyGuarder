package home.safe.com.member;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityMemberSignup extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "ActivityMemberSignup";

    private static final String settingCode = "200";

    private static final String typeDuplication = "ID 중복 체크를";
    private static final String typeCertification = "전화 번호 인증을";

    private EditText etID;
    private EditText etPWD;
    private EditText etCheckPWD;
    private EditText etName;
    private TextView tvPhone;
    private EditText etBirth;
    private EditText etEMail;
    private RadioButton rbUndefine;
    private RadioButton rbFemale;
    private RadioButton rbMale;

    private Button btnDuplicationID;
    private Button btnCertificationPhone;
    private Button btnSignup;

    private String myNumber = "";
    private Boolean checkPermission = false;

    private MemberVO memberVO = new MemberVO();

    private ActivityMemberCertDialog certDialog = null;

    MemberCheck memberCheck = new MemberCheck();

    final String testID = "kkkk1111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_signup);

        etID = (EditText)findViewById(R.id.etID);
        etPWD = (EditText)findViewById(R.id.etPWD);
        etCheckPWD = (EditText)findViewById(R.id.etCheckPWD);
        etName = (EditText)findViewById(R.id.etName);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        etBirth = (EditText)findViewById(R.id.etBirth);
        etEMail = (EditText)findViewById(R.id.etEmail);
        rbUndefine = (RadioButton)findViewById(R.id.rbUndefine);
        rbFemale = (RadioButton)findViewById(R.id.rbFemale);
        rbMale = (RadioButton)findViewById(R.id.rbMale);

        btnDuplicationID = (Button) findViewById(R.id.btnDuplicationID);
        btnSignup = (Button)findViewById(R.id.btnSignup);
        btnCertificationPhone = (Button)findViewById(R.id.btnCertificationPhone);
        btnCertificationPhone.setOnClickListener(this);

        // 중복체크
        btnDuplicationID.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = etID.getText().toString().trim();

                if( memberCheck.checkID(id, view.getContext()) &&      // 아이디 조건에 적합하고
                    !id.equals(testID)                            ) {   // 중복이 아니라면

                    etID.setEnabled(false);
                    btnDuplicationID.setEnabled(false);
                    Toast.makeText(ActivityMemberSignup.this, "사용가능한 ID입니다", Toast.LENGTH_SHORT).show();
                }
                // 서버로 아이디 보낸다. 중복이 없으면 isEnable를 false로 해놓고 가입버튼시 false인지 체크한다.
                else {
                    Toast.makeText(ActivityMemberSignup.this, "ID 중복", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSignup.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pwd      = etPWD.getText().toString().trim();
                String checkPWD = etCheckPWD.getText().toString().trim();
                String name     = etName.getText().toString().trim();
                boolean btnDupli    = btnDuplicationID.isEnabled();
                boolean btnCert    = btnDuplicationID.isEnabled();
                String birth    = etBirth.getText().toString().trim();
                String email    = etEMail.getText().toString().trim();

                if (memberCheck.checkBtn(typeDuplication, btnDupli, view.getContext())   == true &&  // 중복 버튼이 비활성화 되어있고
                    memberCheck.checkPWD(pwd, checkPWD, view.getContext())                 == true &&  // 비번이 형식이 맞고
                    memberCheck.checkName(name , view.getContext())                        == true &&  // 이름이 기입되어있다면
                    memberCheck.checkBtn(typeCertification, btnDupli, view.getContext()) == true &&  // 인증 버튼이 비활성화 되어있고
                    memberCheck.checkBirth(birth, view.getContext())                       == true &&  // 생년월일 체크가 되어있고(빈칸가능)
                    memberCheck.checkEmail(email, view.getContext())                       == true  ) {// 이메일 체크가 되었다면(빈칸가능)

                    memberVO.setMid(etID.getText().toString().trim());
                    memberVO.setMpwd(etPWD.getText().toString().trim());
                    memberVO.setMname(etName.getText().toString().trim());
                    memberVO.setMphone(hyphenRemove(tvPhone.getText().toString().trim()));  // 하이픈 제거해서 세팅
                    memberVO.setMbirth(etBirth.getText().toString().trim());
                    memberVO.setMemail(etEMail.getText().toString().trim());


                    // 성별 판단 f,m,u
                    if (rbFemale.isChecked()) {
                        memberVO.setMgender("f");
                    } else if (rbMale.isChecked()) {
                        memberVO.setMgender("m");
                    } else if (rbUndefine.isChecked()) {
                        memberVO.setMgender("u");
                    }

                    Toast.makeText(ActivityMemberSignup.this, "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    // 서버로 memberVO를 보냄(update)
                    finish();
                }
            }
        });

        getMemberPhone();
        setTestSignup();
    }


    private void setTestSignup() {
        etID.setText("abcd123");
        etPWD.setText("aAbBcC11");
        etCheckPWD.setText("aAbBcC11");
        etName.setText("김종핰");
        etBirth.setText("");
        etEMail.setText("sdf@fds.com");
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnCertificationPhone) {
            certDialog = new ActivityMemberCertDialog(ActivityMemberSignup.this);
            certDialog.setCancelable(false);
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
                    if (settingCode.equals(certDialog.getSendCode())) {
                        Toast.makeText(ActivityMemberSignup.this, settingCode + "같아" + certDialog.getSendCode(), Toast.LENGTH_SHORT).show();
                        //tvPhone.setEnabled(false);
                        btnCertificationPhone.setEnabled(false);
                        btnSignup.setEnabled(true);
                    } else {
                        Toast.makeText(ActivityMemberSignup.this, "전화번호 인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            certDialog.show();
        }
    }

/*    private boolean checkName(String name) {
        boolean check = false;

        if( name.length() > 0 ) {
            check = true;
        } else {
            Toast.makeText(ActivityMemberSignup.this, "이름 기입 필수", Toast.LENGTH_SHORT).show();
        }

        return  check;
    }

    private boolean checkPWD(String pwd) {
        boolean check = false;

        if( pwd.length() >= 7 ) {
            check = true ;
        } else {
            Toast.makeText(ActivityMemberSignup.this, "Password 7자리 이상 요구", Toast.LENGTH_SHORT).show();
        }

        return check;
    }

    private boolean checkPWDEqual() {
        boolean check = false;

        if(etPWD.getText().toString().trim().equals(etCheckPWD.getText().toString().trim())) {
            check = true;
        } else {
            Toast.makeText(this, "Password가 서로 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
        return check;
    }

    private boolean checkID(String id) {
        boolean check = false;
        int checkStr = 0;

        if( id.length() >= 6 ) {
            for( int i = 0 ; i < id.length() ; i++) {

                long hexLong = toHex(id.charAt(i));

                Log.v("차",String.valueOf(id.charAt(i)));
                Log.v("롱",String.valueOf(hexLong));

                if((hexLong >= toHex('0') && hexLong <= toHex('9')) ||  // 숫자
                   (hexLong >= toHex('A') && hexLong <= toHex('Z')) ||  // 대문자
                   (hexLong >= toHex('a') && hexLong <= toHex('z')) )  {// 소문자
                    // 아이디 적합
                } else {
                    checkStr += 1;
                }
            }
            if(checkStr < 1) {
                check = true ;
            } else {
                Toast.makeText(this, "영문자, 숫자만 가능", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "ID 6자리 이상 요구", Toast.LENGTH_SHORT).show();
        }

        return check;
    }

    private long toHex(char ch) {
        String hexStr = Integer.toHexString(ch);

        Log.v("스트링",String.valueOf(hexStr));

        long hexLong = Long.parseLong(hexStr, 16);

        return hexLong;
    }*/

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
            Log.v(TAG, "들어옴?");
            String phoneNum = telephonyManager.getLine1Number();

            TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            try {
                myNumber = mgr.getLine1Number();
                myNumber = myNumber.replace("+82", "0");
                Toast.makeText(this, myNumber, Toast.LENGTH_SHORT).show();
                tvPhone.setText(hyphenAdd(myNumber));
            } catch (Exception e) {
                Toast.makeText(this, "전화번호 가져오기 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
     *  date     : 2017.11.22
     *  author   : Kim Jong-ha
     *  title    : hyphenAdd() 메소드 생성
     *  comment  : 전화 번호 사이의 '-' 를 추가한다
     *  return   : String 형태
     * */
    private String hyphenAdd(String phone) {

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

    /*
     *  date     : 2017.11.22
     *  author   : Kim Jong-ha
     *  title    : hyphenRemove() 메소드 생성
     *  comment  : 전화 번호 사이의 '-' 를 제거한다
     *  return   : String 형태
     * */
    private String hyphenRemove(String phone) {

        String[] basePhone = phone.split("-");

        Log.v(TAG, "나눔"+basePhone[0].length()+ " " + basePhone[0]);
        String resultPhone = basePhone[0];
        if(basePhone[0].length() < 10) {
            resultPhone = resultPhone + basePhone[1] + basePhone[2];
        }

        return resultPhone;
    }
}
