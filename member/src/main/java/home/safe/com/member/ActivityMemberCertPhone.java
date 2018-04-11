package home.safe.com.member;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMemberCertPhone extends AppCompatActivity implements View.OnClickListener{

    private TextView tvPhone;
    private String myNumber = "";
    private Button btnCert;
    private int SH_JOB_OK = 200;
    private int SH_JOB_FALSE = 400;
    private boolean checkCert = false;
    private boolean checkPermission = false;
    private ActivityMemberCertDialog certDialog;

    private String id;
    private MemberVO memberVO = new MemberVO();

    private String settingCode;

    //kch
    HttpResultListener listener;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_cert_phone);

        Intent intent = getIntent();
        Log.v("로그", "여기오냐");
        id = intent.getStringExtra("id");
        //Log.v("로그", id);

        tvPhone = (TextView) findViewById(R.id.tvPhone);
        btnCert = (Button) findViewById(R.id.btnCert);

        MemberLoadPhoneNumber memberLoadPhoneNumber = new MemberLoadPhoneNumber(this);
        phoneNumber=memberLoadPhoneNumber.getMyPhoneNumber();
        tvPhone.setText(addHyphen(memberLoadPhoneNumber.getMyPhoneNumber()));

        btnCert.setOnClickListener(this);

        listener = new HttpResultListener() {
            @Override
            public void onPost(String result) {
                certDialog.setRecvCode(result.replace("/n",""));
            }
        };
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnCert) {
            recvCodeFromServer();

            certDialog = new ActivityMemberCertDialog(ActivityMemberCertPhone.this);

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
                    if(certDialog.getResultCode())
                    {
                        updatePhone();
                        //Toast.makeText(ActivityMemberCertPhone.this, settingCode + "같아" + certDialog.getSendCode(), Toast.LENGTH_SHORT).show();
                        checkCert = true;
                    }
                    else
                    {
                        Toast.makeText(ActivityMemberCertPhone.this, "전화번호 인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent();
                    intent.putExtra("phone", myNumber);
                    if(checkCert == true) {
                        setResult(SH_JOB_OK, intent);
                    } else {
                        setResult(SH_JOB_FALSE, intent);
                    }
                    finish();
                }
            });
            certDialog.setPhoneNumber(phoneNumber);
            certDialog.show();
        }
    }

    // 서버에 랜덤으로 조합된 인증코드를 요청하고 받은 값을 리턴 (settingCode를 이것으로 해주면됨)
    private String recvCodeFromServer() {

        MemberManager memberManager = new MemberManager(getApplicationContext());

        settingCode = memberManager.requestCode(phoneNumber, listener);



        return settingCode;
    }

    private int updatePhone(){
        Log.v("로그", "폰넘버 업데이트");
        int check = 0;

        MemberVO memberVO = new MemberVO();
        memberVO.setMid(id);
        memberVO.setMphone(myNumber);

        MemberManager memberManager = new MemberManager(getApplicationContext());
        check = memberManager.update(MemberShareWord.TARGET_SERVER, memberVO);

        return check;
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
