package home.safe.com.member;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by hotki on 2017-12-07.
 */

public class ActivityMemberModifyCheck extends AppCompatActivity {

    private final static int SH_JOB_OK = 200;

    EditText etPWD;
    Button btnCheckPWD;

    String id;
    String pwd;
    String sns;
    String snsid;

    //kch
    NetworkTask networkTask;
    HttpResultListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_modify_check);

        // SNS 아이디는 그냥 패스
        if(checkSNS() == 1) {
            moveNextActivity("sns");
        }

        id = getId();

        etPWD = (EditText) findViewById(R.id.etPWD);
        btnCheckPWD = (Button) findViewById(R.id.btnCheckPWD);

        listener = new HttpResultListener() {
            @Override
            public void onPost(String result) {
                if("1".equals(result.replace("/n",""))) {
                    moveNextActivity("nomal");
                } else {
                    Toast.makeText(ActivityMemberModifyCheck.this, "Password를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnCheckPWD.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        String pwd = etPWD.getText().toString().trim();

                        networkTask = new NetworkTask(getApplicationContext(), listener);
                        networkTask.strUrl = NetworkTask.HTTP_IP_PORT_PACKAGE_STUDY;
                        networkTask.params=NetworkTask.CONTROLLER_MEMBER_DO + NetworkTask.METHOD_LOGIN_MEMBER + "&mid="+id+"&mpwd="+pwd;
                        networkTask.execute();
            }
        });

    }


    private int checkSNS(){
        int check = 0;
        String memberSNSID;

        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        memberSNSID = preferences.getString("MemberSNSID","empty");

        if(!"empty".equals(memberSNSID))
            check = 1;

        return check;
    }

    private String getId() {
        String id;
        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        id = preferences.getString("MemberID","empty");
        return "empty".equals(id) ? preferences.getString("MemberSNSID","empty") : id;
    }

    private void moveNextActivity(String type){
        Intent intent = new Intent(ActivityMemberModifyCheck.this, ActivityMemberModify.class);
        switch (type) {
            case "nomal":
                break;
            case "sns":
                intent.putExtra("sns", "sns");
                break;
        }
        intent.putExtra("id", id);
        startActivity(intent);
        finish();
    }
}
