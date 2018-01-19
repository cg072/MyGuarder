package home.safe.com.member;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityMemberFindPWD extends AppCompatActivity {

    private EditText etID;
    private EditText etEmail;
    private Button btnSend;

    private String email = "hotkiss86kjh@gmail.com";
    private String password = "5029kjhkjh";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_find_pwd);

        etID = (EditText)findViewById(R.id.etID);
        etEmail = (EditText)findViewById(R.id.etEmail);
        btnSend = (Button)findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (sendDataToServer()) {
                    case 0 :
                        Toast.makeText(ActivityMemberFindPWD.this, "해당 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case 1 :
                        Toast.makeText(ActivityMemberFindPWD.this, "해당 e-Mail로 발송하였습니다", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private int sendDataToServer() {
        int check = 0;

        String id = etID.toString().toString().trim();
        String email = etEmail.getText().toString().trim();

        MemberVO memberVO = new MemberVO();
        memberVO.setMid(id);
        memberVO.setMemail(email);

        MemberCheck memberCheck = new MemberCheck();

        return memberCheck.checkExistence(memberVO, getApplicationContext());
    }
}
