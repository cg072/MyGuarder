package home.safe.com.guarder;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ActivityGuarder extends AppCompatActivity {

    final static private String TAB_FIRST = "회원 검색";
    final static private String TAB_SECOND = "지킴이 등록/해제";

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private boolean checkPermission = false;

    ListView lvGuarders;
    GuarderVO guarderVO;

    ArrayList<GuarderVO> alSearch = null;
    ArrayList<GuarderVO> alSearchResult = null;
    ArrayList<GuarderVO> alGuarders = null;

    final private String TAG = "가드";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guarder);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        checkPermission();

        tabLayout.addTab(tabLayout.newTab().setText(TAB_FIRST), 0, true);
        tabLayout.addTab(tabLayout.newTab().setText(TAB_SECOND), 1);
        tabLayout.addOnTabSelectedListener(tabSelectedListener);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        lvGuarders = (ListView) findViewById(R.id.lvGuarders);

    }

    // 키보드 숨기기
    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

            EditText etSearch = (EditText) findViewById(R.id.etSearch);

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    /*
     *  date     : 2017.11.22
     *  author   : Kim Jong-ha
     *  title    : hyphenRemove() 메소드 생성
     *  comment  : 전화 번호 사이의 '-' 를 제거한다
     *  return   : String 형태
     * */
    private String hyphenRemove(String phone) {

        String[] basePhone = phone.split("-");

        Log.v(TAG, "나눔"+basePhone[0].length()+ " " + basePhone[0]);
        String resultPhone = basePhone[0];
        if(basePhone[0].length() < 10) {
            resultPhone = resultPhone + basePhone[1] + basePhone[2];
        }

        return resultPhone;
    }

    // 이하의 코딩 내용은 주소록 불러오기 관련
   /*
    *  date     : 2017.11.19
    *  author   : Kim Jong-ha
    *  title    : getList 메소드 생성
    *  comment  : 시험용 리스트
    * */
    private void loadList() {

        alSearch = new ArrayList<GuarderVO>();
        Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " asc");

        while (c.moveToNext()) {
            guarderVO = new GuarderVO();
            // 연락처 id 값
            String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            // 연락처 대표 이름
            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
            guarderVO.setGmcname(name);

            // ID로 전화 정보 조회
            Cursor phoneCursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                    null, null);

            // 데이터가 있는 경우
            if (phoneCursor.moveToFirst()) {
                String phone = phoneCursor.getString(phoneCursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));

                // hyphen을 제거하는 메소드를 추가하였다.
                guarderVO.setGmcphone(hyphenRemove(phone));

                // 여기 if문 아래서 추가를 해야 전화번호가 있는 사람만 담아간다.
                alSearch.add(guarderVO);
            }

            phoneCursor.close();

        }// end while

        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), alSearch, this));
        c.close();
    }

    /*
      *  date     : 2017.11.12
      *  author   : Kim Jong-ha
      *  title    : checkPermission 메소드 생성
      *  comment  : 권한이 부여되었는지, 없다면 권한 재요청인지, 첫요청인지를 판단함
      *             첫요청인지 재요청인지를 판단하는 부분은 당장은 필요한 부분이 아니나, 남겨둠
      * */
    private void checkPermission()
    {
        Log.v(TAG, "checkPermission들어옴");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //퍼미션이 없는 경우
            //최초로 퍼미션을 요청하는 것인지 사용자가 취소되었던것을 다시 요청하려는건지 체크
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                //퍼미션을 재요청 하는 경우 - 왜 이 퍼미션이 필요한지등을 대화창에 넣어서 사용자를 설득할 수 있다.
                //대화상자에 '다시 묻지 않기' 체크박스가 자동으로 추가된다.
                Log.v(TAG, "퍼미션을 재요청 합니다.");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);

            } else {
                //처음 퍼미션을 요청하는 경우
                Log.v(TAG, "첫 퍼미션 요청입니다.");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
            }
        } else {
            //퍼미션이 있는 경우 - 쭉 하고 싶은 일을 한다.
            checkPermission = true;
            loadList();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 당겨서 새로고침
                }
            }, 2000);
            //searchAdapterUpdate();
            //guarderAdapterUpdate();
            Log.v(TAG, "Permission is granted");
            Toast.makeText(this, "\"이미 퍼미션이 허용되었습니다.\"", Toast.LENGTH_SHORT).show();
        }
        Log.v(TAG, "checkPermission나감");
    }

    /*
    *  date     : 2017.11.12
    *  author   : Kim Jong-ha
    *  title    : onRequestPermissionResult 메소드 불러옴
    *  comment  : 권한 사용or거부 요청창을 띄워 사용자가 권한을 동의, 비동의 때의 Perform을 둠
    * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: //
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //사용자가 동의했을때
                    Toast.makeText(this, "퍼미션 동의", Toast.LENGTH_SHORT).show();
                    checkPermission = true;
                    checkPermission();
                    loadList();
                } else {
                    //사용자가 거부 했을때
                    Toast.makeText(this, "거부 - 동의해야 사용가능합니다.", Toast.LENGTH_SHORT).show();

                    /*new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000);*/
                }
                return;
        }
    }
}


//import home.safe.com.guarder.R;
