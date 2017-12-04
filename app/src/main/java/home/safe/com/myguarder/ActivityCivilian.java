package home.safe.com.myguarder;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;


public class ActivityCivilian extends ProGuardian implements View.OnClickListener{

    Button btnCivilianLog;
    Button btnEmergency;

    TextView tvTransNameThisCivilian;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate","in");

        if(savedInstanceState != null)
        {
            mCurrentLocation = savedInstanceState.getParcelable(LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(CAMERA_POSITION);
        }

        setContentView(R.layout.activity_civilian);

        first = System.currentTimeMillis();

        btnCivilianLog = (Button)findViewById(R.id.btnCivilianLog);
        btnEmergency = (Button)findViewById(R.id.btnEmergency);
        tvTransNameThisCivilian = (TextView)findViewById(R.id.tvTransNameThisCivilian);

        buildGoogleApiClient();
        mGoogleApiClient.connect();


        btnCivilianLog.setOnClickListener(this);
        btnEmergency.setOnClickListener(this);
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.d("onStart","in");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("onRestart","in");
    }

    @Override
    protected void onResume() {

        loadData();
        tvTransNameThisCivilian.setText(""+cycleCivilian);
        if(mGoogleApiClient.isConnected())
        {
            Log.d("onResume","isConnected");
            getPermissions();
        }
        super.onResume();
        Log.d("onResume","in");
//        super.reDrawPolyline();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onPause","in");
        if(mGoogleApiClient.isConnected())
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        if(null != line) {
            line.remove();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("onStop","in");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy","in");
    }

    @Override
    public void onClick(View view) {
        //지난위치보기 팝업
        if(view.getId() == btnCivilianLog.getId())
        {
            Intent intent = new Intent(this,ActivityPopup.class);
            startActivity(intent);
        }
        else if(view.getId() == btnEmergency.getId())
        {
            Toast.makeText(this,"긴급버튼",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            if(requestCode == MY_REQUEST_CODE)
            {
                cycleCivilian = data.getIntExtra(DATA_NAME, DEFAULT_NUMBER)*60000;
                Log.d("주기 : ", "" +cycleCivilian);
                saveData();
            }
        }
    }

    /**
    *
    * @author 경창현
    * @version 1.0.0
    * @text 외부에 데이터 저장
    * @since 2017-12-03 오후 4:29
    **/
    private void saveData()
    {
        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("cycleCivilian",cycleCivilian);
        editor.commit();
    }

    private void loadData()
    {
        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        cycleCivilian = preferences.getInt("cycleCivilian", 10000);
    }

}
