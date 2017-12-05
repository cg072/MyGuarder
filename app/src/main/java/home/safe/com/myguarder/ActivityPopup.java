package home.safe.com.myguarder;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
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
    ArrayList<LocationVO> alData;
    AdapterPopupList adapterPopupList;

    Button btnPopupOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        alData = new ArrayList<LocationVO>();
        alData.add(new LocationVO(1,"2017.12.05"));
        alData.add(new LocationVO(2,"2017.12.04"));
        alData.add(new LocationVO(3,"2017.12.03"));

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
    }
}
