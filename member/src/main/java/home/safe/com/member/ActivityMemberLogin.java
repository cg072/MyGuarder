package home.safe.com.member;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import java.util.ArrayList;


public class ActivityMemberLogin extends AppCompatActivity {

    final String TAG = "로그인";

    private final static int REQUEST_CODE_GOOGLE = 1;

    private final static int MY_LOGIN_SUCCESS_CODE = 201;
    private final static int MY_END_CODE = 100;
    private final static int ROOT_LOGIN_SUCCESS_CODE = 202;
    private boolean testLoginFlag;

    private final static int SH_JOB_OK = 200;

    private final static int REQUEST_CODE_PHONE = 11;
    private boolean autoLoginCheck;

    // 기기내 파일 탐색

    // 변수 설정
    private EditText etID;
    private EditText etPWD;
    private CheckBox cboxCheck;
    private Button btnLogin;
    private OAuthLoginButton btnNaver;
    private TextView tvSignup;
    private TextView tvFIndID;
    private TextView tvFindPWD;
    private boolean checkPermission = false;
    private RelativeLayout googleLogin;
    private RelativeLayout naverLogin;
    private TextView tvNaver;
    private MemberVO memberVO = new MemberVO();

/*    // 구글 로그인 변수들
    private static final int REQUEST_CODE_GOOGLE = 1;
    private GoogleApiClient mGoogleApiClient;
    private String google = "google";*/

    private String sns;
    private String snsid;

    private boolean checkLoadInfo = false;

    // 체크에 필요한 메소드들
    MemberCheck memberCheck = new MemberCheck();

