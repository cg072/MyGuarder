package home.safe.com.member;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityMemberFindID extends AppCompatActivity {

    private EditText etEmail;
    private Button btnCertificationEmail;
    private EditText etPhone;
    private Button btnCertificationPhone;
    private Button btnSend;

    private String savedEmail;

    // 테스트용
    private String code = "1111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_find_id);

        etEmail = (EditText)findViewById(R.id.etEmail);
        btnCertificationEmail = (Button)findViewById(R.id.btnCertificationEmail);
        etPhone = (EditText)findViewById(R.id.etPhone);
        btnCertificationPhone = (Button)findViewById(R.id.btnCertificationPhone);
        btnSend = (Button)findViewById(R.id.btnSend);

        btnCertificationEmail.setEnabled(true);
        btnCertificationPhone.setEnabled(false);
        btnSend.setEnabled(false);

        btnCertificationEmail.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnCertificationPhone.setOnClickListener(new Button.OnClickListener() {
            ActivityMemberCertDialog certDialog = new ActivityMemberCertDialog(ActivityMemberFindID.this);
            @Override
            public void onClick(View view) {
                certDialog.setOnShowListener((new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        // 서버로부터 받은 값을 셋팅하여 준다. [후에 code에 값을 서버로부터 받은 값으로~!]
                        certDialog.setRecvCode(code);

                    }
                }));
                certDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if(code.equals(certDialog.getSendCode()))
                        {
                            Toast.makeText(ActivityMemberFindID.this, code + "같아" + certDialog.getSendCode(), Toast.LENGTH_SHORT).show();
                            etPhone.setEnabled(false);
                            btnCertificationPhone.setEnabled(false);
                            btnSend.setEnabled(true);
                        }
                        else
                        {
                            Toast.makeText(ActivityMemberFindID.this, "전화번호 인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                certDialog.show();
            }
        });
        btnSend.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

}
