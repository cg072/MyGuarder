package home.safe.com.myguarder;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hotki on 2017-11-01.
 */

public class ProGuardian extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    //퍼미션
    final static int MY_PERMISSIONS_FINE_LOCATION = 99;
    boolean mLocationPermissionGranted = false;

    //프레그먼트
    MapFragment mMapFragment;

    //맵 객체
    GoogleMap googleMap;

    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mCurrentLocation;
    CameraPosition mCameraPosition;

    public static final String CAMERA_POSITION = "camera_position";
    public static final String LOCATION = "location";

    //polyline
    Polyline line;

    private LatLng startPL = new LatLng(0, 0);        //polyline 시작점
    private LatLng endPL = new LatLng(0, 0);        //polyline 끝점
    //marker
    Marker point;

    // 시스템으로부터 현재시간(ms) 가져오기
    long first;
    long now;

    ArrayList locationList = new ArrayList<Location>();
    public boolean outFlag = false;

    //db
    private SQLiteOpenHelper sqLiteOpenHelper;
    public static boolean loginCheck;


    public SQLiteOpenHelper getSqLiteOpenHelper() {
        return  sqLiteOpenHelper;
    }

    public void sendMessage(ContentValues contentValues) {
    }

    public ContentValues recvMessage() {
        ContentValues contentValues = new ContentValues();
        return contentValues;
    }

    public void sendEmergency(ContentValues contentValues) {

    }

    public boolean getCert(ContentValues contentValues) {
        boolean check = false;

        return check;
    }

    /**
     *
     * @author 경창현
     * @version 1.0.0
     * @text Menu 올리기 - action_bar 추가
     * @since 2017-11-08 오후 5:16
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);

        return true;
    }

    /**
     *
     * @author 경창현
     * @version 1.0.0
     * @text Menu 클릭시 처리 부분
     * @since 2017-11-08 오후 5:17
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        String txt = null;

        switch (id)
        {
            case R.id.action_setting:
                txt = "action_setting";
                Intent intent = new Intent(this,ActivitySetting.class);
                startActivity(intent);
                break;
        }

        Toast.makeText(this,txt,Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(googleMap != null) {
            outState.putParcelable(CAMERA_POSITION, googleMap.getCameraPosition());
            outState.putParcelable(LOCATION, mCurrentLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
    * 
    * @author 경창현
    * @version 1.0.0
    * @text fragment 객체 생성 및 container에 올리기, getMapAsync 콜백 지정
    * @since 2017-11-22 오후 4:24
    **/
    public void initFragment()
    {
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.my_container, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if(mCameraPosition !=  null)
        {
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
            Log.d("onMapReady","mCameraPosition not null");
        }
        else if(mCurrentLocation != null)
        {
            showCamera();
            Log.d("onMapReady","mCurrentLocation not null");
        }
        else
        {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.566535, 126.97796919999996), 16));
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            Log.d("onMapReady","null");
        }

        updateLocationUI();
    }

    public void showCamera()
    {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(mCurrentLocation.getLatitude(),
                        mCurrentLocation.getLongitude()), 16));
    }

    /**
    * 
    * @author 경창현
    * @version 1.0.0
    * @text GoogleApiClient 생성
    * @since 2017-11-22 오후 4:25
    **/
    public synchronized void buildGoogleApiClient()
    {
        if(mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        createLocationRequest();
    }

    /**
    * 
    * @author 경창현
    * @version 1.0.0
    * @text 퍼미션 체크
    * @since 2017-11-22 오후 4:25
    **/
    public void getDeviceLocation()
    {
        // 퍼미션 체크
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            // 퍼미션 없음
            Log.d("getDeviceLocation","in!!!");

//            Log.d("mCurrentLocation",mCurrentLocation.toString());

            //ActivityCompat.shouldShowRequestPermissionRationale 재요청인지 확인
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_FINE_LOCATION);
            }
            else{   //처음일 경우 퍼미션 요청
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_FINE_LOCATION);
            }


        }
        else
        {
            //퍼미션 있음
            Log.d("getDeviceLocation","out!!!");
            mLocationPermissionGranted = true;

            //현재위치 정보 세팅
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            //처음 시작 라인
            startPL = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            Log.d("startPL","!!!!");
            Log.d("getLatitude ",""+mCurrentLocation.getLatitude());
            Log.d("getLongitude ",""+mCurrentLocation.getLongitude());

            updateLocationUI();
        }
    }

