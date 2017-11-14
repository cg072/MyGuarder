package home.safe.com.myguarder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by kch on 2017-11-06.
 */

/*
 * @author 경창현
 * @version 1.0.0
 * @text 구글 Fragment
 *  fragment에서 함수 불러오기
 *  http://kwangsics.tistory.com/entry/Android-Activity%EC%97%90%EC%84%9C-Fragment-%ED%95%A8%EC%88%98-%ED%98%B8%EC%B6%9C
 * @since 2017-11-14 오전 12:13
 */

public class FragmentMap extends Fragment implements OnMapReadyCallback{

    private MapView mapView = null;

    GoogleMap googleMap;

    private final static int MY_LOCATION_REQUEST_CODE = 99;
    boolean permissionCheck = false;

    GoogleApiClient mGoogleApiClient = null;

    public FragmentMap()
    {
        //required
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView)rootView.findViewById(R.id.map);
        mapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //액티비티가 처음 생성될 때 실행되는 함수

        if(mapView != null)
            mapView.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /*
     * @author 경창현
     * @version 1.0.0
     * @text 맵 처리 부분
     * @since 2017-11-14 오전 12:14
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

        //퍼미션
        this.initPermissions();
        Log.d("퍼미션 권한",String.valueOf(permissionCheck));

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    /*
     * @author 경창현
     * @version 1.0.0
     * @text 퍼미션 작성
     * @since 2017-11-14 오전 12:12
     */
    public void initPermissions()
    {
        Log.d("initPermissions","오는가");
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            permissionCheck = true;
            Log.d("initPermissions","T");
            googleMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_LOCATION_REQUEST_CODE);
            Log.d("initPermissions","F");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                // Permission was denied. Display an error message.
                Toast.makeText(getActivity(), "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
/**
*
* @author 경창현
* @version 1.0.0
* @text
 * 위치
 * http://developer88.tistory.com/31
* @since 2017-11-14 오후 4:18
**/
    protected synchronized  void buildGoogleApiClient()
    {
        if(mGoogleApiClient == null)
        {
//            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                    .enableAutoManage(getActivity(),this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(getActivity().LOCATION_SERVICE)
//                    .build();
        }
    }

}
