package home.safe.com.member;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by hotki on 2017-12-07.
 */

public class ActivityMemberModifyCheck extends AppCompatActivity {

    private final static int SH_JOB_OK = 200;

    EditText etPWD;
    Button btnCheckPWD;

    final static String password = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_modify_check);

        etPWD = (EditText) findViewById(R.id.etPWD);
        btnCheckPWD = (Button) findViewById(R.id.btnCheckPWD);

        btnCheckPWD.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {

                String pwd = etPWD.getText().toString().trim();

                if(password.equals(etPWD.getText().toString().trim())) {
                    Intent intent = new Intent(ActivityMemberModifyCheck.this, ActivityMemberModify.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ActivityMemberModifyCheck.this, "Password를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
