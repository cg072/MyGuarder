package home.safe.com.member;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityMemberLogin extends AppCompatActivity implements View.OnClickListener{

    // 변수 설정
    private EditText memberLogin_et_id;
    private EditText memberLogin_et_pwd;
    private CheckBox memberLogin_cbox_keep;
    private Button memberLogin_btn_login;
    private TextView memberLogin_tv_signup;
    private TextView memberLogin_tv_findpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_login);

        // 변수에 아이디 연동
        memberLogin_et_id = (EditText)findViewById(R.id.login_et_id);
        memberLogin_et_pwd = (EditText)findViewById(R.id.login_et_pwd);
        memberLogin_cbox_keep = (CheckBox)findViewById(R.id.login_cbox_check);
        memberLogin_btn_login = (Button)findViewById(R.id.login_btn_login);
        memberLogin_tv_signup = (TextView)findViewById(R.id.login_tv_signup);
        memberLogin_tv_findpwd = (TextView)findViewById(R.id.login_tv_findpwd);


    }

    @Override
    public void onClick(View view) {

    }
}
