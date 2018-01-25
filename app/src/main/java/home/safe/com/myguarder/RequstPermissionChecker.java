package home.safe.com.myguarder;

import android.*;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by kch on 2018-01-25.
 */

public class RequstPermissionChecker {

    Activity activity;

    ArrayList<String> permissionArr;

    //퍼미션
    public static final int MY_PERMISSIONS_CODE = 99;
    public static final int MY_PERMISSION_FINE_SMS_SEND = 100;

    public RequstPermissionChecker(Activity activity)
    {
        this.activity = activity;
    }

    public void lacksPermissions(String... permissions) {
        permissionArr = new ArrayList<>();

        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                permissionArr.add(permission);
                Log.d("RequstPermissionChecker", "lacksPermission - "+ permission);
            }
        }

    }

    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(activity.getApplicationContext(), permission) == PackageManager.PERMISSION_DENIED;
    }

    public void getPermission()
    {
        Log.d("Permission","ACCESS_FINE_LOCATION");
        // 퍼미션 체크
        if (!permissionArr.isEmpty()) {
            // 퍼미션 없음
            String[] permissionList = permissionArr.toArray(new String[permissionArr.size()]);

            Log.d("RequstPermissionChecker","getPermission - 퍼미션 없음");
//            if(ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.ACCESS_FINE_LOCATION))
//            {
//                Log.d("RequstPermissionChecker","shouldShowRequestPermissionRationale");
//            }

            // 퍼미션 요청
            ActivityCompat.requestPermissions(activity, permissionList, MY_PERMISSIONS_CODE);
        } else {
            //퍼미션 있음
            Log.d("RequstPermissionChecker","getPermission - 퍼미션 있음");
        }
    }



}
