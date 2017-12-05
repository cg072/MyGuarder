package home.safe.com.member;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityMemberCertDialog extends Dialog {

    public ActivityMemberCertDialog(@NonNull Context context) {
        super(context);
    }

    private TextView tvCertCode;
    private EditText etCertCode;
    private Button btnCheckCode;

    private String recvCode;
    private String sendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_member_cert_dialog);

        tvCertCode = (TextView) findViewById(R.id.tvCertCode);
        etCertCode = (EditText) findViewById(R.id.etCertCode);
        btnCheckCode = (Button) findViewById(R.id.btnCheckCode);

        btnCheckCode.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setRecvCode(String recvCode) {
        this.recvCode = recvCode;
        this.tvCertCode.setText(this.recvCode);
    }

    public String getRecvCode() {
        return recvCode;
    }

    public String getSendCode() {
        sendCode = etCertCode.getText().toString();
        return sendCode;
    }


    // 아래와 같은 방식으로 해당 다이얼로그를 불러온다.
/*
        // 나중에 서버로부터 얻은 값을 셋팅하는 것으로 해야함
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
        }); */
}
