package home.safe.com.myguarder;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteOpenHelper;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

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

    //intent PopupCycle key code
    public final static int MY_REQUEST_CODE = 1111;
    public final static int MY_REQUEST_CODE_POPUP = 2222;
    public final static String DATA_NAME = "cycle";
    public final static String DATA_NAME_POPUP = "dateList";
    public final static int DEFAULT_NUMBER = 5;

    public int cycleCivilian = 10000;
    public int cycleGuarder = 10000;

    // 시스템으로부터 현재시간(ms) 가져오기
    long first;
    long now;

    ArrayList locationList = new ArrayList<Location>();
    public boolean mapDrawFlag = false;

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
                startActivityForResult(intent,MY_REQUEST_CODE);
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

        /**
         *
         * @author 경창현
         * @version 1.0.0
         * @text 저장된 폴로라인 그리기
         * 시간차를 두니 그리기 완료
         * - 문제사항 : 1. 이상한 곳에 마커가 보였다 없어졌다 한다.
         *              2. 불러온 좌표가 다 표시 되지 않는것 같다.
         * - 원인 : 맵이 그려지기 전에 changeLocation이 실행이 여러번 됨 이후 onMapReady이 실행 후 맵이 그려진다.
         * - 해결방법 : onMapReady에 위치를 불러오는 코드를 사용하여 해결
         * @since 2017-11-30 오후 1:42
         **/

            reDrawPolyline();


    }

    public void showCamera()
    {
        //이동
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                new LatLng(mCurrentLocation.getLatitude(),
//                        mCurrentLocation.getLongitude()), 16));

        //이동 + 모션
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
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
    public void getPermissions()
    {
        // 퍼미션 체크
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            // 퍼미션 없음
            Log.d("getPermissions","in!!!");


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
            Log.d("getPermissions","out!!!");
            mLocationPermissionGranted = true;

            //현재위치 정보 세팅
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            if(mCurrentLocation != null) {
                //처음 시작 라인
                startPL = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                Log.d("startPL", "!!!!");
                Log.d("getLatitude ", "" + mCurrentLocation.getLatitude());
                Log.d("getLongitude ", "" + mCurrentLocation.getLongitude());
            }

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
        getPermissions();

        //콘넥트후 프레그먼트 생성
        initFragment();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("onConnectionFailed","Connect fail");
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

        if(null != googleMap) {
            //위치 저장
            locationList.add(mCurrentLocation);

            printThisLocation();
        }
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

        if(now - first > cycleCivilian )
        {
            Log.d("TimeMillis", "now - first");
            first = now;

            if(true) {
                //이동경로 그리기
                endPL = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());

                drawPolyline(startPL,endPL);

                startPL = endPL;
            }

            showCamera();
        }
    }

    /**
    *
    * @author 경창현
    * @version 1.0.0
    * @text Polyline 그리기
     * param : ( LatLng, LatLng)
     * return : void
    * @since 2017-11-30 오후 2:25
    **/
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


    /**
    *
    * @author 경창현
    * @version 1.0.0
    * @text
     * 1. 마커
     * 2. 이동경로 저장 ok
     * 3. 카메라 이동 모션 ok
     * 4. 이동경로 뿌리기 part ok
     * 5. 지킴이 화면에 피지킴이 위치 뿌리기 5분
     * 6. 설정 화면 - ok - 전송주기 받아오기 ok - 적용 아직 activi
     * 7. gps 중간에 키면 오류 - ok t  null 체크
    * @since 2017-11-28 오후 4:10
    **/

    /**
    *
    * @author 경창현
    * @version 1.0.0
    * @text
     * 1. 마커 개수찾기
     * 2. 저장된 위치정보 출력 고치지 -  리줌이 가장 이상적 ok
     * 3. 설정화면 주기부분 -  activity를 통해 ok -> 데이터 저장까지 완료
    * @since 2017-12-01 오후 3:34
    **/

    /**
    *
    * @author 경창현
    * @version 1.0.0
    * @text
     * 1. 마커 갯수
     * 2. activityPopup 세부 코딩 ok
     * 3. 모드전환 -> 스위치 형식 비활성화 활성화 두개의 버튼  □■ ok -> 엑티비티를 현재창에서 띄울건지, 뒤로가서 띄울건지
     * 4. 지킴이가 위치정보 불러와서 그리는 부분 만들기 ok -> 그리기는 sql에서 데이터 가져와서
    * @since 2017-12-05 오후 3:23
    **/

    /**
    *
    * @author 경창현
    * @version 1.0.0
    * @text
     * 1. 마커 갯수 정하기
     * 2. 객체로 만들어서 생성해보기
     * 3. 지난 위치보기 3개 경로 만들고 지우고 새로 뿌리는지 확인
     * 4. 테스크 설정하고 새로띄움
     * 5. 지킴이 주기 필요없다.
     * 6. 설정 주기 확인
     * 7. 쉐어프리펄런스 -> 이동수단 내용 사용  (public class 생성해서 모든 모듈에서 접근가능하게도 고려)
     * 8. 지킴이 화면에서 피지킴이 목록을 띄워 한명 선택 후 위치 확인 가능하게
    * @since 2017-12-08 오후 3:25
    **/
}
