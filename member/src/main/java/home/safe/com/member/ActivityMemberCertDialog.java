package home.safe.com.member;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
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

    ActivityMemberCertDialog certDialog = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_member_cert_dialog);

        tvCertCode = (TextView) findViewById(R.id.tvCertCode);
        etCertCode = (EditText) findViewById(R.id.etCertCode);
        btnCheckCode = (Button) findViewById(R.id.btnCheckCode);

        //키보드 보이게 하는 부분
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

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
}
