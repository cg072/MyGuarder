package home.safe.com.member;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityMemberFindID extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPhone;
    private Button btnSend;

    // 테스트용
    private String code = "1111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_find_id);

        etEmail = (EditText)findViewById(R.id.etEmail);
        etPhone = (EditText)findViewById(R.id.etPhone);
        btnSend = (Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (sendDataToServer()) {
                    case 0 :
                        Toast.makeText(ActivityMemberFindID.this, "해당 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case 1 :
                        Toast.makeText(ActivityMemberFindID.this, "해당 e-Mail로 발송하였습니다", Toast.LENGTH_SHORT).show();
                        break;
                }
                // 서버로 보내고 이메일이 발송되으면 발송되었다고, 오류가 났으면 났다고 함
            }
        });

    }

    private int sendDataToServer() {
        int check = 0;

        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemail(email);
        memberVO.setMphone(phone);

        MemberCheck memberCheck = new MemberCheck();

        return memberCheck.checkExistence(memberVO, getApplicationContext());
    }

}
