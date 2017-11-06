package home.safe.com.member;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        tvCertCode = (TextView)findViewById(R.id.tvCertCode);
        etCertCode = (EditText)findViewById(R.id.etCertCode);
        btnCheckCode = (Button)findViewById(R.id.btnCheckCode);

         btnCheckCode.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
    public void setRecvCode(String recvCode){
        this.recvCode = recvCode;
        this.tvCertCode.setText(this.recvCode);
    }

    public String getRecvCode(){
        return recvCode;
    }

    public String getSendCode(){
        //sendCode = (String)etCertCode.getText();
        return sendCode;
    }

}
