package home.safe.com.myguarder;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;


public class ActivityCivilian extends ProGuardian implements View.OnClickListener{

    Button btnCivilianLog;
    Button btnEmergency;

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

        btnCivilianLog = (Button)findViewById(R.id.btnCivilianLog);
        btnEmergency = (Button)findViewById(R.id.btnEmergency);

        buildGoogleApiClient();
        mGoogleApiClient.connect();

//        initFragment();


        btnCivilianLog.setOnClickListener(this);
        btnEmergency.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("onStart","in");
    }

    @Override
    protected void onResume() {
        if(mGoogleApiClient.isConnected())
        {
            getDeviceLocation();
        }
        super.onResume();
        Log.d("onResume","in");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onPause","in");
        if(mGoogleApiClient.isConnected())
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
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

}
