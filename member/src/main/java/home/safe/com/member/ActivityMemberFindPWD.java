package home.safe.com.member;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                switch (sendData()) {
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

    private int sendData() {
        int check = 0;

        MemberVO memberVO = new MemberVO();
        memberVO.setMid(etID.toString().toString().trim());
        memberVO.setMemail(etEmail.getText().toString().trim());

        return check;
    }
}
