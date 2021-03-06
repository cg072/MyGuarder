package home.safe.com.myguarder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hotki on 2017-11-01.
 */

public class ProGuardian extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    //퍼미션
    public static final int MY_PERMISSIONS_CODE = 99;
    public static final int MY_PERMISSION_FINE_SMS_SEND = 100;
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

    //지난 위치보기 현재위치 출력 막는 frag
    boolean printLocationFrag = false;

    //polyline

    //polyline 지난위치보기
    List<Polyline> polylinesLastLocation = new ArrayList<>();
    //polyline 현재이동경로
    List<Polyline> polylinesLocation = new ArrayList<>();
    //polyline 요청위치
    List<Polyline> polylinesRequestLocation = new ArrayList<>();
    //polyline 피지킴이 위치
    List<Polyline> polylinesCivilianLocation = new ArrayList<>();

    boolean isStartPLLocation = false;  //startPL 값이 들어왔는지 확인
    private LatLng startPL = new LatLng(0, 0);        //polyline 시작점
    private LatLng endPL = new LatLng(0, 0);        //polyline 끝점

    //marker
    //marker 지난위치마커
    List<Marker> markersLastLocation = new ArrayList<>();
    //marker 현재위치마커
    List<Marker> markersLocation = new ArrayList<>();
    //marker 요청위치마커
    List<Marker> markersRequestLocation = new ArrayList<>();

    //Location
    // Location 현재위치
    ArrayList locationList = new ArrayList<Location>();

    //전역변수
    ActivityLocal local;

    //intent key code
    public final static int MAIN_REQUEST_SETTING_CODE = 1111;
    public final static int MYGUARDER_REQUEST_POPUP_CODE = 2222;
    public final static int MYGUARDER_REQUEST_CIVILIAN_LIST_CODE = 3333;
    public final static int MY_END_CODE = 100;
    public final static int MAIN_REQUEST_MEMBER_CODE = 200;
    public final static int MY_LOGIN_SUCCESS_CODE = 201;
    public final static int ROOT_LOGIN_SUCCESS_CODE = 202;
    public final static int MY_LOGOUT_CODE = 300;
    public final static int MY_MENU_CHANGE_CODE = 400;
    public final static int MY_CIVILIAN_CODE = 601;
    public final static int MY_GUARDER_CODE = 602;
    public final static String DATA_NAME = "cycle";
    public final static String DATA_NAME_POPUP = "dateList";
    public final static String DATA_CIVILIAN_NAME = "civilianName";
    public final static int DEFAULT_NUMBER = 5;

    public int cycleCivilian = 10000;
    public int cycleGuarder = 10000;

    // 시스템으로부터 현재시간(ms) 가져오기
    long first;
    long now;

    //MVC
    LocationManage locationManage;
    MyGuarderVO vo;

    //db
    private SQLiteOpenHelper sqLiteOpenHelper;
    public static boolean loginCheck;

    //State
    boolean RequestState;

    String guarderID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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

        switch (id)
        {
            case R.id.action_setting:
                Intent intent = new Intent(this,ActivitySetting.class);
                startActivityForResult(intent, MAIN_REQUEST_SETTING_CODE);
                break;
        }

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
     * @text Back 버튼
     * @since 2017-12-24 오후 7:37
    **/
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("어플을 종료하시겠습니까?");
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intentData = new Intent();
                setResult(MY_END_CODE, intentData);
                finish();
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();

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
            Log.d("onMapReady","mCameraPosition not null");
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        }
        else if(mCurrentLocation != null)
        {
            Log.d("onMapReady","mCurrentLocation not null");
            showCamera();
        }
        else
        {
            Log.d("onMapReady","null");
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.566535, 126.97796919999996), 16));
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);

            //GPS 상태 ON 요청
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("위치정보를 켜주세요");

            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            AlertDialog alertDialog = alert.create();
            alertDialog.show();
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
    public boolean getPermissions()
    {
        RequstPermissionChecker permissionChecker = new RequstPermissionChecker(this);
        permissionChecker.lacksPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE

        );

        return permissionChecker.getPermission();
    }

    @SuppressLint("MissingPermission")
    public void settingLocation()
    {
        mLocationPermissionGranted = true;

        //현재위치 정보 세팅
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        updateLocationUI();
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

        if(requestCode == MY_PERMISSIONS_CODE)
        {

            for(int i = 0; i < grantResults.length; i++ ) {
                if (grantResults.length > 0 && grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //권한 거부한 퍼미션 화면에 필요하다고 토스트 띄우기
                    Toast.makeText(this,permissions[i]+"가 꼭 필요합니다.",Toast.LENGTH_LONG).show();
                }
            }
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
        Log.d("updateLocationUI", " ------------");
        if(googleMap == null)
            return;
        if (mLocationPermissionGranted)
        {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    for(Polyline line : polylinesLastLocation)
                    {
                        line.remove();
                    }
                    polylinesLastLocation.clear();

                    for(Polyline line : polylinesRequestLocation)
                    {
                        line.remove();
                    }
                    polylinesRequestLocation.clear();

                    for(Polyline line : polylinesCivilianLocation)
                    {
                        line.remove();
                    }
                    polylinesCivilianLocation.clear();

                    reDrawPolyline();

                    printLocationFrag = false;

                    return false;
                }
            });
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
        if(getPermissions())
        {
            settingLocation();
        }

        //콘넥트후 프레그먼트 생성
        initFragment();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("onConnectionSuspended","Suspended");
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
 * 고려해야할 부분 - 지킴이 화면에 위치정보를 보내는 방법 2개 중 택 1 해야함
 * 1. locationList를 사용하여 위치정보를 보내서 폴리라인을 추가한다.
 * 2. polylinesLocation를 사용하여 폴리라인을 그려준다.
