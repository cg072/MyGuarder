package home.safe.com.myguarder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/*
 * @author 경창현
 * @version 1.0.0
 * @text 지난위치보기 Popup
 * - 지난 위치보기 날짜 한달
 * @since 2017-11-07 오후 5:18
 */

public class ActivityPopup extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{

    ListView lvPopupList;
    ArrayList<MyGuarderVO> alData;
    AdapterPopupList adapterPopupList;

    Intent intent;
    ArrayList<CharSequence> dateList;

    Button btnPopupOk;

    private final static String DATA_NAME_POPUP = "dateList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        selectPopupDateList();

        adapterPopupList = new AdapterPopupList(alData);

        lvPopupList = (ListView)findViewById(R.id.lvPopupList);
        lvPopupList.setAdapter(adapterPopupList);

        btnPopupOk = findViewById(R.id.btnPopupOk);

        btnPopupOk.setOnClickListener(this);
        lvPopupList.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getResources() == btnPopupOk.getResources())
            finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(this, alData.get(position).getLday(),Toast.LENGTH_SHORT).show();

        //date 별로 sql로 가져와서 리스트에 뿌려주고
        // 뿌린 리스트를 여기서 클릭했을 때 해당 날짜의 데이터를 모두 sql로 가져와서 맵에 뿌린다.

        //sql -> 1. db에서 날짜만 다가져와서 지난위치 리스트 뿌림
        //       2. 클릭한 날짜를 db에서 해당 날짜 자료를 다 가져옴
        //아래 메서드
        returnPopupData(alData.get(position).getLday());
    }

    /**
    *
    * @author 경창현
    * @version 1.0.0
    * @text sql에서 날자를 추려서 가져옴
     * reference : String date
    * @since 2017-12-08 오후 1:24
    **/
    public void selectPopupDateList()
    {
        Intent intent = getIntent();
        dateList = intent.getCharSequenceArrayListExtra("dataList");

        alData = new ArrayList<MyGuarderVO>();
        int num = 1;
        CharSequence dl;

        for(num = 0;num < dateList.size();num++)
        {
            dl = dateList.get(dateList.size() - num -1);
            alData.add( new MyGuarderVO(num+1, String.valueOf(dl)));
        }
    }

    /**
    *
    * @author 경창현
    * @version 1.0.0
    * @text ActivityCivilian,ActivityMyGuarder로 날짜를 리턴시킴
     * reference String date
    * @since 2017-12-08 오후 2:17
    **/
    public void returnPopupData(String date)
    {
        //date로 sql에서 해당 날짜 리스트를 가져옴
        Intent intentData = new Intent();
        intentData.putExtra(DATA_NAME_POPUP, date);
        setResult(RESULT_OK, intentData);
        finish();
    }
}
