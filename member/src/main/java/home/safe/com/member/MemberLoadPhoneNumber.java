package home.safe.com.member;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created by hotki on 2018-01-19.
 */

public class MemberLoadPhoneNumber extends AppCompatActivity{
    Activity activity;
    boolean checkPermission = false;
    String myPhoneNumber;

    public MemberLoadPhoneNumber(Activity activity){
        this.activity = activity;

        while(myPhoneNumber == null) {
            getMemberPhone();
        }
    }

    /*
    *  date     : 2017.11.12
    *  author   : Kim Jong-ha
    *  title    : getMemnerPhone 메소드 생성
    *  comment  : 권한이 부여가 되어있다면 User의 기기에서 PhoneNumber을 불러옴
    * */
    @SuppressLint("MissingPermission")
    private void getMemberPhone()
    {
        TelephonyManager telephonyManager = (TelephonyManager) activity.getApplicationContext().getSystemService(activity.getApplicationContext().TELEPHONY_SERVICE);

        @SuppressLint("MissingPermission") String phoneNum = telephonyManager.getLine1Number();

        TelephonyManager mgr = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

        try {
            myPhoneNumber = mgr.getLine1Number();
            myPhoneNumber = myPhoneNumber.replace("+82", "0");
        } catch (Exception e) {
            Toast.makeText(activity, "전화번호 가져오기 실패", Toast.LENGTH_SHORT).show();
        }
    }

    /*
 *  date     : 2017.11.22
 *  author   : Kim Jong-ha
 *  title    : addHyphen() 메소드 생성
 *  comment  : 전화 번호 사이의 '-' 를 추가한다
 *  return   : String 형태
 * */
    private String addHyphen(String phone) {

        String resultString = phone;

        switch(resultString.length()) {
            case 10 :
                resultString =  resultString.substring(0,3) + "-" +
                        resultString.substring(3,6) + "-" +
                        resultString.substring(6,10);
                break;

            case 11 :
                resultString =  resultString.substring(0,3) + "-" +
                        resultString.substring(3,7) + "-" +
                        resultString.substring(7,11);
                break;
            default :
                resultString = "Not Mobile";
        }
        return resultString;
    }

    public String getMyPhoneNumber(){
        return myPhoneNumber;
    }
}
