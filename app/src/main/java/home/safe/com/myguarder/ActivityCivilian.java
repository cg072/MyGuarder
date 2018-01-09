package home.safe.com.myguarder;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ActivityCivilian extends ProGuardian implements View.OnClickListener{

    Button btnCivilianLog;
    Button btnEmergency;

    TextView tvJikimNameThisCivilian;
    TextView tvTransNameThisCivilian;
    TextView tvMemoThisCivilian;

    private String myGuarderCol[] = {"lseq","llat","llong","lday","ltime","lid"};

    //SELECT ALL
    ArrayList<MyGuarderVO> list;
    //Location LL
    ArrayList<LatLng> LastLocationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate","in");

        if(savedInstanceState != null)
        {
            mCurrentLocation = savedInstanceState.getParcelable(LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(CAMERA_POSITION);
        }

        setContentView(R.layout.activity_civilian);

        first = System.currentTimeMillis();

        //로그인시 DB생성 및 연결
        locationManage = new LocationManage(getApplicationContext());

        //어플 시작시 2일전 DB 데이터 삭제
        Date date = new Date(first);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strFormat = dateFormat.format(date);

        vo = new MyGuarderVO(0,strFormat);

        int res = locationManage.controller.remove(vo.resetDataToContentValues());
        Log.d("ActivityCivilian", "controller.remove - "+res);


        btnCivilianLog = (Button)findViewById(R.id.btnCivilianLog);
        btnEmergency = (Button)findViewById(R.id.btnEmergency);
        tvJikimNameThisCivilian = (TextView)findViewById(R.id.tvJikimNameThisCivilian);
        tvTransNameThisCivilian = (TextView)findViewById(R.id.tvTransNameThisCivilian);
        tvMemoThisCivilian = (TextView)findViewById(R.id.tvMemoThisCivilian);

        buildGoogleApiClient();
        mGoogleApiClient.connect();


        btnCivilianLog.setOnClickListener(this);
        btnEmergency.setOnClickListener(this);
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

        loadData();
//        tvTransNameThisCivilian.setText(""+cycleCivilian);//전송주기 표시
        if(mGoogleApiClient.isConnected())
        {
            Log.d("onResume","isConnected");
            getPermissions();
        }
        super.onResume();
        Log.d("onResume","in");
//        super.reDrawPolyline();
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
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View view) {
        //지난위치보기 팝업
        if(view.getId() == btnCivilianLog.getId())
        {
            //지난 위치 보기 목록 가져오기
//            vo = new MyGuarderVO();

            //LocationManage ( ContentValues를 사용형태로 바꿔야하는가)
            list = locationManage.search(new ContentValues());
            Log.d("MainActivity", "controller.search - "+list.size());

            ArrayList<CharSequence> dateList = new ArrayList<>();

            String compactBox = "";

            for(MyGuarderVO fList : list)
            {
                if(!compactBox.equals(fList.getLday()))
                {
                    dateList.add(fList.getLday());

                    compactBox = fList.getLday();
                }

            }

            //지난 위치 보기 띄우기
            Intent intent = new Intent(this,ActivityPopup.class);
            intent.putCharSequenceArrayListExtra("dataList",dateList);
            startActivityForResult(intent, MYGUARDER_REQUEST_POPUP_CODE);
        }
        else if(view.getId() == btnEmergency.getId())
        {
            Toast.makeText(this,"긴급버튼",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == MAIN_REQUEST_SETTING_CODE)
        {
            if(resultCode == RESULT_OK) {
                Log.d("onActivityResult", "Civilian - " + MAIN_REQUEST_SETTING_CODE);
                cycleCivilian = data.getIntExtra(DATA_NAME, DEFAULT_NUMBER) * 60000;
                Log.d("주기 : ", "" + cycleCivilian);

                saveData();
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
        if(requestCode == MYGUARDER_REQUEST_POPUP_CODE)
        {
            if(resultCode == RESULT_OK) {
                Log.d("onActivityResult", "Civilian - " + MYGUARDER_REQUEST_POPUP_CODE);
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
        mapVisibleFalse();

        //기존의 지난 위치보기 polyline 삭제
        for(Polyline line : polylinesLastLocation)
        {
            line.remove();
        }
        polylinesLastLocation.clear();

        //지난 위치 가져와서 그리기
        Toast.makeText(this,""+date,Toast.LENGTH_SHORT).show();
        LastLocationList = new ArrayList<>();

        for(MyGuarderVO fList : list)
        {
            if(date.equals(fList.getLday()))
            {
                Log.d("asd",""+fList.getLlat());
                LastLocationList.add(new LatLng(Double.parseDouble(fList.getLlat()) ,Double.parseDouble(fList.getLlong())));
            }
        }


        for(int i=1; i <LastLocationList.size();i++)
        {
            drawPolyline(LastLocationList.get(i-1), LastLocationList.get(i), polylinesLastLocation);
        }

        printLocationFrag = true;

    }


    /**
    *
    * @author 경창현
    * @version 1.0.0
    * @text 외부에 데이터 저장
    * @since 2017-12-03 오후 4:29
    **/
    private void saveData()
    {
        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("cycleCivilian",cycleCivilian);
        editor.commit();
    }

    private void loadData()
    {
        SharedPreferences preferences = getSharedPreferences("MyGuarder", Activity.MODE_PRIVATE);
        tvTransNameThisCivilian.setText(preferences.getString("TransName","택시(기본값)"));
        tvMemoThisCivilian.setText(preferences.getString("TransMemo","기본값"));
        cycleCivilian = preferences.getInt("cycleCivilian", 10000);
    }

}