* @since 2017-11-22 오후 4:29
**/
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;


        if(!isStartPLLocation)
        {
            //문제가 된 부분
            if(mCurrentLocation != null) {
                //처음 시작 라인
                startPL = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                Log.d("startPL", "!!!!");
                Log.d("getLatitude ", "" + mCurrentLocation.getLatitude());
                Log.d("getLongitude ", "" + mCurrentLocation.getLongitude());
            }

            isStartPLLocation = true;
        }

        requestCivilianLocation();

        now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
        Log.d("TimeMillisNow",String.valueOf(now));

        if(null != googleMap && null != guarderID) {
            //위치 저장
            locationList.add(mCurrentLocation); // 1. 위치를 저장해서 폴리라인을 추가하는 방법
            //polylinesLocation 치환 가능   ->  2. polylinesLocation을 보내서 폴리라인을 그려주는 방법(위치정보까지 들어있음 getPoint())


            vo = new MyGuarderVO(
                    String.valueOf(mCurrentLocation.getLatitude()),
                    String.valueOf(mCurrentLocation.getLongitude()),
                    dateFormat.format(date),
                    timeFormat.format(date),
                    guarderID);

            Log.d("guarderID",guarderID);

            //insert
            int res = locationManage.controller.insert(vo.locationDataToContentValues());
            Log.d("ProGuardian", "controller.insert - "+res);


            drawThisLocation();
        }
        else
        {
            Log.d("onLocationChanged","googleMap,guarderID null");
        }
    }

    /**
     *
     * @author 경창현
     * @version 1.0.0
     * @text 위치요청 다이얼로그 호출
     * @since 2018-01-23 오후 4:12
    **/
    public void requestCivilianLocation() {
        // 위치가 켜져있고 위치를 받은 상태
        loadRequestStateData();
        Log.d("onMapReady","RequestState - "+RequestState);

        if(RequestState) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("위치요청을 수락하시겠습니까?");

            alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //서버로 현재위치 피지킴이,지킴이 아이디 전송

                    dialogInterface.cancel();
                }
            });

            AlertDialog alertDialog = alert.create();
            alertDialog.show();

            //위치요청 처리 후 요청플레그 false로 초기화
            saveRequestStateData(false);
        }
    }

    /**
    * 
    * @author 경창현
    * @version 1.0.0
    * @text 현재위치 위도,경도 출력
    * @since 2017-11-22 오후 4:23
    **/
    public void drawThisLocation()
    {
        Log.d("drawThisLocation",""+mCurrentLocation.getLatitude()+" , "+mCurrentLocation.getLongitude());

        if(now - first > cycleCivilian )
        {

            Log.d("TimeMillis", "now - first");
            first = now;

            //웹과 연동
            sendLocation();
            HttpResultListener listener = new HttpResultListener() {
                @Override
                public void onPost(String result) {
                    Log.d("HttpResultListener"," onPost - "+result);
                }
            };

            NetworkTask networkTask = new NetworkTask(this, listener);
            networkTask.strUrl = NetworkTask.HTTP_IP_PORT_PACKAGE_STUDY;
//            networkTask.params = "map.do?method=getMapList&lid=kch";
            networkTask.params = NetworkTask.CONTROLLER_MAP_DO + NetworkTask.METHOD_ADD_MAP + "&llat=" +vo.getLlat()+"&llong="+vo.getLlong()+"&ltime="+vo.getLtime()+"&lid="+vo.getLid();
            networkTask.execute();



            //이동경로 그리기
            endPL = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());

            drawPolyline(startPL, endPL, polylinesLocation);
            drawMarker(startPL, markersLocation);

            // 지난위치보기 클릭시 polyline, marker 안보임
            if(printLocationFrag) {
                mapVisibleFalse();
            }

            startPL = endPL;


            showCamera();
        }
    }
