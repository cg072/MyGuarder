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
                GMailSender sender = new GMailSender(email, password);

                try {
                    sender.sendMail("타이틀",      // 제목
                            "내용",          // 내용
                            "hotkiss86kjh@gmail.com",                 // 보내는이
                            "joakim1120@naver.com" // 받는이
                    );
                    Log.v("이메일", "보내짐");
                } catch (Exception e) {
                    Log.v("이메일", "안보내짐");
                }
            }
        });


    }
}
