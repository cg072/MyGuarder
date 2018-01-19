package home.safe.com.myguarder;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class ActivityPopupLocationRequest extends Activity implements View.OnClickListener{

    Button btnLocationReqOk;
    Button btnLocationReqNo;

    boolean activityState;
    boolean mapState;
    boolean loginState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_location_request);

        btnLocationReqOk = (Button)findViewById(R.id.btnLocationReqOk);
        btnLocationReqNo = (Button)findViewById(R.id.btnLocationReqNo);

        loadData();


        Log.d("LocationRequest()"," "+activityState);
        Log.d("LocationRequest()"," "+mapState);
        Log.d("LocationRequest()"," "+loginState);


        btnLocationReqOk.setOnClickListener(this);
        btnLocationReqNo.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == btnLocationReqOk.getId())
        {

            getServiceData();

            if(!activityState) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            else
            {
                //로그인, 엑티비티, 맵 체크 후
                //위치 요청 수락 및 정보 보내기
            }

            finish();
        }
        else if(view.getId() == btnLocationReqNo.getId())
        {
            finish();
        }

    }

    public  void getServiceData()
    {
        Intent intent = getIntent();
        String str = intent.getStringExtra("service");
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    private void loadData()
    {
        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        activityState = preferences.getBoolean("ActivityState",false);
        mapState = preferences.getBoolean("MapState", false);
        loginState = preferences.getBoolean("loginState", false);
    }
}
