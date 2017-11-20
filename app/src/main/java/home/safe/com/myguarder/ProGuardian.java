package home.safe.com.myguarder;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteOpenHelper;
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
                break;
        }

        Toast.makeText(this,txt,Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }

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
        }
        else if(mCurrentLocation != null)
        {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mCurrentLocation.getLatitude(),
                    mCurrentLocation.getLongitude()), 16));
        }
        else
        {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.3, 34.3), 16));
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        updateLocationUI();
    }

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

            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }


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

    public void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);            //위치 요청 간격 ms
        mLocationRequest.setFastestInterval(5000);      //
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
    }
}
