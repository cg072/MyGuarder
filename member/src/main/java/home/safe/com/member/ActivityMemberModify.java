package home.safe.com.member;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityMemberModify extends AppCompatActivity {


    String settingCode;
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

    String sns;
    String id;

    MemberCheck memberCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_signup);

        memberCheck = new MemberCheck(getApplicationContext());

        Intent intent = getIntent();
        sns = intent.getStringExtra("sns");
        id = intent.getStringExtra("id");

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
        btnModify = (Button)findViewById(R.id.btnSignup);
        btnDuplicationID = (Button) findViewById(R.id.btnDuplicationID);

        if(sns != null && sns.equals("sns")) {
            etPWD.setText("SNS 계정은 지원하지 않는 기능입니다.");
            etPWD.setInputType(0);
            etPWD.setEnabled(false);
            etCheckPWD.setText("SNS 계정은 지원하지 않는 기능입니다.");
            etCheckPWD.setInputType(0);
            etCheckPWD.setEnabled(false);
        }
        // 사전 셋팅(사전에 셋팅해야할 서버로부터 받은 정보 등을 받고 셋팅)
        changeViewContents();

        // 기기에서 번호 가져오기
        MemberLoadPhoneNumber memberLoadPhoneNumber = new MemberLoadPhoneNumber(this, tvPhone);

        btnCertificationPhone.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCertDialog();
                certDialog.show();
            }
        });

        btnModify.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                int check = 0;

                String pwd = etPWD.getText().toString().trim();
                String checkPWD = etCheckPWD.getText().toString().trim();
                String name     = etName.getText().toString().trim();
                String birth    = etBirth.getText().toString().trim();
                String email    = etEMail.getText().toString().trim();

                MemberVO memberVO = new MemberVO();

                if(sns != null && sns.equals("sns")) {
                    check = 1;
                } else {
                    if(memberCheck.checkPWD(pwd, checkPWD) == true) {
                        check = 1;
                        memberVO.setMpwd(pwd);
                    }
                }
                if(check                                                        == 1    &&
                   memberCheck.checkName(name)              == true &&
                   memberCheck.checkBirth(birth)            == true &&
                   memberCheck.checkEmail(email)            == true ) {

                    if(updateInfomation() == 1) {
                        Toast.makeText(ActivityMemberModify.this, "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        saveData();
                        finish();
                    } else {
                        Toast.makeText(ActivityMemberModify.this, "수정이 오류", Toast.LENGTH_SHORT).show();
                    }
                    // 서버로 memberVO를 보냄(update)

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
        MemberVO memberVO = new MemberVO();

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
        setUserInfo(id);
    }

    private int updateInfomation() {

        int check = 0;

        MemberVO memberVO = getUserInfo();
        MemberManager memberManager = new MemberManager(getApplicationContext());

        check = memberManager.update(MemberShareWord.TARGET_SERVER, memberVO);

        return check;
    }

    private MemberVO getUserInfo(){

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

    private void setCertDialog() {
        recvCodeFromServer();
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
    }

    // 서버에 랜덤으로 조합된 인증코드를 요청하고 받은 값을 리턴 (settingCode를 이것으로 해주면됨)
    private String recvCodeFromServer() {
        MemberManager memberManager = new MemberManager(getApplicationContext());

        settingCode = memberManager.requestCode();

        return settingCode;
    }

    private void setUserInfo(String id) {
        MemberManager memberManager = new MemberManager(getApplicationContext());

        MemberVO memberVO = new MemberVO();
        memberVO.setMid(id);

        ArrayList<MemberVO> resultList = new ArrayList<MemberVO>();

        resultList = memberManager.select(MemberShareWord.TARGET_SERVER, MemberShareWord.TYPE_SELECT_CON, memberVO);

        if(resultList.size() == 1) {
            memberVO = resultList.get(0);
            etID.setText(id);
            etName.setText(memberVO.getMname());
            etBirth.setText(memberVO.getMbirth());
            etEMail.setText(memberVO.getMemail());
            switch (memberVO.getMgender()) {
                case "m" :
                    rbMale.setChecked(true);
                    break;
                case "f" :
                    rbFemale.setChecked(true);
                    break;
                case "u" :
                    rbUndefine.setChecked(true);
                    break;
            }
        }
    }

    // 아이디, 비번, 자동 로그인 여부 저장
    private void saveData() {

        MemberManager memberManager = new MemberManager(getApplicationContext());

        MemberVO memberVO = new MemberVO();
        memberVO.setMid(id);

        ArrayList<MemberVO> resultList = new ArrayList<MemberVO>();

        resultList = memberManager.select(MemberShareWord.TARGET_SERVER, MemberShareWord.TYPE_SELECT_CON, memberVO);

        if(resultList.size() == 1) {
            memberVO = resultList.get(0);
            if(memberVO.getMsns() == null) {
                Log.v("수정","비번");
                SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("MemberPWD",etPWD.getText().toString().trim());
                editor.commit();
            }
        }

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

