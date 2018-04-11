package home.safe.com.myguarder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;

import home.safe.com.guarder.GuarderManager;
import home.safe.com.guarder.GuarderVO;


public class ActivityMyGuarder extends ProGuardian implements View.OnClickListener{

//    Button btnGuarderLog;
    Button btnCivilianList;
    Button btnLocation;

    TextView tvJikimNameThisGuarder;
    TextView tvTransNameThisGuarder;
    TextView tvMemoThisGuarder;


    GuarderManager guarderManager;
    //SELECT Guarder
    String guarderID;
    ArrayList<GuarderVO> list;

    NetworkTask networkTask;
    HttpResultListener listener;
    HttpResultListener locationListener;
    HttpResultListener transListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ActivityMyGuarder","onCreate");

        local = (ActivityLocal) getApplication();
        polylinesLocation = local.getPolylinesLocation();
        markersLocation = local.getMarkersLocation();
        local.getPolylinesLocationSize();
        local.getMarkersLocationSize();

        loadID();

        if(savedInstanceState != null)
        {
            mCurrentLocation = savedInstanceState.getParcelable(LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(CAMERA_POSITION);
        }
        setContentView(R.layout.activity_myguarder);

        first = System.currentTimeMillis();

        //로그인시 DB생성 및 연결
        locationManage = new LocationManage(getApplicationContext());
        guarderManager = new GuarderManager(getApplicationContext());

//        btnGuarderLog = (Button)findViewById(R.id.btnGuarderLog);
        btnCivilianList = (Button)findViewById(R.id.btnCivilianList);
        btnLocation = (Button)findViewById(R.id.btnLocation);
        tvJikimNameThisGuarder = (TextView)findViewById(R.id.tvJikimNameThisGuarder);
        tvTransNameThisGuarder = (TextView)findViewById(R.id.tvTransNameThisGuarder);
        tvMemoThisGuarder = (TextView)findViewById(R.id.tvMemoThisGuarder);

        buildGoogleApiClient();
        mGoogleApiClient.connect();

//        btnGuarderLog.setOnClickListener(this);
        btnCivilianList.setOnClickListener(this);
        btnLocation.setOnClickListener(this);

        listener = new HttpResultListener() {
            @Override
            public void onPost(String result) {

                Log.d("ActivityMyGuarder","listener - "+result.replace("/n",""));

                ArrayList<GuarderVO> dateList = new ArrayList<>();

                if(!"401".equals(result.replace("/n",""))) {
                    String[] str = (result.replace("/n", "")).split("&");

                    for (String s : str) {
                        String[] cul = s.split(":");

                        GuarderVO vo = new GuarderVO();
                        vo.setGseq(0);
                        vo.setGstate(Integer.parseInt(cul[1]));
                        vo.setGmcname(cul[2]);
                        vo.setGmcphone(cul[3]);
                        vo.setGmid(guarderID);
                        vo.setGmcid(cul[0]);
                        vo.setGregday("");

                        Log.d("kch", vo.getGmcid() + " - " + vo.getGstate() + " - " + vo.getGmcname() + " - " + vo.getGmcphone());

                        dateList.add(vo);
                    }
                }

                Log.d("ActivityMyGuarder","listener - size  "+dateList.size());

                //피지킴이 팝업
                Intent intent = new Intent(getApplicationContext(),ActivityPopupCivilianList.class);
                intent.putParcelableArrayListExtra("CivilianList",dateList);
                startActivityForResult(intent, MYGUARDER_REQUEST_CIVILIAN_LIST_CODE);
            }
        };

        transListener = new HttpResultListener() {
            @Override
            public void onPost(String result) {
                if(null!=result && !"401".equals(result.replace("/n",""))) {
                    String[] str = (result.replace("/n", "")).split("&");

                    for (String s : str) {
                        String[] cul = s.split("/");

                        if(cul.length>0) {
                            Log.d("getTransInfo", cul[0] + " - " + cul[1] + " - " + cul[2]);
                            tvJikimNameThisGuarder.setText(cul[2]);
                            tvTransNameThisGuarder.setText(cul[0]);
                            tvMemoThisGuarder.setText(cul[1]);
                        }
                    }

                }
            }
        };

        locationListener = new HttpResultListener() {
            @Override
            public void onPost(String result) {
                Log.d("ActivityMyGuarder","listener - "+result.replace("/n",""));

                ArrayList<MyGuarderVO> dateList = new ArrayList<>();

                if(!"401".equals(result.replace("/n",""))) {
                    String[] str = (result.replace("/n", "")).split("&");

                    for (String s : str) {
                        String[] cul = s.split("/");

                        MyGuarderVO vo = new MyGuarderVO();
                        vo.setLseq(Integer.parseInt(cul[0]));
                        vo.setLlat(cul[1]);
                        vo.setLlong(cul[2]);
                        vo.setLday(cul[3]);
                        vo.setLtime(cul[4]);
                        vo.setLid(cul[5]);

                        Log.d("kch",  vo.getLseq()+ " - " + vo.getLlat() + " - " + vo.getLlong() + " - " + vo.getLday() + " - " + vo.getLid());

                        dateList.add(vo);
                    }

                    for(Polyline line : polylinesCivilianLocation)
                    {
                        line.remove();
                    }
                    polylinesCivilianLocation.clear();

                    for(int i=1; i <dateList.size();i++)
                    {
                        drawPolyline(new LatLng(Double.parseDouble(dateList.get(i-1).getLlat()) ,Double.parseDouble(dateList.get(i-1).getLlong())),
                                new LatLng(Double.parseDouble(dateList.get(i).getLlat()) ,Double.parseDouble(dateList.get(i).getLlong())),
                                polylinesCivilianLocation);
                    }

                    networkTask = new NetworkTask(getApplicationContext(), transListener);
                    networkTask.strUrl = NetworkTask.HTTP_IP_PORT_PACKAGE_STUDY;
                    networkTask.params= NetworkTask.CONTROLLER_TRANS_DO + NetworkTask.METHOD_GET_TRANS_INFO + "&rmid=" +dateList.get(0).getLid()+"&rday="+dateList.get(0).getLday();
                    networkTask.execute();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ActivityMyGuarder","onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("ActivityMyGuarder","onRestart");
    }

    @Override
    protected void onResume() {
        if(mGoogleApiClient.isConnected())
        {
            Log.d("onResume","isConnected");
            if(getPermissions())
            {
                settingLocation();
            }
        }
        super.onResume();
        Log.d("ActivityMyGuarder","onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ActivityMyGuarder","onPause");
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
        Log.d("ActivityMyGuarder","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManage.closeDB();

        Log.d("ActivityMyGuarder","onDestroy");

    }

    @Override
    public void onClick(View view) {
        //지난위치보기 팝업
//        if(view.getId() == btnGuarderLog.getId())
//        {
//            Intent intent = new Intent(this,ActivityPopup.class);
//            startActivityForResult(intent, MYGUARDER_REQUEST_POPUP_CODE);
//        }
        if(view.getId() == btnCivilianList.getId())
        {
            loadData();

            //피지킴이 목록 불러오기
            networkTask = new NetworkTask(this, listener);
            networkTask.strUrl = NetworkTask.HTTP_IP_PORT_PACKAGE_STUDY;
            networkTask.params= NetworkTask.CONTROLLER_GUARDER_DO + NetworkTask.METHOD_GET_CIVILIAN_LIST + "&gmid=" +guarderID;
            networkTask.execute();

        }
        else if(view.getId() == btnLocation.getId())
        {
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
        Toast.makeText(this,"위치요청!!",Toast.LENGTH_SHORT).show();
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
        //서버에서 위치정보를 가져옴

        networkTask = new NetworkTask(this, locationListener);
        networkTask.strUrl = NetworkTask.HTTP_IP_PORT_PACKAGE_STUDY;
        networkTask.params= NetworkTask.CONTROLLER_MAP_DO + NetworkTask.METHOD_GET_MAP_LIST + "&lid=" +id;
        networkTask.execute();

        mapVisibleFalse();
        printLocationFrag = true;

    }


    /**
     *
     * @author 경창현
     * @version 1.0.0
     * @text 날짜를 가지고 sql로 리스트를 불러와서 맵에 뿌려주는 메서드
     * reference String date
     * 사용안함 -> 지난위치보기 필요없음
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

    private void loadData()
    {
        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        guarderID = preferences.getString("MemberID","-");
    }
}
