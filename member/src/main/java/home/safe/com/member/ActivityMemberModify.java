package home.safe.com.member;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMemberModify extends AppCompatActivity implements View.OnClickListener{


    final static String settingCode = "200";
    final static String TAG = "내  정 보  수 정";

    private TextView tvTitle;

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

    private Button btnCertificationPhone;
    private Button btnModify;
    private Button btnDuplicationID;

    private boolean checkPermission = false;
    private String myNumber = "";

    ActivityMemberCertDialog certDialog = null;

    MemberCheck memberCheck = new MemberCheck();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_signup);

        tvTitle = (TextView) findViewById(R.id.tvTitle);

        etID = (EditText) findViewById(R.id.etID);

        etPWD = (EditText)findViewById(R.id.etPWD);
        etCheckPWD = (EditText)findViewById(R.id.etCheckPWD);
        etName = (EditText)findViewById(R.id.etName);
        tvPhone = (TextView)findViewById(R.id.tvPhone);
        etBirth = (EditText)findViewById(R.id.etBirth);
        etEMail = (EditText)findViewById(R.id.etEmail);

        rbUndefine = (RadioButton)findViewById(R.id.rbUndefine);
        rbFemale = (RadioButton)findViewById(R.id.rbFemale);
        rbMale = (RadioButton)findViewById(R.id.rbMale);

        btnCertificationPhone = (Button)findViewById(R.id.btnCertificationPhone);
        btnCertificationPhone.setOnClickListener(this);
        btnModify = (Button)findViewById(R.id.btnSignup);
        btnDuplicationID = (Button) findViewById(R.id.btnDuplicationID);

        // 사전 셋팅(사전에 셋팅해야할 서버로부터 받은 정보 등을 받고 셋팅)
        changeViewContents();

        // 기기에서 번호 가져오기
        getMemberPhone();

        btnModify.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pwd = etPWD.getText().toString().trim();
                String checkPWD = etCheckPWD.getText().toString().trim();
                String name     = etName.getText().toString().trim();
                String birth    = etBirth.getText().toString().trim();
                String email    = etEMail.getText().toString().trim();

                if(memberCheck.checkPWD(pwd, checkPWD, view.getContext())      == true &&
                   memberCheck.checkName(name, view.getContext())              == true &&
                   memberCheck.checkBirth(birth, view.getContext())            == true &&
                   memberCheck.checkEmail(email, view.getContext())            == true ) {

                    MemberVO memberVO = new MemberVO();
                    memberVO = setMemberVO();

                    Toast.makeText(ActivityMemberModify.this, "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    // 서버로 memberVO를 보냄(update)
                    finish();
                }
            }
        });
    }

    private void phoneCompare() {
        // 서버로부터 받은 회원의 전화번호와 기기로부터 받은 전화번호 값이 다를때,
        // 인증을 꼭 해야하도록 한다.
    }

    private void changeViewContents() {
        // 타이틀 셋팅
        tvTitle.setText(TAG);

        // 아이디 셋팅
        setID();
    }

    private void setID() {
        etID.setText("아이디 셋팅");
        etID.setEnabled(false);

        // 버튼 셋팅
        btnDuplicationID.setEnabled(false);
        btnDuplicationID.setBackgroundColor(Color.WHITE);
        btnDuplicationID.setText("");
        /* 버튼 숨기기
        btnDuplicationID.setVisibility(View.INVISIBLE);
        */

        // Password Edittext로 포커스를 옮기고 키보드를 띄운다.
        etPWD.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        /* 키보드를 숨기는 부분
        InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        */
    }

    private int sendDataToServer() {

        int check = 0;

        MemberVO memberVO = setMemberVO();
        MemberManager memberManager = new MemberManager(getApplicationContext());

        check = memberManager.update(MemberShareWord.TARGET_SERVER, memberVO);

        return check;
    }

    private MemberVO setMemberVO(){

        String id = etID.getText().toString().trim();
        String pwd = etPWD.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String phone = removeHyphen(tvPhone.getText().toString().trim());  // 하이픈 제거해서 세팅
        String birth = etBirth.getText().toString().trim();
        String email = etEMail.getText().toString().trim();
        String gender = "u";
        // 성별 판단 f,m,u
        if (rbFemale.isChecked()) {
            gender = "f";
        } else if (rbMale.isChecked()) {
            gender = "m";
        } else if (rbUndefine.isChecked()) {
            gender = "u";
        }

        MemberVO memberVO = new MemberVO(id, pwd, name, phone, birth, email, gender);

        return memberVO;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnCertificationPhone) {
            certDialog = new ActivityMemberCertDialog(ActivityMemberModify.this);
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
                        Toast.makeText(ActivityMemberModify.this, settingCode + "같아" + certDialog.getSendCode(), Toast.LENGTH_SHORT).show();
                        //tvPhone.setEnabled(false);
                        btnCertificationPhone.setEnabled(false);
                        btnModify.setEnabled(true);
                    } else {
                        Toast.makeText(ActivityMemberModify.this, "전화번호 인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            certDialog.show();
        }
    }

    // 서버에 랜덤으로 조합된 인증코드를 요청하고 받은 값을 리턴 (settingCode를 이것으로 해주면됨)
    private String recvCodeFromServer() {
        String requestCode = "";

        MemberManager memberManager = new MemberManager(getApplicationContext());

        requestCode = memberManager.requestCode();

        return requestCode;
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
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                //퍼미션을 재요청 하는 경우 - 왜 이 퍼미션이 필요한지등을 대화창에 넣어서 사용자를 설득할 수 있다.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);

            } else {
                //처음 퍼미션을 요청하는 경우
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
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
                    finish();
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
                resultString = "Not Mobile";
        }
        return resultString;
    }


    /*
     *  date     : 2017.11.22
     *  author   : Kim Jong-ha
     *  title    : removeHyphen() 메소드 생성
     *  comment  : 전화 번호 사이의 '-' 를 제거한다
     *  return   : String 형태
     * */
    private String removeHyphen(String phone) {

        String[] basePhone = phone.split("-");
        String resultPhone = basePhone[0];

        if(phone.contains(phone)) {
            int check = 0;
            for(int i = 0 ; i < phone.length() ; i++) {
                if(phone.charAt(i) == '-') {
                    check++;
                }
            }
            switch ( check ) {
                // check = 0 일때는 resultPhone에 이미 basePhone[0] 이 들어있음
                case 1 :
                    resultPhone += basePhone[1];
                    break;
                case 2 :
                    resultPhone = basePhone[1] + basePhone[2];
                    break;
            }
        }
        return resultPhone;
    }
}

