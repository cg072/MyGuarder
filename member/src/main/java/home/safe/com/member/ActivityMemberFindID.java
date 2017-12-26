package home.safe.com.member;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivityMemberFindID extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPhone;
    private Button btnSend;

    private MemberVO memberVO = new MemberVO();

    private String savedEmail;

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
                memberVO.setMemail(etEmail.getText().toString().trim());
                memberVO.setMphone(etPhone.getText().toString().trim());

                // 서버로 보내고 이메일이 발송되으면 발송되었다고, 오류가 났으면 났다고 함
            }
        });

    }

}
