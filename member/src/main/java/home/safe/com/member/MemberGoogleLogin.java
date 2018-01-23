package home.safe.com.member;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by hotki on 2018-01-21.
 */

public class MemberGoogleLogin extends AppCompatActivity{

    // 구글 로그인 변수들
    private static final int REQUEST_CODE_LOGIN_FAIL = 0;
    private static final int REQUEST_CODE_LOGIN_SUCCESS = 1;
    private GoogleApiClient mGoogleApiClient;
    private String google = "google";
    private final static String USER_INFO = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setGoogleLogin();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent( mGoogleApiClient );
        startActivityForResult( signInIntent, REQUEST_CODE_LOGIN_SUCCESS);
    }

    private void setGoogleLogin(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN )
                // 필요한 항목이 있으면 아래에 추가
                .requestEmail( )
                .requestProfile( )
                .build( );

        mGoogleApiClient = new GoogleApiClient.Builder( getApplicationContext() )
                .enableAutoManage( this, new GoogleApiClient.OnConnectionFailedListener( ) {
                    @Override public void onConnectionFailed( @NonNull ConnectionResult connectionResult )
                    {
                        // 연결에 실패했을 경우 실행되는 메소드
                    }
                })
                // 필요한 api가 있으면 아래에 추가
                .addApi( Auth.GOOGLE_SIGN_IN_API, gso )
                .build( );
    }

    // 구글 로그인 반환코드
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        switch (requestCode) {
            // 구글 로그인이 성공하였을 경우의 requestCode
            case REQUEST_CODE_LOGIN_SUCCESS:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    GoogleSignInAccount acct = result.getSignInAccount();
                    // 구글 로그인 시, 반환되는 결과값들(email, name(이름 or 별명))
                    MemberVO googleMemberVO = new MemberVO();
                    googleMemberVO.setMid(acct.getEmail());
                    googleMemberVO.setMname(acct.getDisplayName());
                    googleMemberVO.setMsns(google);
                    googleMemberVO.setMsnsid(separateSNSID(acct.getEmail()));

                    Intent intentData = new Intent();
                    intentData.putExtra(USER_INFO, googleMemberVO);
                    setResult(REQUEST_CODE_LOGIN_SUCCESS, intentData);
                }

                finish();

                break;
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
