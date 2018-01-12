package home.safe.com.member;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import java.util.HashMap;
import java.util.Map;


public class ActivityMemberLogin extends AppCompatActivity {

    final String TAG = "로그인";

    private final static int MY_LOGIN_SUCCESS_CODE = 201;
    private final static int MY_END_CODE = 100;
    private final static int ROOT_LOGIN_SUCCESS_CODE = 202;
    private boolean testLoginFlag;

    private final static String LOGIN_ID = "loginID";
    private final static String LOGIN_PWD = "loginPWD";

    private final static int SH_JOB_OK = 200;

    private final static int REQUEST_CODE_PHONE = 11;
    private boolean autoLoginCheck;

    final static String TYPE_INSERT = "insert";
    final static String TYPE_DELETE = "delete";
    final static String TYPE_UPDATE = "update";
    final static String TYPE_SELECT_ALL = "selectAll";
    final static String TYPE_SELECT_CON = "selectPart";
    final static String TARGET_SERVER = "server";
    final static String TARGET_DB = "db";

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
    //private static final int RESOLVE_CONNECTION_REQUEST_CODE = 1;
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
    // 네이버 로그인 변수들

    // memberVO에 필요 없는 변수들
    private String nickname;                 // nickname        = f_array[0];
    private String enc_id;                   // enc_id          = f_array[1];
    private String profile_image;           // 프로필 이미지   = f_array[2];
    private String age; ;                    // 나이대          = f_array[3]
    private String id;                       // 고유 ID         = f_array[5];

    private boolean checkLoadInfo = false;

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

        loadSharePreData();


