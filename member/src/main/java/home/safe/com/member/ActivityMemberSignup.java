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
                    memberVO.setMpwd(pwd);
                    memberVO.setMname(name);
                    memberVO.setMphone(removeHyphen(tvPhone.getText().toString().trim()));  // 하이픈 제거해서 세팅
                    memberVO.setMbirth(birth);
                    memberVO.setMemail(email);

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

        MemberLoadPhoneNumber memberLoadPhoneNumber = new MemberLoadPhoneNumber(this, tvPhone);
        setTestSignup();
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

    // 서버에 랜덤으로 조합된 인증코드를 요청하고 받은 값을 리턴 (settingCode를 이것으로 해주면됨)
    private String recvCodeFromServer() {
        String requestCode = "";

        MemberManager memberManager = new MemberManager(getApplicationContext());

        requestCode = memberManager.requestCode();

        return requestCode;
    }

    private int sendDataForDuplicationToServer(){
        int check = 0;

        String id = etID.getText().toString().trim();

        MemberVO memberVO = new MemberVO();
        memberVO.setMid(id);
        MemberManager memberManager = new MemberManager(getApplicationContext());

        ArrayList<MemberVO> resultList = new ArrayList<MemberVO>();
        resultList = memberManager.select(MemberShareWord.TARGET_SERVER, MemberShareWord.TYPE_SELECT_CON, memberVO);

        if(resultList == null) {
            check = 1;
        }

        return check;
    }

    private int sendDataForInsertToServer() {

        int check = 0;

        MemberVO memberVO = setMemberVO();
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

    // 테스트용
    private void setTestSignup() {
        etID.setText("abcd123");
        etPWD.setText("aAbBcC11");
        etCheckPWD.setText("aAbBcC11");
        etName.setText("김종핰");
        etBirth.setText("");
        etEMail.setText("sdf@fds.com");
    }

}
