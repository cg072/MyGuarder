package home.safe.com.myguarder;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import java.util.ArrayList;

/*
 * @author 경창현
 * @version 1.0.0
 * @text  추가해야할일
 *        1. 뷰 부분 부드럽게 수정 30%
 *        2. 지난위치보기 내부 구현
 *        3. 설정 버튼
 *        4. 설정 엑티비티
 *        5. 퍼미션 요청 메시지 안뜨는거 해결해야함
 *        6. 현재 위치 표시 부터 시작
 * @since 2017-11-15 오전 12:13
 */



public class MainActivity extends ProGuardian implements IProGuardian {


    boolean permissionCheck = false;

    private final static int MY_LOCATION_REQUEST_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        this.initPermissions();

        Log.d("퍼미션 권한",String.valueOf(permissionCheck));

        /*
         * @author 경창현
         * @version 1.0.0
         * @text ActivityGuarder 띄움
         * @since 2017-11-06 오후 11:47
         */
//        Intent intent = new Intent(this, ActivityGuarder.class);
//        startActivity(intent);

        /*
         * @author 경창현
         * @version 1.0.0
         * @text ActivityCivilian 띄움
         * @since 2017-11-07 오후 3:42
         */
        Intent intent2 = new Intent(this, ActivityCivilian.class);
        startActivity(intent2);
    }



    @Override
    public ArrayList searchList(ContentValues contentValues) {
        return null;
    }

    @Override
    public int modify(ContentValues contentValues) {
        return 0;
    }

    @Override
    public int insert(ContentValues contentValues) {
        return 0;
    }

    @Override
    public int remove(ContentValues contentValues) {
        return 0;
    }


    /*
     * @author 경창현
     * @version 1.0.0
     * @text 퍼미션 작성 -> 미완료  퍼미션 요청 메시지가 안뜸
     * @since 2017-11-14 오전 12:12
     */
    public void initPermissions()
    {
        Log.d("initPermissions","오는가");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            permissionCheck = true;
            Log.d("initPermissions","T");
//            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this,
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
//                mMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

