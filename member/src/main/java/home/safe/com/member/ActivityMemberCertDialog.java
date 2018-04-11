package home.safe.com.member;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityMemberCertDialog extends Dialog {

    Context context;

    public ActivityMemberCertDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    private TextView tvCertCode;
    private EditText etCertCode;
    private Button btnCheckCode;

    private String recvCode;
    private String sendCode;

    ActivityMemberCertDialog certDialog = this;

    //kch
    NetworkTask networkTask;
    HttpResultListener listener;
    boolean resultCode;
    String phoneNumber;

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

        listener = new HttpResultListener() {
            @Override
            public void onPost(String result) {
                if(null!=result && "1".equals(result.replace("/n","")))
                    resultCode=true;
                else
                    resultCode=false;

                dismiss();
            }
        };

        btnCheckCode.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmCode(phoneNumber,etCertCode.getText().toString(),listener);
            }
        });
    }

    public void confirmCode(String phoneNumber,String code,HttpResultListener listener) {

        networkTask = new NetworkTask(context,listener);
        networkTask.strUrl = NetworkTask.HTTP_IP_PORT_PACKAGE_STUDY;
        networkTask.params= NetworkTask.CONTROLLER_SECURE_DO + NetworkTask.METHOD_CONFIRM_KEY + "&mphone=" +phoneNumber+"&securekey="+code;
        networkTask.execute();
    }

    public boolean getResultCode() {
        return resultCode;
    }
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    // 서버로 부터 받은 코드를 TextView에 셋팅
    public void setRecvCode(String recvCode) {
        this.recvCode = recvCode;
        this.tvCertCode.setText(this.recvCode);
    }

    public String getRecvCode() {
        return recvCode;
    }

    // 입력한 코드값이 서버로부터 받은 값과 일치하는지 여부를 판단한다.
    // 지금은 sendCode지만, 변수명 교체요망
    public String getSendCode() {
        sendCode = etCertCode.getText().toString();
        return sendCode;
    }
}
