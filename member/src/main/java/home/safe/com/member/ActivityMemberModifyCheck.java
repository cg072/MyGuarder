package home.safe.com.member;

import android.content.Intent;
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

    final static String testPassword = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_modify_check);

        etPWD = (EditText) findViewById(R.id.etPWD);
        btnCheckPWD = (Button) findViewById(R.id.btnCheckPWD);

        btnCheckPWD.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {

                String pwd = etPWD.getText().toString().trim();

                if(testPassword.equals(etPWD.getText().toString().trim())) {
                    Intent intent = new Intent(ActivityMemberModifyCheck.this, ActivityMemberModify.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ActivityMemberModifyCheck.this, "Password를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int recvPWDToServer() {

        int check = 0;

        MemberVO memberVO = new MemberVO();

        String id = "이전 액티비티에서 아이디를 받아서 셋팅";
        String pwd = etPWD.getText().toString().trim();

        memberVO.setMid(id);
        memberVO.setMpwd(pwd);
        ArrayList<MemberVO> resultList = new ArrayList<MemberVO>();

        MemberManager memberManager = new MemberManager(getApplicationContext());

        resultList = memberManager.select(MemberShareWord.TARGET_SERVER, MemberShareWord.TYPE_SELECT_CON, memberVO);

        try {
            if (resultList.get(0).getMid().equals(id) && resultList.get(0).getMpwd().equals(pwd)) {
                check = 1;
            }
        } catch (Exception e) {
            Log.v("MemberModifyCheck", e.getMessage());
        }

        return check;
    }
}