        // 자동로그인 체크
        if(cboxCheck.isChecked() == true) {
            if(sendLogInfoToServer() == 1) {
                goMainTest();
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
                saveData();
                //testDB();
                if(sendLogInfoToServer() == 1) {
                    goMainTest();
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

    // 테스트 로그인
    private void testDB(){
        MemberManager memberManager = new MemberManager(getApplicationContext());
        MemberVO memberVO = new MemberVO();
        memberManager.insert(TARGET_DB, memberVO);
    }

    // 테스트 로그인 후 ~ (서버디비 완성 후 -> 아래의 두 메소드를 사용(주석처리됨))
    private void goMainTest() {
        Log.v("로그인", "가즈아");
        // 회원일 경우이므로, 메인액티비티를 띄워준다.
        Intent intentData = new Intent();
        if(testLoginFlag == true) {
            setResult(MY_LOGIN_SUCCESS_CODE, intentData);
        } else {
            setResult(ROOT_LOGIN_SUCCESS_CODE, intentData);
        }
        finish();
    }
    
    private int checkLogInfo() {
        int check = 0;
        if(sendLogInfoToServer() == 1) {
            check = 1;
        }
        
        return check;
    }

/*    private int sendToServer() {

        int check = 0;

        return check;
    }

   private void moveToMain(){
        switch(sendToServer()){
            case 0 :
                Toast.makeText(mContext, "로그인 정보가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                break;
            case 1 :
                Intent intentData = new Intent();
                if(testLoginFlag == true) {
                    setResult(MY_LOGIN_SUCCESS_CODE, intentData);       // 테스터 로그인(회원)
                } else {
                    setResult(ROOT_LOGIN_SUCCESS_CODE, intentData);     // 운영자 로그인
                }
                finish();
                break;
        }
    }*/

/*    // 테스트용 아이디 세팅(서버 역활중)
    private boolean setTestLogin() {
        testLoginFlag = false;
        String rootID = "root";
        String rootPWD = "11111";
        String testID = "test";
        String testPWD = "11111";

        if((etID.getText().toString().equals(rootID) && etPWD.getText().toString().equals(rootPWD)) ||
                (etID.getText().toString().equals(testID) && etPWD.getText().toString().equals(testPWD))) {
            if(etID.getText().toString().equals(rootID)) {
                testLoginFlag = true;
            }
            return true;
        }
        return false;
    }*/

    private int testServer(MemberVO memberVO){
        int check = 0;
        ArrayList<MemberVO> serverList = new ArrayList<MemberVO>();
        MemberVO serverMember = new MemberVO();

        String rootID = "root";
        String rootPWD = "11111";
        
        serverMember.setMid(rootID);
        serverMember.setMpwd(rootPWD);

        serverList.add(serverMember);

        serverMember = new MemberVO();

        String testID = "test";
        String testPWD = "11111";

        serverMember.setMid(testID);
        serverMember.setMpwd(testPWD);

        serverList.add(serverMember);

        Log.v("체크값1", memberVO.getMid());
        Log.v("체크값1", memberVO.getMpwd());

        for(MemberVO m : serverList) {
            Log.v("체크값2", "들어옴?2");
            Log.v("체크값2", m.getMid());
            Log.v("체크값2", m.getMpwd());
            if(m.getMid().equals(memberVO.getMid()) && m.getMpwd().equals(memberVO.getMpwd())) {
                Log.v("체크값3", "들어옴?3");
                check = 1;
                if (m.getMid().equals(rootID)) {
                    testLoginFlag = false;
                } else {
                    testLoginFlag = true;
                }
                
            }
        }
        Log.v("체크값", String.valueOf(check));
        return check;
    }

    private int sendLogInfoToServer() {

        int check = 0;

        String sendID = etID.getText().toString().trim();
        String sendPWD = etPWD.getText().toString().trim();
        String recvID = "";
        String recvPWD = "";
        int checkLog = 0;
        MemberVO memberVO = new MemberVO(sendID, sendPWD);

        check =testServer(memberVO);

        Log.v("체크값-2", String.valueOf(check));

        return check;
/*        ArrayList<MemberVO> resultList = sendDataForList(TARGET_SERVER, TYPE_SELECT_CON, memberVO);
        if(resultList != null) {
            int check = 0;
            for(MemberVO m : resultList) {
                check++;
                if(sendID.equals(m.getMid()) && sendPWD.equals(m.getMpwd())) {
                    checkLog = 1;
                }
            }
            if(check != 1) {
                checkLog = 0;
            }
        }
        return checkLog; */
    }

    // int값으로 반환
    private int sendDataForCehck (String target, String type, MemberVO memberVO) {
        // db로 데이터를 보냄
        MemberManager memberManager = new MemberManager(getApplicationContext());

        int check = 0;

        switch (type) {
            case TYPE_INSERT :
                return memberManager.insert(target, memberVO);
            case TYPE_DELETE :
                return memberManager.delete(target, memberVO);
            case TYPE_UPDATE :
                return memberManager.update(target, memberVO);
        }

        return check;
    }

    // 리스트 형식으로 반환
    private ArrayList<MemberVO> sendDataForList(String target, String type, MemberVO memberVO) {

        MemberManager memberManager = new MemberManager(getApplicationContext());

        switch (type) {
            case TYPE_SELECT_ALL :
                return memberManager.select(target, type, memberVO);
            case TYPE_SELECT_CON:
                return memberManager.select(target, type, memberVO);
        }

        return null;
    }

    private Map<String, String> receiveData () {
        // 데이터를 받아서
        Map<String, String> map = new HashMap<String, String>();
        map = (Map)map;  // 여따 캐스팅해서 넣고

        return map;
    }

    // 아이디, 비번, 자동 로그인 여부 저장
    private void saveData()
    {
        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("MemberID",etID.getText().toString().trim());
        editor.putString("MemberPWD",etPWD.getText().toString().trim());
        editor.putBoolean("MemberAuto",cboxCheck.isChecked());
        editor.commit();
    }

    // 아이디, 비번 불러옴
    private void loadSharePreData()
    {
        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        etID.setText(preferences.getString("MemberID","root"));
        etPWD.setText(preferences.getString("MemberPWD","11111"));
        autoLoginCheck = preferences.getBoolean("MemberAuto", false);
        cboxCheck.setChecked(autoLoginCheck);
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
                        // 연결에 실패했을 경우 실행되는 메서드입니다.
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



                Log.d("myLog", "accessToken  " + accessToken);
                //AAAAOrQU9DBIXbOIhNTv7FpgDG5KadQGGtIVwmFrtZEyo69xE52Odj70O7+O+aLZsmXvnc5baewQ1x5PdAoS5HyDeHs=
                Log.d("myLog", "refreshToken  " + refreshToken);
                //lipPPw1N9Dqipa84eGU5ipHrKQEAy1s67qYprH3rxPahYFj7tre9Oisq3apu2chY7zmppP88sjL4JL6AFanmvvSxjis7hLaqPisSpsGR50ZxrsOvGpn2FfHVBqr1FUWovkiiIhl
                Log.d("myLog","String.valueOf(expiresAt)  " + String.valueOf(expiresAt));
                //1512008864
                Log.d("myLog", "tokenType  " + tokenType);
                //bearer
                Log.d("myLog", "mOAuthLoginInstance.getState(mContext).toString()  " + mOAuthLoginModule.getState(mContext).toString());

                new RequestApiTask().execute(); //로그인이 성공하면  네이버에 계정값들을 가져온다.

            } else {

                // 로그인이 실패하면 에러 코드와 에러 메시지를 저장
                String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);

                // Toast.makeText(mContext, "errorCode:" + errorCode +

                // ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();

                Toast.makeText(ActivityMemberLogin.this, "로그인이 취소/실패 하였습니다.!",
                        Toast.LENGTH_SHORT).show();
            }
        };
    };

