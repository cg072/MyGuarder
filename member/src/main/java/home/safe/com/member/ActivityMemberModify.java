package home.safe.com.member;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class ActivityMemberModify extends AppCompatActivity {

    private TextView etID;

    private EditText etPWD;
    private EditText etName;
    private EditText etPhone;
    private EditText etBirth;
    private EditText etEMail;

    private RadioButton rbUndefine;
    private RadioButton rbFemail;
    private RadioButton rbMail;

    private Button btnPWDModify;
    private Button btnPhoneCertification;
    private Button btnModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_modify_check);
    }

}
