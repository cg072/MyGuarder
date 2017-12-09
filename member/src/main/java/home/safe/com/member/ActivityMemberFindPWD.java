package home.safe.com.member;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivityMemberFindPWD extends AppCompatActivity {

    private EditText etID;
    private EditText etEmail;
    private Button btnSend;

    private String email = "hotkiss86kjh@gmail.com";
    private String password = "5029kjhkjh";

    private MemberVO memberVO = new MemberVO();

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
                memberVO.setMid(etID.toString().toString().trim());
                memberVO.setMemail(etEmail.getText().toString().trim());

                // 비번 찾기랑 마찬가지로 서버로 보내고 결과값을 받는다.
            }
        });
    }
}