    ActivityMemberLogin activityMemberLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_login);

        activityMemberLogin = this;

        // 변수에 아이디 연동
        etID = (EditText)findViewById(R.id.etID);
        etPWD = (EditText)findViewById(R.id.etPWD);
        cboxCheck = (CheckBox)findViewById(R.id.cboxCheck);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        tvSignup = (TextView)findViewById(R.id.tvSignup);
        tvFIndID = (TextView)findViewById(R.id.tvFindID);
        tvFindPWD = (TextView)findViewById(R.id.tvFindPWD);
        googleLogin = (RelativeLayout) findViewById(R.id.btnGoogleLogin);
        naverLogin = (RelativeLayout) findViewById(R.id.btnNaverLogin);
        tvNaver = (TextView)findViewById(R.id.tvNaver);

        Typeface type = Typeface.createFromAsset(this.getAssets(), "NanumBarunGothic_Bold(subset).otf"); // asset 폴더에 넣은 폰트 파일 명
        tvNaver.setTypeface(type);

        btnNaver = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        btnNaver.setBgResourceId(R.drawable.naver_login);

        loadNomalData();
        //테스트
        test();
        // 자동로그인 체크
        if(cboxCheck.isChecked() == true) {
            if(searchForID() == 1) {
                moveToMain();
            } else if(sns != null && snsid != null) {
                MemberVO sendMemberVO = new MemberVO();
                sendMemberVO.setMsns(sns);
                sendMemberVO.setMsnsid(snsid);
                Log.v(sns,snsid);
                if(memberCheck.checkExistence(sendMemberVO, getApplicationContext()) == 1) {
                    moveToMain();
                }
            } else {
                Toast.makeText(this, "로그인 정보가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        // 구글 로그인 버튼 리스너
        googleLogin.setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 구글 로그인 화면을 출력합니다. 화면이 닫힌 후 onActivityResult가 실행됩니다.
                Intent intent = new Intent(ActivityMemberLogin.this, MemberGoogleLogin.class);
                startActivityForResult(intent, REQUEST_CODE_GOOGLE);
            }
        });

        // 네이버 로그인 버튼 리스너
        naverLogin.setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View view) {
                MemberNaverLogin memberNaverLogin = new MemberNaverLogin(activityMemberLogin);
            }
        });

        // 어플 기본 로그인 메카니즘
        btnLogin.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNomalData();
                if(searchForID() == 1) {
                    moveToMain();
                } else {
                    Toast.makeText(getApplicationContext(), "로그인 정보가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 가입 Activity로 이동
        tvSignup.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityMemberLogin.this, ActivityMemberSignup.class);
                startActivity(intent);
            }
        });

        // ID 찾기 Activity로 이동
        tvFIndID.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityMemberLogin.this, ActivityMemberFindID.class);
                startActivity(intent);
            }
        });

        // PWD 찾기 Actvity로 이동
        tvFindPWD.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityMemberLogin.this, ActivityMemberFindPWD.class);
                startActivity(intent);
            }
        });
    }

    private int  searchForID() {

        MemberManager memberManager = new MemberManager(getApplicationContext());

        String id = etID.getText().toString().trim();
        String pwd = etPWD.getText().toString().trim();

        MemberVO sendMemberVO = new MemberVO(id, pwd);

        int check = memberCheck.checkExistence(sendMemberVO, getApplicationContext());


        ArrayList<MemberVO> resultList = new ArrayList<MemberVO>();
        resultList = memberManager.select(MemberShareWord.TARGET_SERVER, MemberShareWord.TYPE_SELECT_CON, sendMemberVO);
        for(MemberVO m : resultList) {
            if (m.getMid().equals("test")) {
                testLoginFlag = false;
            } else {
                testLoginFlag = true;
            }
        }

        return check;
    }

    public void setMemberVO(MemberVO memberVO){
        this.memberVO = memberVO;
    }

    // 테스트 로그인 후 ~ (서버디비 완성 후 -> 아래의 두 메소드를 사용(주석처리됨))
    private void moveToMain() {
        // 회원일 경우이므로, 메인액티비티를 띄워준다.
        Intent intentData = new Intent();
        if(testLoginFlag == true) {
            setResult(MY_LOGIN_SUCCESS_CODE, intentData);
        } else {
            setResult(ROOT_LOGIN_SUCCESS_CODE, intentData);
        }
        finish();
    }

    // 구글, 네이버 회원은 가입 즉시, 전화번호 인증이 되어있지 않으므로, 전화번호 인증창으로 이동
    private void certPhone() {

        switch (memberCheck.checkMember(memberVO, getApplicationContext())) {
            case 0 :
                break;
            case 1 :
                moveToMain();
                break;
            case 2 :
                // 핸드폰 인증이 안된 상태이므로 전화번호 인증창으로 이동
                Intent intent = new Intent(ActivityMemberLogin.this, ActivityMemberCertPhone.class);
                intent.putExtra("id", memberVO.getMid());
                startActivityForResult(intent, REQUEST_CODE_PHONE);
                break;
        }
    }

    private int signupSNSID(){

        MemberManager memberManager = new MemberManager(getApplicationContext());

        int check = 0 ;

        // memberCheck 안의 checkExistence 메소드를 사용하여 서버의 DB에 아이디가 있는지 확인한다.
        switch (memberCheck.checkExistence(memberVO, getApplicationContext())) {
            case 0 :    // 아이디 없음
                check = memberManager.insert(MemberShareWord.TARGET_SERVER, memberVO);
                if(check != 0 ) {
                    check = 1;
                }
                break;
            case 1 :    // 아이디 1개 있음
                check = 1;
                break;
            default:    // 아이디 2개 이상 존재
        }

        return check;
    }

    // SNS같은 경우에는 첫 로그인시, insert가 되어주어야한다. 첫 로그인 후, 폰 인증 창으로 간다.
    public void checkFirstLogin () {
        // 로그인 성공 -> sharePreference 저장하기
        saveSNSData();
        Toast.makeText(this, memberVO.getMname() + " 님 환영합니다.", Toast.LENGTH_SHORT).show();
        // 첫 로그인인지, 아닌지(첫 로그인이면 서버로 insert 후에 폰인증으로감
        // SNS 아이디로 로그인 한 것인지 확인
        if(memberCheck.checkSNSID(memberVO, getApplicationContext()) == true) {
            // 서버의 DB에 아이디가 존재하는지 확인
            if (signupSNSID() == 1) {
                // 폰 인증창
                certPhone();
            }
        }
    }

    // 아이디, 비번, 자동 로그인 여부 저장
    private void saveNomalData() {
        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("MemberID",etID.getText().toString().trim());
        editor.putString("MemberPWD",etPWD.getText().toString().trim());
        editor.putBoolean("MemberAuto",cboxCheck.isChecked());

        editor.commit();
    }

    // 아이디, 비번 불러옴
    private void loadNomalData() {
        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        etID.setText(preferences.getString("MemberID",null));
        etPWD.setText(preferences.getString("MemberPWD",null));
        autoLoginCheck = preferences.getBoolean("MemberAuto", false);
        sns = preferences.getString("MemberSNS",null);
        snsid = preferences.getString("MemberSNSID",null);
        if(sns != null) {
            etID.setText("");
            etPWD.setText("");
        }

        cboxCheck.setChecked(autoLoginCheck);
    }

    private void saveSNSData(){
        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("MemberID", memberVO.getMid());
        editor.putString("MemberSNS", memberVO.getMsns());
        editor.putString("MemberSNSID",memberVO.getMsnsid());
        editor.putBoolean("MemberAuto",cboxCheck.isChecked());

        editor.commit();
    }

    // 구글 로그인 반환코드, 폰인증 반환코드를 다룬다.
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        switch ( requestCode ) {
            case REQUEST_CODE_GOOGLE:
                memberVO = (MemberVO)data.getSerializableExtra("data");
                checkFirstLogin();
                break;
            // 전화번호 인증 후, 성공이었을 경우의 requestCode
            case REQUEST_CODE_PHONE:
                if(resultCode == SH_JOB_OK) {
                    Toast.makeText(this, "인증에 성공하였습니다", Toast.LENGTH_SHORT).show();
                    memberVO.setMphone(data.getStringExtra("phone"));
                    Intent intentData = new Intent();
                    setResult(MY_LOGIN_SUCCESS_CODE, intentData);
                    finish();;
                } else {
                    Toast.makeText(this, "인증에 실패하였습니다", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onActivityResult( requestCode, resultCode, data );
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("어플을 종료하시겠습니까?");
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intentData = new Intent();
                setResult(MY_END_CODE, intentData);
                finish();
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();

    }

    // 테스트
    private void test() {
        int check = 0;
        MemberVO serverMember = new MemberVO();

        String rootID = "root";
        String rootPWD = "11111";

        serverMember.setMid(rootID);
        serverMember.setMpwd(rootPWD);

        MemberManager memberManager = new MemberManager(getApplicationContext());

        Log.v(rootID, rootPWD);
        Log.v(serverMember.getMid(), serverMember.getMpwd());

        if(memberCheck.checkExistence(serverMember, getApplicationContext()) == 0 ) {
            check = memberManager.insert(MemberShareWord.TARGET_SERVER, serverMember);
        }

        serverMember = new MemberVO();

        String testID = "test";
        String testPWD = "11111";

        serverMember.setMid(testID);
        serverMember.setMpwd(testPWD);


        if(memberCheck.checkExistence(serverMember, getApplicationContext()) == 0 ) {
            check = memberManager.insert(MemberShareWord.TARGET_SERVER, serverMember);
        }
    }
}

// 네이버 로그인 실패 원인은 2가지가 잇다.
// 가입 실패랑, 제공 거부.