/**
* 
* @author 경창현
* @version 1.0.0
* @text 퍼미션 체크 결과 처리
* @since 2017-11-22 오후 4:25
**/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        mLocationPermissionGranted = false;

        switch (requestCode)
        {
            case MY_PERMISSIONS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    mLocationPermissionGranted = true;

                } else {
                    // permission denied
                }
                break;
            }
            default:
                break;
        }
        updateLocationUI();
    }

    /**
    * 
    * @author 경창현
    * @version 1.0.0
    * @text 맵 표시 설정
    * @since 2017-11-22 오후 4:25
    **/
    @SuppressWarnings("MissingPermission")
    public void updateLocationUI()
    {
        if(googleMap == null)
            return;
        if (mLocationPermissionGranted)
        {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        else
        {
            googleMap.setMyLocationEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    /**
    * 
    * @author 경창현
    * @version 1.0.0
    * @text mLocationRequest 위치 정보 생성 및 요청 시간,범위 설정
    * @since 2017-11-22 오후 4:26
    **/
    public void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);            //위치 요청 간격 ms
        mLocationRequest.setFastestInterval(5000);      //
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }
/**
* 
* @author 경창현
* @version 1.0.0
* @text mGoogleApiClient를 connect했을때 연결 됬을때 실행
* @since 2017-11-22 오후 4:26
**/
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("onConnected", " Connected");

        //권한
        getDeviceLocation();

        //콘넥트후 프레그먼트 생성
        initFragment();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


/**
*
* @author 경창현
* @version 1.0.0
* @text createLocationRequest에서 설정한 시간마다 위치정보를 불러옴
* @since 2017-11-22 오후 4:29
**/
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;

        //위치 저장
        locationList.add(mCurrentLocation);

        printThisLocation();
    }

    /**
    * 
    * @author 경창현
    * @version 1.0.0
    * @text 현재위치 위도,경도 출력
    * @since 2017-11-22 오후 4:23
    **/
    public void printThisLocation()
    {
        Log.d("printThisLocation",""+mCurrentLocation.getLatitude()+" , "+mCurrentLocation.getLongitude());

        now = System.currentTimeMillis();

        Log.d("TimeMillisNow",String.valueOf(now));

        //printLocationList();


        /**
        *
        * @author 경창현
        * @version 1.0.0
        * @text 폴리라인이 그려지지 않음
         * 리스트 저장은 됨
         * 이유를 모르겠다
        * @since 2017-11-29 오후 9:14
        **/
        if(outFlag)
        {
            Log.d("reDrawPolyline","outFlag - "+outFlag);
            reDrawPolyline();
            outFlag = false;
        }


        if(now - first>10000 )
        {
            Log.d("TimeMillis", "now - first");
            first = now;

            if(true) {
                //이동경로 그리기
                endPL = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());

                drawPolyline(startPL,endPL);

                startPL = endPL;
            }
//            showCamera();
        }
    }

    public void drawPolyline(LatLng start, LatLng end)
    {
        PolylineOptions polylineOptions = new PolylineOptions().add(start).add(end).width(15).color(Color.RED).geodesic(true);
        line = googleMap.addPolyline(polylineOptions);

        MarkerOptions markerOptions = new MarkerOptions().position(start).draggable(true);
        //icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_point)
        point = googleMap.addMarker(markerOptions);
    }

    public void reDrawPolyline()
    {

        Location locationS;
        Location locationE;

        Log.d("reDrawPolyline"," "+locationList.size());


        //폴로라인 다시 그리기
        for(int i = 0;i<locationList.size()-1;i++)
        {
            Log.d("reDrawPolyline", "for "+i);
            locationS = ((Location)locationList.get(i));
            locationE = ((Location)locationList.get(i+1));
            Log.d("reDrawPolyline", "locationS"+i+" "+locationS.getLatitude());
            Log.d("reDrawPolyline", "locationS"+i+" "+locationS.getLongitude());
            Log.d("reDrawPolyline", "locationE"+i+" "+locationE.getLatitude());
            Log.d("reDrawPolyline", "locationE"+i+" "+locationE.getLongitude());
            LatLng a = new LatLng(locationS.getLatitude(),locationS.getLongitude());
            LatLng b = new LatLng(locationE.getLatitude(),locationE.getLongitude());
            drawPolyline(a ,b);
        }
    }

    public void printLocationList()
    {
        for(int i = 0;i<locationList.size();i++)
        {
            Log.d("count",""+i);
            Log.d("printLocation ",""+((Location)locationList.get(i)).getLatitude());
            Log.d("printLocation ",""+((Location)locationList.get(i)).getLongitude());
        }
    }

    /**
    *
    * @author 경창현
    * @version 1.0.0
    * @text
     * 1. 마커
     * 2. 이동경로 저장 -
     * 3. 카메라 이동 모션 -
     * 4. 이동경로 뿌리기 -
     * 5. 지킴이 화면에 피지킴이 위치 뿌리기 5분
     * 6. 설정 화면 -
     * 7. gps 중간에 키면 오류 -
    * @since 2017-11-28 오후 4:10
    **/
}
