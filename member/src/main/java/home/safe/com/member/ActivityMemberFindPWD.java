package home.safe.com.member;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityMemberFindPWD extends AppCompatActivity {

    private EditText etID;
    private EditText etPhone;
    private Button btnFindID;
    private Button btnPhoneCertification;
    private Button btnCheck;

    // 테스트용
    private String code = "1111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_find_pwd);

        etID = (EditText)findViewById(R.id.etID);
        etPhone = (EditText)findViewById(R.id.etPhone);
        btnFindID = (Button)findViewById(R.id.btnFindID);
        btnPhoneCertification = (Button)findViewById(R.id.btnPhoneCertification);
        btnCheck = (Button)findViewById(R.id.btnCheck);



        btnFindID.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnPhoneCertification.setOnClickListener(new Button.OnClickListener() {
            // 나중에 서버로부터 얻은 값을 셋팅하는 것으로 해야함
            ActivityMemberCertDialog certDialog = new ActivityMemberCertDialog(ActivityMemberFindPWD.this);
            @Override
            public void onClick(View view) {
                certDialog.setOnShowListener((new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        certDialog.setRecvCode(code);

                    }
                }));
                certDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //Toast.makeText(ActivityMemberFindPWD.this, certDialog.getRecvCode(), Toast.LENGTH_SHORT).show();
                        if(code.equals((String)certDialog.getRecvCode())){
                            Toast.makeText(ActivityMemberFindPWD.this, code+"같아"+certDialog.getRecvCode(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                certDialog.show();
            }
        });
        btnCheck.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
