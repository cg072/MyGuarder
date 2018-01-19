package home.safe.com.member;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by hotki on 2017-12-07.
 */

public class ActivityMemberModifyCheck extends AppCompatActivity {

    private final static int SH_JOB_OK = 200;

    EditText etPWD;
    Button btnCheckPWD;

    String id;
    String pwd;
    String sns;
    String snsid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_modify_check);

        // SNS 아이디는 그냥 패스
        if(checkSNS() == 1) {
            moveNextActivity("sns");
        }

        etPWD = (EditText) findViewById(R.id.etPWD);
        btnCheckPWD = (Button) findViewById(R.id.btnCheckPWD);

        btnCheckPWD.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {

                String pwd = etPWD.getText().toString().trim();

                if(recvPWDToServer() == 1) {
                    moveNextActivity("nomal");
                } else {
                    Toast.makeText(ActivityMemberModifyCheck.this, "Password를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int recvPWDToServer() {

        int check = 0;

        MemberVO memberVO = new MemberVO();

        String pwd = etPWD.getText().toString().trim();

        memberVO.setMid(id);
        memberVO.setMpwd(pwd);
        ArrayList<MemberVO> resultList = new ArrayList<MemberVO>();

        MemberManager memberManager = new MemberManager(getApplicationContext());

        resultList = memberManager.select(MemberShareWord.TARGET_SERVER, MemberShareWord.TYPE_SELECT_CON, memberVO);

        if(resultList.size() != 0) {
            if (resultList.get(0).getMid().equals(id) && resultList.get(0).getMpwd().equals(pwd)) {
                check = 1;
            }
        }
        return check;
    }

    private int checkSNS(){
        int check = 0;

        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        id = preferences.getString("MemberID",null);

        MemberVO memberVO = new MemberVO();
        memberVO.setMid(id);

        MemberManager memberManager = new MemberManager(getApplicationContext());

        ArrayList<MemberVO> resultList = memberManager.select(MemberShareWord.TARGET_SERVER, MemberShareWord.TYPE_SELECT_CON, memberVO);

        if(resultList.size() == 1) {
            Log.v("체크sns", "ㅇㅇ?");
            if(resultList.get(0).getMsns() != null) {
                check = 1;
            }
        }

        return check;
    }

    private void moveNextActivity(String type){
        Intent intent = new Intent(ActivityMemberModifyCheck.this, ActivityMemberModify.class);
        switch (type) {
            case "nomal":
                break;
            case "sns":
                intent.putExtra("sns", "sns");
                break;
        }
        intent.putExtra("id", id);
        startActivity(intent);
        finish();
    }
}
