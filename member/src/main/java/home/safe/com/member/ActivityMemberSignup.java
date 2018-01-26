package home.safe.com.member;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ActivityMemberSignup extends AppCompatActivity {

    String settingCode;

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

    private ActivityMemberCertDialog certDialog = null;

    MemberCheck memberCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_signup);

        memberCheck = new MemberCheck(getApplicationContext());

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

        // 기기의 PhoneNumber 정보 가져오기
        MemberLoadPhoneNumber memberLoadPhoneNumber = new MemberLoadPhoneNumber(this);

        tvPhone.setText(addHyphen(memberLoadPhoneNumber.getMyPhoneNumber()));

        // 중복체크
        btnDuplicationID.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = etID.getText().toString().trim();
                MemberVO memberVO = new MemberVO();
                memberVO.setMid(id);

                if( memberCheck.checkID(id)                         &&  // 아이디 조건에 적합하고
                    memberCheck.checkExistence(memberVO) == 0 ) { // 중복이 아니라면

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

        btnCertificationPhone.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCertDialog();
                certDialog.show();
            }
        });

        btnSignup.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                MemberVO memberVO = setMemberVO();      // View 안의 모든 데이터를 member에 담는다.

                if(checkData(memberVO) == true) {      // 형식에 알맞은 데이터들인지 체크한다.
                    if (sendDataForInsertToServer(memberVO) != 0) {     // 서버에 삽입 요청

                        Toast.makeText(ActivityMemberSignup.this, "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        // 서버로 memberVO를 보냄(update)
                    } else {                                            // 서버에 삽입 실패
                        Toast.makeText(ActivityMemberSignup.this, "가입 불가. 관리자에게 문의하십시오", Toast.LENGTH_SHORT).show();
                    }
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


    private void setCertDialog() {
        recvCodeFromServer();

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
                    btnCertificationPhone.setEnabled(false);
                    btnSignup.setEnabled(true);
                } else {
                    Toast.makeText(ActivityMemberSignup.this, "전화번호 인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int sendDataForDuplicationToServer(){
        int check = 0;

        String id = etID.getText().toString().trim();

        MemberVO memberVO = new MemberVO();
        memberVO.setMid(id);

        return memberCheck.checkExistence(memberVO);
    }

    private int sendDataForInsertToServer(MemberVO memberVO) {

        int check = 0;

        MemberManager memberManager = new MemberManager(getApplicationContext());

        check = memberManager.insert(MemberShareWord.TARGET_SERVER, memberVO);

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

    private boolean checkData(MemberVO memberVO) {
        boolean check = false;

        String checkPWD     = etCheckPWD.getText().toString().trim();
        boolean btnDupli   = btnDuplicationID.isEnabled();
        boolean btnCert    = btnDuplicationID.isEnabled();

        if (memberCheck.checkBtn(typeDuplication, btnDupli)        == true &&  // 중복 버튼이 비활성화 되어있고
                memberCheck.checkPWD(memberVO.getMpwd(), checkPWD)   == true &&  // 비번이 형식이 맞고
                memberCheck.checkName(memberVO.getMname() )          == true &&  // 이름이 기입되어있다면
                memberCheck.checkBtn(typeCertification, btnCert)   == true &&  // 인증 버튼이 비활성화 되어있고
                memberCheck.checkBirth(memberVO.getMbirth())         == true &&  // 생년월일 체크가 되어있고(빈칸가능)
                memberCheck.checkEmail(memberVO.getMemail())         == true  ) {// 이메일 체크가 되었다면(빈칸가능)

            check = true;

            finish();
        }
        return check;
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
                    resultPhone = resultPhone + basePhone[1] + basePhone[2];
                    break;
            }
        }
        return resultPhone;
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
