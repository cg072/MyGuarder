package home.safe.com.guarder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityGuarder extends AppCompatActivity implements ListViewAdapterSearch.SearchListBtnClickListener, ListViewAdapterGuarders.GuardersListBtnClickListener{

    EditText etSearch;
    Button btnSearch;
    ListView lvSearch;
    ListView lvGuarders;
    ListViewItemSearch lvItemSearch;
    ListViewAdapterSearch lvAdapterSearch;
    ListViewAdapterGuarders lvAdapterGuarders;
    private boolean checkPermission = false;
    String nowGuarder;

    ArrayList<ListViewItemSearch> alSearch;

    final private String TAG = "가드";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guarder);

        etSearch = (EditText) findViewById(R.id.etSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);

        ArrayList<ListViewItemGuarders> alGuarders = new ArrayList<ListViewItemGuarders>();

        // alSearch의 아이템들을 Load
        //loadSearchFromDB(getList());  // <- 검색버튼을 눌렀을 시에 적용이 되어야한다.
        //loadItemsFromDB(alGuarders);
        loadItemsFromDB(alGuarders);

        // Adapter 생성 (implements ListViewAdapterSearch.ListBtnClickListener를 하였기때문에, 마지막에 this해도 오류 안남)
        lvAdapterSearch = new ListViewAdapterSearch(this, R.layout.listview_item_search, getList(), this);
        lvAdapterGuarders = new ListViewAdapterGuarders(this, R.layout.listview_item_guarders, alGuarders, this);

        // 리스트뷰 참조 및 Adapter 달기
        lvSearch = (ListView) findViewById(R.id.lvSearch);
        lvSearch.setAdapter(lvAdapterSearch);
        lvGuarders = (ListView) findViewById(R.id.lvGuarders);
        lvGuarders.setAdapter(lvAdapterGuarders);

        btnSearch.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 검색 버튼을 눌렀을 시 해야할 행동
           }
        });

        // lvSearch에 클릭 이벤트 핸들러 정의
        // 아이템 눌렀을 경우인데 다시 봐야할 듯
        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 아이템 클릭시 해야할 행동
                Toast.makeText(ActivityGuarder.this, "검색 리스트뷰 아이템 눌림", Toast.LENGTH_SHORT).show();
            }
        });

        lvGuarders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 아이템 클릭시 해야할 행동
                Toast.makeText(ActivityGuarder.this, "지킴이 리스트뷰 아이템 눌림", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ArrayList에 데이터를 로드하는 loadItemsFromDB() 함수를 추가
    // 현재 내용은 직접 DB를 다루진 않고 리스트에 값을 저장해서 리턴시킴킴

    /*
    *  date     : 2017.11.19
    *  author   : Kim Jong-ha
    *  title    : getList 메소드 생성
    *  comment  : 주소록에 있는 사람들 중 이름과 전화번호가 둘 다 있는 사람들은 가져온다.
    *             checkPermission으로 권한 체크한다.
    * */
    public ArrayList<ListViewItemSearch> getList(){

        checkPermission();

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
                lvItemSearch.setTvPhone(phone);

                // 여기 if문 아래서 추가를 해야 전화번호가 있는 사람만 담아간다.
                alSearch.add(lvItemSearch);
            }

            phoneCursor.close();

        }// end while
        c.close();

        return alSearch;
    }

    public boolean loadItemsFromDB(ArrayList<ListViewItemGuarders> list) {
        ListViewItemGuarders lvItemGuarders;
        int desc ;

        if(list == null) {
            list = new ArrayList<ListViewItemGuarders>();
        }

        // 순서를 위한 desc값을 1로 초기화.
        desc = 1;

        // 아이템 생성.
        lvItemGuarders = new ListViewItemGuarders();
        lvItemGuarders.setTvName("김종하");
        lvItemGuarders.setTvPhone("01072058516");
        list.add(lvItemGuarders);
        desc++;

        // 아이템 생성.
        lvItemGuarders = new ListViewItemGuarders();
        lvItemGuarders.setTvName("김진복");
        lvItemGuarders.setTvPhone("010알아몰라");
        list.add(lvItemGuarders);
        desc++;

        // 아이템 생성.
        lvItemGuarders = new ListViewItemGuarders();
        lvItemGuarders.setTvName("박준규");
        lvItemGuarders.setTvPhone("010몰라알아아");
        list.add(lvItemGuarders);
        desc++;

        // 아이템 생성.
        lvItemGuarders = new ListViewItemGuarders();
        lvItemGuarders.setTvName("경창현");
        lvItemGuarders.setTvPhone("010몰라몰라");
        list.add(lvItemGuarders);
        desc++;

        return true;
    }

    // 이거 안뜸 ㅠ_ㅠ
    @Override
    public void onGuardersListBtnClick(int position) {
        Toast.makeText(this, Integer.toString(position+1) + "아이템이 선택되었습니다.", Toast.LENGTH_SHORT).show();
        //int nowPosition
    }
    @Override
    public void onSearchListBtnClick(int position, ListViewItemSearch lvItemSearch) {
        //Toast.makeText(this, Integer.toString(position+1) + "아이템이 선택되었습니다.", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, lvItemSearch.getTvName() + lvItemSearch.getTvPhone(), Toast.LENGTH_SHORT).show();
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
