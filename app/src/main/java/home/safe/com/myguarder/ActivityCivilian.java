package home.safe.com.myguarder;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.LocationServices;


public class ActivityCivilian extends ProGuardian implements View.OnClickListener{

    Button btnCivilianLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null)
        {
            mCurrentLocation = savedInstanceState.getParcelable(LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(CAMERA_POSITION);
        }

        setContentView(R.layout.activity_civilian);

        btnCivilianLog = (Button)findViewById(R.id.btnCivilianLog);

        buildGoogleApiClient();
        mGoogleApiClient.connect();

//        initFragment();


        btnCivilianLog.setOnClickListener(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(mGoogleApiClient.isConnected())
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    protected void onResume() {
        if(mGoogleApiClient.isConnected())
        {
            getDeviceLocation();
        }
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        //지난위치보기 팝업
        if(view.getResources() == btnCivilianLog.getResources())
        {
            Intent intent = new Intent(this,ActivityPopup.class);
            startActivity(intent);
        }

    }

}
