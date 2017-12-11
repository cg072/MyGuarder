package home.safe.com.myguarder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class ActivityMyGuarder extends ProGuardian implements View.OnClickListener{

    Button btnGuarderLog;
    Button btnLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null)
        {
            mCurrentLocation = savedInstanceState.getParcelable(LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(CAMERA_POSITION);
        }

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

//        if(null != line) {
//            line.remove();
//        }
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
            startActivityForResult(intent,MY_REQUEST_CODE_POPUP);
        }
        else if(view.getId() == btnLocation.getId())
        {
//            drawPolyline(new LatLng(37.2350000, 127.0620000),new LatLng(37.2353114, 127.0626726));
            civilianLocationRequest();
        }
    }


    private void civilianLocationRequest() {
        //피지킴이 위치 요청 메서스 + 위치 그리기
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            if(requestCode == MY_REQUEST_CODE)
            {
                Log.d("onActivityResult", "MyGuarder - " +MY_REQUEST_CODE);
                cycleGuarder = data.getIntExtra(DATA_NAME, DEFAULT_NUMBER);
                Log.d("주기 : ", "" +cycleGuarder);
            }
            if(requestCode == MY_REQUEST_CODE_POPUP)
            {
                Log.d("onActivityResult", "MyGuarder - " +MY_REQUEST_CODE_POPUP);
                selectPopupList(data.getStringExtra(DATA_NAME_POPUP));
            }
        }
    }

    /**
     *
     * @author 경창현
     * @version 1.0.0
     * @text 날짜를 가지고 sql로 리스트를 불러와서 맵에 뿌려주는 메서드
     * reference String date
     * @since 2017-12-08 오후 1:52
     **/
    private void selectPopupList(String date)
    {
        //sql로 해당 날짜의 리스트 가져오기

        //리스트에 있는 위도 경도로 폴리라인 그리기
        // onMapReady()안거친다.
        //리스트를 drawPolyline()으로 그려주기만 하면 된다.
        drawPolyline(new LatLng(37.2352916 ,127.0626087), new LatLng(37.2350000,127.0620000), polylinesLastLocation);
        drawPolyline(new LatLng(37.2350000 ,127.0620000), new LatLng(37.2320000,127.0610000), polylinesLastLocation);
        drawPolyline(new LatLng(37.2320000 ,127.0610000), new LatLng(37.2320000,127.0550000), polylinesLastLocation);

        Toast.makeText(this,"selectPopupList",Toast.LENGTH_SHORT).show();
    }
}
