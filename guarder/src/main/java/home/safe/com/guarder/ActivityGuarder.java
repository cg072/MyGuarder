package home.safe.com.guarder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import home.safe.com.guarder.R;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityGuarder extends AppCompatActivity implements ListViewAdapterSearch.SearchListBtnClickListener, ListViewAdapterGuarders.GuardersListBtnClickListener{

    EditText etSearch;
    Button btnSearch;
    ListView lvSearch;
    ListView lvGuarders;
    ListViewItemSearch lvItemSearch;
    ListViewItemGuarders lvItemGuarders;
    ListViewAdapterSearch lvAdapterSearch;
    ListViewAdapterGuarders lvAdapterGuarders;
    private boolean checkPermission = false;
    String nowGuarderName;
    String nowGuarderPhone;

    ArrayList<ListViewItemSearch> alSearch;
    ArrayList<ListViewItemGuarders> alGuarders;

    private static ListViewItemGuarders sortGuarders;

    final private String TAG = "가드";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guarder);

        etSearch = (EditText) findViewById(R.id.etSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);

        lvSearch = (ListView) findViewById(R.id.lvSearch);
        lvGuarders = (ListView) findViewById(R.id.lvGuarders);

        alGuarders = new ArrayList<ListViewItemGuarders>();

        // 기기에 있는 전화번호를 불러온다. 추후에는 기기에 있는 번호를 보내는 메소드가 필요함
        // private ArrayList<ListViewItemSearch> listSetting로 값을 반환하여 서버에 보내야한다.
        checkPermission();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 검색 버튼을 눌렀을 시 해야할 행동
           }
        });
    }

    /*
     *  date     : 2017.11.22
     *  author   : Kim Jong-ha
     *  title    : hyphenRemove() 메소드 생성
     *  comment  : 전화 번호 사이의 '-' 를 제거한다
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

    // ArrayList에 데이터를 로드하는 loadItemsFromDB() 함수를 추가
    // 현재 내용은 직접 DB를 다루진 않고 리스트에 값을 저장해서 리턴시킴킴
   /*
    *  date     : 2017.11.19
    *  author   : Kim Jong-ha
    *  title    : getList 메소드 생성
    *  comment  : 주소록에 있는 사람들 중 이름과 전화번호가 둘 다 있는 사람들은 가져온다.
    *             checkPermission으로 권한 체크한다.
    *             추후에는 이 리스트들을 서버로 보낼 것 이다.
    * */
    private void listSetting(){

        alSearch = new ArrayList<ListViewItemSearch>();
        Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " asc");

        while (c.moveToNext()) {
            lvItemSearch = new ListViewItemSearch();
            // 연락처 id 값
            String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            // 연락처 대표 이름
            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
            lvItemSearch.setTvName(name);

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
                lvItemSearch.setTvPhone(hyphenRemove(phone));

                // 여기 if문 아래서 추가를 해야 전화번호가 있는 사람만 담아간다.
                alSearch.add(lvItemSearch);
            }

            phoneCursor.close();

        }// end while

        c.close();

    }

    // ArrayList에 지킴이를 추가함 [서버와 연결 후 부터는 서버로부터 받아와야한다.]
    private void guarderAdd(String name, String phone) {
        lvItemGuarders = new ListViewItemGuarders();
        lvItemGuarders.setTvName(name);
        lvItemGuarders.setTvPhone(phone);
        lvItemGuarders.setUse(false);
        lvItemGuarders.setSort(1);

/*
        boolean sortCheck = false;


        for(ListViewItemGuarders item : alGuarders) {
            if(item.getTvName().compareTo(name) < 0) {
                lvItemGuarders.setTvName(name);
                lvItemGuarders(item. + 1);
                sortCheck = true;
            }
        }

        if(sortCheck == false) {

        }*/


        alGuarders.add(lvItemGuarders);

        sortGuarders = lvItemGuarders;

        Collections.sort(alGuarders ,new NameDescCompare());
        // 역방향 시 Collections.reverse 로 해주면 된다
    }

    /**
     * 이름 내림차순
     * @author falbb
     *
     */
    private class NameDescCompare implements Comparator<ListViewItemGuarders> {
        @Override
        public int compare(ListViewItemGuarders arg0, ListViewItemGuarders arg1) {
            return  arg0.getTvName().compareTo(arg1.getTvName());
        }
    }



    // 회원 리스트에서 등록 버튼을 눌렀을 시
    /*
    *  date     : 2017.11.20
    *  author   : Kim Jong-ha
    *  title    : onSearchListBtnClick(int position, ListViewItemSearch lvItemSearch) 메소드 생성
    *  comment  : 회원 목록에 있는 '등록' Button을 눌렀을 시의 작동 코딩.
    * */
    @Override
    public void onSearchListBtnClick(int position) {

        // 클릭한 값의 name과 phone를 ArrayList<ListViewItemGuarders> 에 추가한다.
        guarderAdd(alSearch.get(position).getTvName(), alSearch.get(position).getTvPhone());

        guarderAdapterUpdate();

        // 클릭한 position의 값을 ArrayList<ListViewItemSearch> 에서 지운다
        Toast.makeText(this, alSearch.get(position).getTvName() + alSearch.get(position).getTvPhone() + "지킴이 설정", Toast.LENGTH_SHORT).show();

        alSearch.remove(position);

        searchAdapterUpdate();
    }


    // 지킴이 리스트에서 슬라이딩 버튼을 눌렀을 시
    /*
    *  date     : 2017.11.20
    *  author   : Kim Jong-ha
    *  title    : onGuardersListBtnClick(int position, int count) 메소드 생성
    *  comment  : 지킴이 목록에 있는 SlidingButton을 눌렀을 경우의 작동 코딩
    *             지킴이는 1명만 지정이 가능하기 때문에, 지킴이로 지정된 ListView의 item을 제외하고는 모두 Button 상태가 false가 되어야한다.
    * */
    @Override
    public void onGuardersListBtnClick(int position, int count) {

        // count는 지킴이 목록의 숫자를 의미
        for( int i = 0 ; i < count ; i++ ) {

            if( i != position) {
                // 해당 position이 아니라면 button을 비활성화 시킨다
                alGuarders.get(i).setUse(false);

            } else {
                // Button Off 상태
                if(alGuarders.get(i).getUse() == false) {
                    // 해당 position이라면 button을 활성화 시키고, name, phone을 가져온다.
                    alGuarders.get(i).setUse(true);
                    nowGuarderName = alGuarders.get(i).getTvName();
                    nowGuarderPhone = alGuarders.get(i).getTvPhone();
                    Toast.makeText(this, nowGuarderName + " 님이 지킴이로 설정되었습니다.", Toast.LENGTH_SHORT).show();

                // Button On 상태
                } else {
                    // 해당 position이라면 button을 활성화 시키고, name, phone을 가져온다.
                    alGuarders.get(i).setUse(false);
                    nowGuarderName = null;
                    nowGuarderPhone = null;
                    Toast.makeText(this, "현재 지킴이가 없습니다.", Toast.LENGTH_SHORT).show();
                }

                //위의 nowGuarder부분을 다른 액티비티, 서버로 전송하여 준다.
            }
        }
        // button을 누름으로써, 변경된 내역을 기반으로 지킴이 목록 갱신
        guarderAdapterUpdate();
    }

     /*
    *  date     : 2017.11.20
    *  author   : Kim Jong-ha
    *  title    : guarderSearchUpdate(ArrayList<ListViewItemSearch> alSearch, ArrayList<ListViewItemGuarders> alGuarders 메소드 생성
    *  comment  : Activity 로딩 시, 회원 목록과 지킴이 목록의 중첩을 검색 후, 회원목록에서 삭제한다.
    *  return   : ArrayList<ListViewItemSearch> 형태
    * */
    private ArrayList<ListViewItemSearch> searchUpdate(ArrayList<ListViewItemSearch> alSearch, ArrayList<ListViewItemGuarders> alGuarders) {

        for( int i = 0 ; i < alSearch.size() ; i ++ ) {
            for( int j = 0 ; j < alGuarders.size() ; i++ ) {
                if( alSearch.get(i).getTvName().equals(alGuarders.get(j).getTvName())
                    && alSearch.get(i).getTvPhone().equals(alGuarders.get(j).getTvPhone())){
                    alSearch.remove(i);
                    break;
                }
            }
        }

        return alSearch;
    }

    // 회원 목록의 어댑터 갱신
    private void searchAdapterUpdate() {
        // Adapter 생성 (implements ListViewAdapterSearch.ListBtnClickListener를 하였기때문에, 마지막에 this해도 오류 안남)
        //lvAdapterSearch = new ListViewAdapterSearch(this, R.layout.listview_item_search, alSearch, this);
        lvAdapterSearch = new ListViewAdapterSearch(this, R.layout.listview_item_search, alSearch, this);
        // 변경된 Adapter 적용
        lvAdapterSearch.notifyDataSetChanged();
        // 리스트뷰 참조 및 Adapter 달기
        lvSearch.setAdapter(lvAdapterSearch);
    }

    // 지킴이 목록의 어댑터 갱신
    private void guarderAdapterUpdate() {
        // Adapter 생성 (implements ListViewAdapterSearch.ListBtnClickListener를 하였기때문에, 마지막에 this해도 오류 안남)
        lvAdapterGuarders = new ListViewAdapterGuarders(this, R.layout.listview_item_guarders, alGuarders, this);
        // 변경된 Adapter 적용
        lvAdapterGuarders.notifyDataSetChanged();
        // 리스트뷰 참조 및 Adapter 달기
        lvGuarders.setAdapter(lvAdapterGuarders);
    }

    // 주소록 읽기 권한 체크하는 부분

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
            listSetting();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 당겨서 새로고침
                }
            }, 2000);
            searchAdapterUpdate();
            guarderAdapterUpdate();
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
                    //listSetting();
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