    private void certPhone() {
        if(checkMember() == true) {
            goMainTest();
        } else {
            // 회원이 아니었는데, 가입한 경우 이므로 전화번호 인증 창으로 간다.
            Intent intent = new Intent(ActivityMemberLogin.this, ActivityMemberCertPhone.class);
            startActivityForResult(intent, REQUEST_CODE_PHONE);
        }
    }

    private boolean checkMember() {
        boolean cheeckMember = false;
        // 회원인지 여부 판단후에 회원이면
        //checkMember = true;

        // 회원이 아니면 false로 간다.

        return cheeckMember;
    }

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
            //String url = "https://openapi.naver.com/v1/nid/me";
            String at = mOAuthLoginModule.getAccessToken(mContext);

            // 정보를 넘기고 API대한 정보를 요청한다.
            Pasingversiondata(mOAuthLoginModule.requestApi(mContext, at, url));

            return null;
        }

        protected void onPostExecute(Void content) {

            // infoLoad가 ture일 경우..(모든 정보가 있을 경우)
            if ( checkLoadInfo == true ) {
                Toast.makeText(getBaseContext(), memberVO.getMname()+"님 환영합니다.", Toast.LENGTH_LONG).show();

                certPhone();
                //finish();
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
                boolean lastMatTag = false;

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

            nickname = f_array[0];                  // 닉네임
            enc_id = f_array[1];                    // enc_id
            profile_image = f_array[2];            // 프로필 이미지
            age = f_array[3];                       // 나이대
            id = f_array[5];                        // 고유 id (숫자)

            MemberVO naverMemberVO = new MemberVO(f_array[4], f_array[6], f_array[7], f_array[8], naver);   // 성별, 이름, 이메일, 생일
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

    // 구글 로그인 시, 반환되는 결과값들
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        switch ( requestCode ) {
            case REQUEST_CODE_GOOGLE:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent( data );
                if ( result.isSuccess( ) ) { GoogleSignInAccount acct = result.getSignInAccount( );
                    memberVO.setMemail(acct.getEmail());
                    memberVO.setMid(acct.getEmail());
                    memberVO.setMname(acct.getDisplayName());
                    memberVO.setMsns(google);
                    Toast.makeText(mContext, acct.getDisplayName() + " 님 환영합니다.", Toast.LENGTH_SHORT).show();
                    certPhone();
                }
                break;
            case REQUEST_CODE_PHONE:
                if(resultCode == SH_JOB_OK) {
                    Toast.makeText(mContext, "인증에 성공하였습니다", Toast.LENGTH_SHORT).show();
                    memberVO.setMphone(data.getStringExtra("phone"));
                    Intent intent = new Intent(ActivityMemberLogin.this, ActivityMember.class);
                    startActivity(intent);
                    finish();
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
                } else {
                    //사용자가 거부 했을때
                    Toast.makeText(this, "거부 - 동의해야 사용가능합니다.", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000);
                }
                return;
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("어플을 종료하시겠습니까?");
        alert.setNegativeButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intentData = new Intent();
                setResult(MY_END_CODE, intentData);
                finish();
            }
        });

        alert.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();

    }
}

// 네이버 로그인 실패 원인은 2가지가 잇다.
// 가입 실패랑, 제공 거부.