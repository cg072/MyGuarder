package home.safe.com.myguarder;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_location_request);

        btnLocationReqOk = (Button)findViewById(R.id.btnLocationReqOk);
        btnLocationReqNo = (Button)findViewById(R.id.btnLocationReqNo);

        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();

        String str;
        for(ActivityManager.RunningAppProcessInfo process : list){
            if(process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                str = process.processName;
                Log.d("LocationRequest",str);
            }
        }

        btnLocationReqOk.setOnClickListener(this);
        btnLocationReqNo.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == btnLocationReqOk.getId())
        {
            getServiceData();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

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
}
