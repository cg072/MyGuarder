package home.safe.com.member;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by hotki on 2018-01-19.
 */

public class MemberNaverLogin extends Activity {

    private final static int REQUEST_CODE_LOGIN_SUCCESS = 1;
    private final static int REQUEST_CODE_LOGIN_FAIL = 0;
    private final static int MY_LOGIN_SUCCESS_CODE = 201;
    private final static int ROOT_LOGIN_SUCCESS_CODE = 202;
    private final static String USER_INFO = "data";

    // 네이버 로그인 변수들
    private static String OAUTH_CLIENT_ID = "W3ENfk_vTn1YQaVsG3n_";  // 1)에서 받아온 값들을 넣어좁니다
    private static String OAUTH_CLIENT_SECRET = "q3B35mxCBe";
    private static String OAUTH_CLIENT_NAME = "Cooljj";
    private String naver = "naver";
    private boolean checkLoadInfo = false;

    String accessToken = "";

    String tokenType;

    OAuthLogin mOAuthLoginModule;

    MemberVO memberVO = new MemberVO();
    MemberNaverLogin memberNaverLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 현재 어플리케이션의 life사이클에 해당하는 Context를 mContext에 담는다.
        memberNaverLogin = this;

        // OAuthLogin를 getInstance()를 사용해서 m0AuthLoginModule에 넣는다. [싱글톤패턴]
        mOAuthLoginModule = OAuthLogin.getInstance();

        mOAuthLoginModule.init(getApplicationContext(),                 // 어플리케이션 정보
                OAUTH_CLIENT_ID,            // 네아로에 등록된 접근 아이디
                OAUTH_CLIENT_SECRET,        // 네아로에 등록된 접근 비번
                OAUTH_CLIENT_NAME);         // 네이버 앱의 로그인 화면에 표시할 애플리케이션 이름. 모바일 웹의 로그인 화면을 사용할 때는 서버에 저장된 애플리케이션 이름이 표시됩니다.

        // 클릭시 OAuthLogin의 생성자를 담은 것에서 네로아 액티비티를 시작한다. (인자 : 액티비티, 핸들러)
        mOAuthLoginModule.startOauthLoginActivity(this, mOAuthLoginHandler);
    }

    /*
    *  date     : 2017.11.29
    *  author   : Kim Jong-ha
    *  title    : OAuthLoginHandler 생성
    *  comment  : Naver 로그인 핸들러
    * */
    @SuppressLint("HandlerLeak")
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {

        @Override
        public void run(boolean success) {

            if (success) {
                // String Type
                // 이 어플리케이션에서 네로아에 대한 접근 토큰을 저장
                accessToken = mOAuthLoginModule.getAccessToken(getApplicationContext());

                // 이 어플리케이션에서 네로아에 대한 갱신 토큰을 저장
                String refreshToken = mOAuthLoginModule.getRefreshToken(getApplicationContext());

                // 이 어플리케이션에서 네로아에 대한 접근 토큰 만료기간을 저장
                long expiresAt = mOAuthLoginModule.getExpiresAt(getApplicationContext());

                // 이 어플리케이션에서 네로아에 대한 접근 토큰의 형태를 저장(MAC, Bearer 지원)
                tokenType = mOAuthLoginModule.getTokenType(getApplicationContext());

                new RequestApiTask().execute(); //로그인이 성공하면  네이버에 계정값들을 가져온다.
                // 네이버 로그인 성공시 작성할 메소드
                Intent intentData = new Intent();
                setResult(ROOT_LOGIN_SUCCESS_CODE, intentData);
            } else {

                // 로그인이 실패하면 에러 코드와 에러 메시지를 저장
                String errorCode = mOAuthLoginModule.getLastErrorCode(getApplicationContext()).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(getApplicationContext());

                Toast.makeText(getApplicationContext(), "로그인이 취소/실패 하였습니다.!",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        ;
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
            String at = mOAuthLoginModule.getAccessToken(getApplicationContext());

            // 정보를 넘기고 API대한 정보를 요청한다.
            Pasingversiondata(mOAuthLoginModule.requestApi(getApplicationContext(), at, url));

            return null;
        }

        protected void onPostExecute(Void content) {

            // infoLoad가 ture일 경우..(모든 정보가 있을 경우)
            if ( checkLoadInfo == true ) { // 첫 로그인인지, 아닌지(첫 로그인이면 서버로 insert 후에 폰인증으로감
                Intent intentData = new Intent();
                intentData.putExtra(USER_INFO, memberVO);
                setResult(REQUEST_CODE_LOGIN_SUCCESS, intentData);
                finish();
            } else {                 // infoLoad가 false일 경우..(이름과 이메일을 받아오지 못할 경우를 포함해서, 정보가 없을 경우)
                Toast.makeText(getApplicationContext(),
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
                    if(tag != null) {
                        Log.v("태그"+colIdx, tag) ;}
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
            naverMemberVO.setMid(f_array[6]);
            naverMemberVO.setMgender(f_array[4]);   // 성별
            naverMemberVO.setMname(f_array[7]);     // 이름
            naverMemberVO.setMemail(f_array[6]);    // 이메일
            naverMemberVO.setMbirth(f_array[8]);    // 생일
            naverMemberVO.setMsns(naver);
            naverMemberVO.setMsnsid(separateSNSID(f_array[7]));

            memberVO = naverMemberVO;

            // 로그인이 정상적이라면 f_array[i] != null 이 된다.
            for(int i = 0 ; i < 9 ; i++) {
                Log.v("로그", f_array[i] + " 순서 : " + i);
                if(f_array[i] == null) {
                    checkLoadInfo = false;
                    break;
                } else {
                    checkLoadInfo = true;
                }
            }
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
}
