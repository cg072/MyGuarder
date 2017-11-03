package home.safe.com.member;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;


public class ActivityMemberSignup extends AppCompatActivity {

    private EditText etID;
    private EditText etPWD;
    private EditText etName;
    private EditText etPhone;
    private EditText etBirth;
    private EditText etEMail;
    private CheckBox cboxKeep;
    private RadioButton rbUndefine;
    private RadioButton rbFemail;
    private RadioButton rbMail;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_signup);
    }
}
