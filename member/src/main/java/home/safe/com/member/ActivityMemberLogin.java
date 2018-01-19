package home.safe.com.member;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
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
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;


public class ActivityMemberLogin extends AppCompatActivity {

    final String TAG = "로그인";

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

    // 구글 로그인 변수들
    private static final int REQUEST_CODE_GOOGLE = 1;
    private GoogleApiClient mGoogleApiClient;
    private String google = "google";

    // 네이버 로그인 변수들
    private static String OAUTH_CLIENT_ID = "W3ENfk_vTn1YQaVsG3n_";  // 1)에서 받아온 값들을 넣어좁니다
    private static String OAUTH_CLIENT_SECRET = "q3B35mxCBe";
    private static String OAUTH_CLIENT_NAME = "Cooljj";
    private String naver = "naver";

    String accessToken = "";

    String tokenType;

    OAuthLogin mOAuthLoginModule;
    OAuthLoginButton authLoginButton;
    Context mContext;

    private String sns;
    private String snsid;

    private boolean checkLoadInfo = false;

    // 체크에 필요한 메소드들
    MemberCheck memberCheck = new MemberCheck();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_login);

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

        callNaverLogin();
        callGoogleLogin();

        loadNomalData();
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
                Toast.makeText(mContext, "로그인 정보가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        // 구글 로그인 버튼 리스너
        googleLogin.setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 구글 로그인 화면을 출력합니다. 화면이 닫힌 후 onActivityResult가 실행됩니다.
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent( mGoogleApiClient );
                startActivityForResult( signInIntent, REQUEST_CODE_GOOGLE );
            }
        });

        // 네이버 로그인 버튼 리스너
        naverLogin.setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 클릭시 OAuthLogin의 생성자를 담은 것에서 네로아 액티비티를 시작한다. (인자 : 액티비티, 핸들러)
                mOAuthLoginModule.startOauthLoginActivity(ActivityMemberLogin.this,
                        mOAuthLoginHandler);
            }
        });

        // 어플 기본 로그인 메카니즘
        btnLogin.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("로그인 버튼", "눌림");
                saveNomalData();
                if(searchForID() == 1) {
                    moveToMain();
                } else {
                    Toast.makeText(mContext, "로그인 정보가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
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
    private void checkFirstLogin () {
        // 로그인 성공 -> sharePreference 저장하기
        saveSNSData();
        Toast.makeText(mContext, memberVO.getMname() + " 님 환영합니다.", Toast.LENGTH_SHORT).show();
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

    /*
    *  date     : 2017.12.03
    *  author   : Kim Jong-ha
    *  title    : callGoogleLogin 메소드 생성
    *  comment  : Google 로그인에 필요한 것들
    * */
    private void callGoogleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN )
                // 필요한 항목이 있으면 아래에 추가
                .requestEmail( )
                .requestProfile( )
                .build( );

        mGoogleApiClient = new GoogleApiClient.Builder( ActivityMemberLogin.this )
                .enableAutoManage( ActivityMemberLogin.this, new GoogleApiClient.OnConnectionFailedListener( ) {
                    @Override public void onConnectionFailed( @NonNull ConnectionResult connectionResult )
                    {
                        // 연결에 실패했을 경우 실행되는 메소드
                    }
                })
                // 필요한 api가 있으면 아래에 추가
                .addApi( Auth.GOOGLE_SIGN_IN_API, gso )
                .build( );
        googleLogin = ( RelativeLayout ) findViewById( R.id.btnGoogleLogin );
    }

    /*
    *  date     : 2017.11.29
    *  author   : Kim Jong-ha
    *  title    : callNaverLogin 메소드 생성
    *  comment  : Naver 로그인에 필요한 것들 세팅
    * */
    private void callNaverLogin() {

        Typeface type = Typeface.createFromAsset(this.getAssets(), "NanumBarunGothic_Bold(subset).otf"); // asset 폴더에 넣은 폰트 파일 명
        tvNaver.setTypeface(type);

        // 현재 어플리케이션의 life사이클에 해당하는 Context를 mContext에 담는다.
        mContext = getApplicationContext();

        // OAuthLogin를 getInstance()를 사용해서 m0AuthLoginModule에 넣는다. [싱글톤패턴]
        mOAuthLoginModule = OAuthLogin.getInstance();

        mOAuthLoginModule.init(mContext,                 // 어플리케이션 정보
                OAUTH_CLIENT_ID,            // 네아로에 등록된 접근 아이디
                OAUTH_CLIENT_SECRET,        // 네아로에 등록된 접근 비번
                OAUTH_CLIENT_NAME);         // 네이버 앱의 로그인 화면에 표시할 애플리케이션 이름. 모바일 웹의 로그인 화면을 사용할 때는 서버에 저장된 애플리케이션 이름이 표시됩니다.

        // 네아로 버튼을 resource와 연동
        btnNaver = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        //btnNaver.setOAuthLoginHandler(mOAuthLoginHandler);
        btnNaver.setBgResourceId(R.drawable.naver_login);
    }

    /*
    *  date     : 2017.11.29
    *  author   : Kim Jong-ha
    *  title    : OAuthLoginHandler 생성
    *  comment  : Naver 로그인 핸들러
    * */
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {

        @Override
        public void run(boolean success) {

            if (success) {
                // String Type
                // 이 어플리케이션에서 네로아에 대한 접근 토큰을 저장
                accessToken = mOAuthLoginModule.getAccessToken(mContext);

                // 이 어플리케이션에서 네로아에 대한 갱신 토큰을 저장
                String refreshToken = mOAuthLoginModule.getRefreshToken(mContext);

                // 이 어플리케이션에서 네로아에 대한 접근 토큰 만료기간을 저장
                long expiresAt = mOAuthLoginModule.getExpiresAt(mContext);

                // 이 어플리케이션에서 네로아에 대한 접근 토큰의 형태를 저장(MAC, Bearer 지원)
                tokenType = mOAuthLoginModule.getTokenType(mContext);

                new RequestApiTask().execute(); //로그인이 성공하면  네이버에 계정값들을 가져온다.

                // 네이버 로그인 성공시 작성할 메소드
                Intent intentData = new Intent();
                setResult(MY_LOGIN_SUCCESS_CODE, intentData);
            } else {

                // 로그인이 실패하면 에러 코드와 에러 메시지를 저장
                String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);

                Toast.makeText(ActivityMemberLogin.this, "로그인이 취소/실패 하였습니다.!",
                        Toast.LENGTH_SHORT).show();
            }
        };
    };

    /*
    *  date     : 2017.11.29
    *  author   : Kim Jong-ha
    *  title    : callGoogleLogin 메소드 생성
    *  comment  : Naver 로그인 API 작업에 필요한 부분
    * */
    // API 요청 작업
    private class RequestApiTask extends AsyncTask<Void, Void, Void> {

        // 메인 쓰레드에서 onPreExecute가 작동되고 -> dolnBackground등 의 작업 후에 onPostExecute로 결과값을 반환한다
        @Override
        protected void onPreExecute() {

        }

        //
        @Override
        protected Void doInBackground(Void... params) {

            String url = "https://openapi.naver.com/v1/nid/getUserProfile.xml";
            String at = mOAuthLoginModule.getAccessToken(mContext);

            // 정보를 넘기고 API대한 정보를 요청한다.
            Pasingversiondata(mOAuthLoginModule.requestApi(mContext, at, url));

            return null;
        }

        protected void onPostExecute(Void content) {

            // infoLoad가 ture일 경우..(모든 정보가 있을 경우)
            if ( checkLoadInfo == true ) {
                // 첫 로그인인지, 아닌지(첫 로그인이면 서버로 insert 후에 폰인증으로감
                checkFirstLogin();

                // infoLoad가 false일 경우..(이름과 이메일을 받아오지 못할 경우를 포함해서, 정보가 없을 경우)
            } else {
                Toast.makeText(ActivityMemberLogin.this,
                        "로그인 실패하였습니다.  잠시후 다시 시도해 주세요!!", Toast.LENGTH_SHORT).show();
            }
        }

        private void Pasingversiondata(String data) { // xml 파싱

            String f_array[] = new String[9];

            try {

                // <XML을 분석하는 코드>
                // 1. XmlPullParserFactory의 인스턴스 얻기
                XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();

                // 얘가 주인공
                XmlPullParser parser = parserCreator.newPullParser();

                // XmlPullParser인스턴스의 분석하고 싶은 XML을 셋팅한다
                InputStream input = new ByteArrayInputStream(data.getBytes("UTF-8"));

                // 읽은 내용(InputStream)을 parser에게 전달. 파일의 인코딩 타입을 지정해 주어야 함
                parser.setInput(input, "UTF-8");

                // 읽고 있는 태그의 종류 (문서의 시작, 문서의 끝, 시작태그, 끝태그)
                int parserEvent = parser.getEventType();

                String tag;
                boolean inText = false;

                int colIdx = 0;

                // 파싱 처리를 위한 반복 시작(태그의 끝이 아닌 문서의 끝)
                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    // 읽은 태그의 이름
                    tag = parser.getName();
                    if(tag != null) {Log.v("태그"+colIdx, tag) ;}
                    // 현재 읽고 있는 태그의 종류별로 분기
                    switch (parserEvent) {
                        // XmlPullParser.START_TAG 와  XmlPullParser.END_TAG 이벤트에서는 getName() 메소드를 사용한다. getText()사용시 null반환
                        case XmlPullParser.START_TAG:
                            // 원하는 종류의 시작 태그를 만났을 떄, 값을 추출
                            // 태그의 이름이 "~~~" 과 완전히 같다면
                            if (tag.compareTo("xml") == 0)              { inText = false;
                            } else if (tag.compareTo("data") == 0)     { inText = false;
                            } else if (tag.compareTo("result") == 0)   { inText = false;
                            } else if (tag.compareTo("resultcode") == 0) { inText = false;
                            } else if (tag.compareTo("message") == 0)   { inText = false;
                            } else if (tag.compareTo("response") == 0)  { inText = false;
                            } else { inText = true; } // xml. data, result, resultcode, message, response 를 거치고 TEXT이벤트로 간다.
                            break;

                        // TEXT 이벤트에서 임시변수에 저장된 문자열을 확인하여 적절한 객체에 저장
                        // TEXT 이벤트에서는 getName()을 쓰지말고(null을 반환) getText를 써야한다.
                        case XmlPullParser.TEXT:
                            if (inText) {
                                if (parser.getText() == null) {
                                    f_array[colIdx] = "";
                                } else {
                                    f_array[colIdx] = parser.getText().trim();
                                    colIdx++;
                                }
                            }
                            inText = false;
                            break;

                        case XmlPullParser.END_TAG:
                            inText = false;
                            break;
                    }
                    parserEvent = parser.next();
                }
            } catch (Exception e) {
                Log.e("dd", "Error in network call", e);
            }

            // 받은 값을 member에 셋팅
            MemberVO naverMemberVO = new MemberVO();
            naverMemberVO.setMid(f_array[7]);
            naverMemberVO.setMgender(f_array[4]);   // 성별
            naverMemberVO.setMname(f_array[6]);     // 이름
            naverMemberVO.setMemail(f_array[7]);    // 이메일
            naverMemberVO.setMbirth(f_array[8]);    // 생일
            naverMemberVO.setMsns("naver");
            naverMemberVO.setMsnsid(separateSNSID(f_array[7]));
            memberVO = naverMemberVO;

            for(int i = 0 ; i < 9 ; i++) {
                if(f_array[i] == null) {
                    checkLoadInfo = false;
                    break;
                } else {
                    checkLoadInfo = true;
                }
            }
        }
    }


    // 구글 로그인 반환코드, 폰인증 반환코드를 다룬다.
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        switch ( requestCode ) {
            // 구글 로그인이 성공하였을 경우의 requestCode
            case REQUEST_CODE_GOOGLE:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent( data );
                if ( result.isSuccess( ) ) { GoogleSignInAccount acct = result.getSignInAccount( );
                    // 구글 로그인 시, 반환되는 결과값들(email, name(이름 or 별명))
                    memberVO.setMid(acct.getEmail());
                    memberVO.setMname(acct.getDisplayName());
                    memberVO.setMsns(google);
                    memberVO.setMsnsid(separateSNSID(acct.getEmail()));

                    checkFirstLogin();
                }
                break;
            // 전화번호 인증 후, 성공이었을 경우의 requestCode
            case REQUEST_CODE_PHONE:
                if(resultCode == SH_JOB_OK) {
                    Toast.makeText(mContext, "인증에 성공하였습니다", Toast.LENGTH_SHORT).show();
                    memberVO.setMphone(data.getStringExtra("phone"));
                    Intent intentData = new Intent();
                    setResult(MY_LOGIN_SUCCESS_CODE, intentData);
                    finish();;
                } else {
                    Toast.makeText(mContext, "인증에 실패하였습니다", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onActivityResult( requestCode, resultCode, data );
        }
    }

    /*
    *  date     : 2017.11.12
    *  author   : Kim Jong-ha
    *  title    : separateSNSID 메소드 생성
    *  comment  : '구글'이나 '네이버' 로그인시, 받아온 E-Mail에서 snsid 를 분리한다
    */
    private String separateSNSID (String email) {
        String temp[] = email.split("@");
        String snsid = temp[0];

        return snsid;
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