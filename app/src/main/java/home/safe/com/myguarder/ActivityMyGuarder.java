package home.safe.com.myguarder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class ActivityMyGuarder extends ProGuardian implements View.OnClickListener{

    Button btnGuarderLog;
    Button btnLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if(savedInstanceState != null)
//        {
//            mCurrentLocation = savedInstanceState.getParcelable(LOCATION);
//            mCameraPosition = savedInstanceState.getParcelable(CAMERA_POSITION);
//        }

        setContentView(R.layout.activity_myguarder);

        first = System.currentTimeMillis();

        btnGuarderLog = (Button)findViewById(R.id.btnGuarderLog);
        btnLocation = (Button)findViewById(R.id.btnLocation);

        buildGoogleApiClient();
        mGoogleApiClient.connect();

        btnGuarderLog.setOnClickListener(this);
        btnLocation.setOnClickListener(this);
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
        if(mGoogleApiClient.isConnected())
        {
            Log.d("onResume","isConnected");
            getPermissions();
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
        if(view.getId() == btnGuarderLog.getId())
        {
            Intent intent = new Intent(this,ActivityPopup.class);
            startActivity(intent);
        }
        else if(view.getId() == btnLocation.getId())
        {
            drawPolyline(new LatLng(37.2353127, 127.0626751),new LatLng(37.2353114, 127.0626726));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            if(requestCode == MY_REQUEST_CODE)
            {
                cycleGuarder = data.getIntExtra(DATA_NAME, DEFAULT_NUMBER);
                Log.d("주기 : ", "" +cycleGuarder);
            }
        }
    }
}
