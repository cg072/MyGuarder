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
    }
}