/**
 *
 * @author 경창현
 * @version 1.0.0
 * @text 현재위치 맵 안보이기
 * @since 2018-01-07 오후 4:39
**/
    public void mapVisibleFalse() {
        Log.d("visibleTest","false");

        for(Polyline line : polylinesLocation)
        {
            line.setVisible(false);
        }
        for(Marker marker : markersLocation)
        {
            marker.setVisible(false);
        }
    }

    /**
     *
     * @author 경창현
     * @version 1.0.0
     * @text 주기별로 위치정보 서버에 전송
     * @since 2017-12-12 오후 5:15
    **/
    private void sendLocation() {
        //locationList.add(mCurrentLocation);//이거를 이용하여 위치를 받아와 서버에 전송
        //전송하는 메서드
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
    public void drawPolyline(LatLng start, LatLng end,List<Polyline> polylines)
    {
        if(polylines.equals(polylinesLocation)) {
            local = (ActivityLocal) getApplication();
            polylines = local.getPolylinesLocation();
        }

        PolylineOptions polylineOptions = new PolylineOptions().add(start).add(end).width(15).color(Color.RED).geodesic(true);
        polylines.add(googleMap.addPolyline(polylineOptions));

        if(polylines.equals(polylinesLocation)) {
            local.setPolylinesLocation(polylines);
            local.getPolylinesLocationSize();
        }
    }

    /**
     *
     * @author 경창현
     * @version 1.0.0
     * @text Polyline 그리기
     * param : ( LatLng )
     * return : void
     * @since 2017-12-10 오후 8:25
     **/
    public void drawMarker(LatLng start, List<Marker> markers)
    {
        if(markers.equals(markersLocation)) {
            local = (ActivityLocal) getApplication();
            markers = local.getMarkersLocation();
        }

        MarkerOptions markerOptions = new MarkerOptions().position(start).draggable(true);
        //icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_point)
        markers.add(googleMap.addMarker(markerOptions));

        if(markers.equals(markersLocation)) {
            local.setMarkersLocation(markers);
            local.getMarkersLocationSize();
        }
    }

/**
 * 
 * @author 경창현
 * @version 1.0.0
 * @text 다시 polyline 그리기
 * @since 2017-12-11 오후 4:32
**/
    public void reDrawPolyline()
    {
        Log.d("ProGuardian","reDrawPolyline - polylinesLocation : "+polylinesLocation.size());

        List<LatLng> points;

        //맵 클리어 해주고
        googleMap.clear();

        int ind = 0;
        //폴리라인 그리기
        for(Polyline line : polylinesLocation)
        {

            line.setVisible(true);
            polylinesLocation.set(ind++,
            googleMap.addPolyline(new PolylineOptions().add(line.getPoints().get(0),line.getPoints().get(1)).width(15).color(Color.RED).geodesic(true))
            );
            Log.d("polylineVB",""+line.getPoints().get(0));
            Log.d("polylineVB",""+line.getPoints().get(1));
            Log.d("polylineVB",""+line.isVisible());

        }

        ind = 0;
        for(Marker mark : markersLocation)
        {
            mark.setVisible(true);
            markersLocation.set(ind++,
            googleMap.addMarker(new MarkerOptions().position(mark.getPosition()).draggable(true))
            );
        }
    }


    public void saveMapStateData(boolean isMapState )
    {
        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("MapState",isMapState);
        editor.commit();
    }

    //RequestState
    private void saveRequestStateData(boolean isRequestState )
    {
        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("RequestState",isRequestState);
        editor.commit();
    }

    public void loadRequestStateData()
    {
        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        RequestState = preferences.getBoolean("RequestState",false);
    }

    public void loadID()
    {
        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        guarderID = preferences.getString("MemberID","-");
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
     * 1. 마커 갯수 정하기 - 마커 객체화로 처리 ok
     * 2. 객체로 만들어서 생성해보기 - ok
     * 3. 지난 위치보기 3개 경로 만들고 지우고 새로 뿌리는지 확인 ok
     * 4. 테스크 설정하고 새로띄움 - > 문제사항 모드선택시 최상위로 띄운 메인 아래있는 테스크 제거가 안되고, 플레그를 넣으면 세팅목록 등에 문제가 있음,
     *                                  항상 메인이 세팅목록 뒤에 있지가 않다.)
     *
     * 5. 지킴이 주기, 지킴이 관리 필요없다. ok
     * 6. 설정 주기 확인 - ?
     * 7. 쉐어프리펄런스 -> 이동수단 내용 사용  (public class 생성해서 모든 모듈에서 접근가능하게도 고려)  ok
     * 8. 지킴이 화면에서 피지킴이 목록을 띄워 한명 선택 후 위치 확인 가능하게 -> 문제사항
     * 9. 지난 위치보기 난후 현재 위치보기로 이전해야하는데 버튼을 만들지 안만들지(지난 위치보기와 현재 위치가 겹침) + 현재위치보기 호출하는것 중지해야함 ok
    * @since 2017-12-08 오후 3:25
    **/

    /**
     * @author 경창현
     * @version 1.0.0
     * @text 추가해야할 사항
     * 1. 테스크 설정 만들기 ok
     * 2. 지킴이 지난경로 그리는지 확인 ok
     * 3. 피지킴이, 지킴이 둘다 서버에 현재 위치 송신 ok
     * 4. 지킴이 화면에서 피지킴이 목록을 띄워 한명 선택 후 위치 확인 가능하게 -> 목록 생성 및 구분 완료 ->  위치를 뿌려주는 부분 해야함 ok
     * @since 2017-12-11 오후 5:41
    **/

    /**
     *
     * @author 경창현
     * @version 1.0.0
     * @text
     * 1. 지킴이가 피지킴이 경로 뿌렸을때 현재위치 누르면 지우기 해야함 ok
     * 2. 버튼 백 눌렀을때 버튼 호환이 안됨(해결사항 : Setting을 피니쉬시키는 방향으로  ok
     * 3. 서버를 찾아보자
     * https://blog.naver.com/xiaoyu00
     * 223.195.109.70
     * kyungch123
     * 1515
     * @since 2017-12-15 오후 3:36
    **/

    /**
     *
     * @author 경창현
     * @version 1.0.0
     * @text  피지킴이 모드 불켜지는거 확인
     * @since 2017-12-19 오후 4:50
    **/

    /**
     *
     * @author 경창현
     * @version 1.0.0
     * @text
     * 1. 로그인화면을 메인에 올려서 시작순서대로 정렬하기 ok
     * 2. 종하형과 연동 ok
     * http://kylblog.tistory.com/21
     * - 로그아웃할때 문제점 해결방법 2가지 중 택 1
     *  1. 메인엑티비티에서 메서드 만들어서 list로 콘텍스트 넣고 관리 x
     *  2. 로그아웃할때 메인 종료하고 다시 띄우기 x
     *  3. 메인 엑티비티에서 주요 엑티비티 관리 o
     *  쌓인 엑티비티 다 지우기 i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
     *
     *  -> 엑티비티리절트 통합해야 하는지 검토해야함
     * @since 2017-12-22 오후 3:54
    **/

    /**
     *
     * @author 경창현
     * @version 1.0.0
     * @text
     * 1. 지킴이 모드일때 백 버튼 눌렀을때 피지킴이 화면으로 이동  ok
     * 2. 컨트롤러, DB헬퍼, VO 생성 ok
     * 3. 개개인 컨트롤러,DB헬퍼, VO를 통해서 만들고, 나중에 PGchang로 연동 ok
     * 4. 컨트롤러 IProGuardianController로 구현, DB헬퍼 ProGuardianDBHelper상속받아서 구현 ok
     * VO는 ProGuardianVO상속받아서 구현 ok
     * 5. 피지킴이 화면에 현재위치 그리기
     * 6. 글씨체
     * @since 2017-12-26 오후 4:09
    **/
    
    /**
     * 
     * @author 경창현
     * @version 1.0.0
     * @text
     * 1. 순번 오토인크리즈 ok
     * 2. 현재 핸드폰 DB에 저장 ok
     * 3. 현재 이동 위치 DB에 저장 다른 날짜는 삭제, 2틀치 저장 후 다음날에는 전전날꺼 삭제 -> ok
     * 4. 현재 이동 위치 서버에 전송
     * 5. 지난 위치 보기 - 최근 1~2일치는 핸드폰 DB에서 세부위치 출력 ok
     *                   - 3일치 부터는 서버에서 가져와서 대강적인 위치 출력
     *                   - 위치는 날짜별로 표시 ok
     * 6. 순번이 0이면 제외하게 코드상 구현  ( 0이 기준)
     * 7. 나머지 속성 기본값 0인것은 코드에 VO에 기본값 0
     * @since 2018-01-02 오후 2:20
    **/

    /**
     *
     * @author 경창현
     * @version 1.0.0
     * @text
     * 1. 지난위치 볼때 현재위치 그리지 않기  -> 구조 변경 고려 573라인  ok
     * -> 문제사항 -  지난 위치보기 주기와 현재 위치보기 주기가 다름 (상관은 없을거 같다. 세부위치랑 서버 위치가 다르기때문)
     * 2. CouplerMVC에다가 insert,select등 수정 ok  -> 리스트 VO로 바꿔줌
     * 3. 위치요청 다이얼로그 (신호줬을때 다이얼로그가 뜨는지) -> 1. 서버를 통해 받아오기 2. 앱이 안켜져있을때 행동
     * 4. 피지킴이 목록
     * 5. 긴급버튼 -> 전화인가
     * @since 2018-01-05 오후 4:29
    **/

    /**
     *
     * @author 경창현
     * @version 1.0.0
     * @text
     * 1. CouplerMVC에다가 insert,select등 수정 ok  -> 리스트 VO로 바꿔줌 ok
     * 2. 위치요청 다이얼로그 -> 서비스에서 리슨만 하고 있고 요청오면 다이얼로그 띄워서 확인하면 서버에서 위치만 보냄 ok - 서버 없이 다이얼로그까지
     * -> http://twinw.tistory.com/49
     * http://twinw.tistory.com/50
     * 3. 긴급버튼 문자 보냄 ok
     * @since 2018-01-09 오후 3:33
    **/
    
    /**
     * 
     * @author 경창현
     * @version 1.0.0
     * @text
     * 1. 방법 -> SharedPreferences로 로그인, 위치 상태 저장 -> 시작할때 확인 및 전송
     * 1) 위치정보가 켜져있을때 -> 확인 눌렀을때 위치가 켜져있는지 확인 -> 켜져있으면 위치정보 전송
     * 2) 위치정보가 꺼져있을때 -> 확인 눌렀을때 위치가 꺼져있는지 확인 -> 커져있으면 위치정보 켜달라는 요청 후 화면 이동
     * -> 위치 온 -> 화면 돌아왔을때 체크후 전송
     * 3) 취소일 경우
     * - 로그인, 위치 정보 허용 체크, 위치 요청 체크
     *
     * 2. 피지킴이 목록  -> 지킴이 SELETE를 사용해서 목록 가져와서 피지킴이 이름 뿌리기
     * 3. 서버 개발
     * @since 2018-01-16 오후 2:50
    **/

    /**
     *
     * @author 경창현
     * @version 1.0.0
     * @text
     * 1. 맵에서 요청 마지막 체크 후 다이얼로그 확인 누르면 요청정보 보냄  ok
     * 1) Notification으로 띄우기 ok
     * 2) 메인에서 요청 확인 및 State 세팅 ok
     * 2. 다이얼로그를 노티비케이션으로 변경 중 ok
     * 3. 로그인 화면 로그인 요청 다이얼로그 안보냄
     * -> 무조건 로그인화면으로 갈것이고 피지킴이 화면이 떠야 위치를 받음
     * 그렇기 때문에 로그인 화면으로 이동
     * @since 2018-01-19 오후 3:18
    **/

    /**
     *
     * @author 경창현
     * @version 1.0.0
     * @text
     * 1. 중간에 위치켜면 작동문제 ok
     * 2. 카메라 이후 요청이 되게 ok
     * 3. 피지킴이 목록 불러오기 -> 보류
     * 4. 권한 모아서 실행하기 -> ok
     * 5. 웹공부
     * @since 2018-01-23 오후 2:13
    **/
    
}
