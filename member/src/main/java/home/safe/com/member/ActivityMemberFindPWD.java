package home.safe.com.member;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivityMemberFindPWD extends AppCompatActivity {

    private EditText etID;
    private EditText etEmail;
    private Button btnCertificationID;
    private Button btnCertificationEmail;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_find_pwd);

        etID = (EditText)findViewById(R.id.etID);
        etEmail = (EditText)findViewById(R.id.etEmail);
        btnCertificationID = (Button)findViewById(R.id.btnCertificationID);
        btnCertificationEmail = (Button)findViewById(R.id.btnCertificationEmail);
        btnSend = (Button)findViewById(R.id.btnSend);

        btnCertificationEmail.setEnabled(false);
        btnSend.setEnabled(false);

        btnSend.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnCertificationEmail.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnSend.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
