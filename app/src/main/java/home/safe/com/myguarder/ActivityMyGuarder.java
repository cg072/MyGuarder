package home.safe.com.myguarder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

public class ActivityMyGuarder extends ProGuardian implements View.OnClickListener{

    Button btnGuarderLog;
    Button btnCivilianList;
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
        btnCivilianList = (Button)findViewById(R.id.btnCivilianList);
        btnLocation = (Button)findViewById(R.id.btnLocation);

        buildGoogleApiClient();
        mGoogleApiClient.connect();

        btnGuarderLog.setOnClickListener(this);
        btnCivilianList.setOnClickListener(this);
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
            startActivityForResult(intent, MYGUARDER_REQUEST_POPUP_CODE);
        }
        else if(view.getId() == btnCivilianList.getId())
        {
            Intent intent = new Intent(this,ActivityPopupCivilianList.class);
            startActivityForResult(intent, MYGUARDER_REQUEST_CIVILIAN_LIST_CODE);

        }
        else if(view.getId() == btnLocation.getId())
        {
//            drawPolyline(new LatLng(37.2350000, 127.0620000),new LatLng(37.2353114, 127.0626726));
            civilianLocationRequest();


        }
    }

/**
 *
 * @author 경창현
 * @version 1.0.0
 * @text 피지킴이 위치 요청 메서드 + 위치 그리기
 * @since 2017-12-12 오후 5:17
**/
    private void civilianLocationRequest() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MAIN_REQUEST_SETTING_CODE)
        {
            if(resultCode == RESULT_OK) {
                Log.d("onActivityResult", "MyGuarder - " + MAIN_REQUEST_SETTING_CODE);
                cycleGuarder = data.getIntExtra(DATA_NAME, DEFAULT_NUMBER);
                Log.d("주기 : ", "" + cycleGuarder);
            }
            if(resultCode == MY_LOGOUT_CODE)
            {
                Intent intentData = new Intent();
                setResult(MY_LOGOUT_CODE, intentData);
                finish();
            }
            if(resultCode == MY_CIVILIAN_CODE)
            {
                Intent intentData = new Intent();
                setResult(MY_CIVILIAN_CODE, intentData);
                finish();
            }
            if(resultCode == MY_GUARDER_CODE)
            {
                Intent intentData = new Intent();
                setResult(MY_GUARDER_CODE, intentData);
                finish();
            }
        }
        else if(requestCode == MYGUARDER_REQUEST_POPUP_CODE)
        {
            if(resultCode == RESULT_OK) {
                Log.d("onActivityResult", "MyGuarder - " + MYGUARDER_REQUEST_POPUP_CODE);
                selectPopupList(data.getStringExtra(DATA_NAME_POPUP));
            }
        }
        else if(requestCode == MYGUARDER_REQUEST_CIVILIAN_LIST_CODE)
        {
            if(resultCode == RESULT_OK) {
                Log.d("onActivityResult", "MyGuarder - " + MYGUARDER_REQUEST_CIVILIAN_LIST_CODE);
                data.getStringExtra(DATA_CIVILIAN_NAME);
                // 지킴이가 선택한 피지킴이의 아이디를 가져옴

                //피지킴이의 아이디로 DB에서 위치정보 SELECT해온후 그려준다
                selectCivilianLocation(data.getStringExtra(DATA_CIVILIAN_NAME));
            }
        }
        else if(resultCode == 123)
        {
            Intent intentData = new Intent();
            setResult(MY_MENU_CHANGE_CODE, intentData);
            finish();
        }
    }

    private void selectCivilianLocation(String id) {
        //DB로 위치정보를 가져옴


        //가져온 위치정보를 그려준다.
        Toast.makeText(this,id,Toast.LENGTH_SHORT).show();
        drawPolyline(new LatLng(37.2352916 ,127.0626087), new LatLng(37.2350000,127.0620000), polylinesRequestLocation);
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

        //기존의 지난 위치보기 polyline 삭제
        for(Polyline line : polylinesLastLocation)
        {
            line.remove();
        }
        polylinesLastLocation.clear();

        //선택한 목록의 polyline 그리기
        if("2017.12.05".equals(date))
        {
            Toast.makeText(this,"2017.12.05",Toast.LENGTH_SHORT).show();
            drawPolyline(new LatLng(37.2352916 ,127.0626087), new LatLng(37.2350000,127.0620000), polylinesLastLocation);
            drawPolyline(new LatLng(37.2350000 ,127.0620000), new LatLng(37.2320000,127.0610000), polylinesLastLocation);
            drawPolyline(new LatLng(37.2320000 ,127.0610000), new LatLng(37.2320000,127.0550000), polylinesLastLocation);
        }
        else if("2017.12.04".equals(date))
        {
            Toast.makeText(this,"2017.12.04",Toast.LENGTH_SHORT).show();
            drawPolyline(new LatLng(37.2350000 ,127.0626087), new LatLng(37.2350000,127.0620000), polylinesLastLocation);
        }
        else if("2017.12.03".equals(date))
        {

            Toast.makeText(this,"2017.12.03",Toast.LENGTH_SHORT).show();
            drawPolyline(new LatLng(37.2320000 ,127.0610000), new LatLng(37.0000000,127.0000000), polylinesLastLocation);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        Intent intentData = new Intent();
        setResult(MY_CIVILIAN_CODE, intentData);
        finish();
    }
}
