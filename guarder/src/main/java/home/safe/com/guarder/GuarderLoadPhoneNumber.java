package home.safe.com.guarder;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by hotki on 2018-01-25.
 */

public class GuarderLoadPhoneNumber extends AppCompatActivity {

    Activity activity;
    ArrayList<GuarderVO> phoneList = null;


    public GuarderLoadPhoneNumber (Activity activity) {
        if(activity == null ){
            Log.v("액티비티","널");
        }
        this.activity = activity;

        while(phoneList == null) {
            loadPhoneList();
        }
    }

    // 이하의 코딩 내용은 주소록 불러오기 관련
   /*
    *  date     : 2017.11.19
    *  author   : Kim Jong-ha
    *  title    : loadPhoneList 메소드 생성
    *  comment  : DB에 있는 전화번호부 리스트를 불러온다
    * */

    private void loadPhoneList() {

            phoneList = new ArrayList<GuarderVO>();

            String[] projection = {
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.Data.CONTACT_ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Contactables.DATA,
                    ContactsContract.CommonDataKinds.Contactables.TYPE,
            };
            String selection = ContactsContract.Data.MIMETYPE + " in (?, ?)";
            String[] selectionArgs = {
                    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
            };
            String sortOrder = ContactsContract.Contacts.SORT_KEY_ALTERNATIVE;

            Uri uri = ContactsContract.CommonDataKinds.Contactables.CONTENT_URI;

            Cursor c = activity.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);


            GuarderVO guarderVO;

            while (c.moveToNext()) {
                guarderVO = new GuarderVO();
                String id = c.getString(c.getColumnIndex(ContactsContract.Data.CONTACT_ID));
                // 연락처 대표 이름
                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                String phone = c.getString(c.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                if(phone != null) {
                    guarderVO.setGmcname(name);
                    guarderVO.setGmcphone(removeHyphen(phone));
                }

                // 여기 if문 아래서 추가를 해야 전화번호가 있는 사람만 담아간다.
                phoneList.add(guarderVO);

            }// end while

            c.close();
    }

    /*
      *  date     : 2017.11.22
      *  author   : Kim Jong-ha
      *  title    : removeHyphen() 메소드 생성
      *  comment  : 전화 번호 사이의 '-' 를 제거한다
      *  return   : String 형태
      * */
    private String removeHyphen(String phone) {

        int check = 0;
        String[] basePhone = phone.split("-");
        String resultPhone = basePhone[0];

        if(phone.contains("-")) {
            for (int i = 0; i < phone.length(); i++) {
                if (phone.charAt(i) == '-') {
                    check++;
                }
            }
        }

        switch ( check ) {
            case 1 :
                resultPhone += basePhone[1];
                break;
            case 2 :
                resultPhone = resultPhone + basePhone[1] + basePhone[2];
                break;
        }

        return resultPhone;
    }

    public ArrayList<GuarderVO> getPhoneList() {
        return phoneList;
    }
}
