package home.safe.com.myguarder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import home.safe.com.guarder.GuarderVO;

/**
 *
 * @author 경창현
 * @version 1.0.0
 * @text 피지킴이 목록 뿌리는 Popup
 * @since 2017-12-13 오후 4:14
**/
public class ActivityPopupCivilianList extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener{

    ListView lvCivilianList;
    ArrayList<GuarderVO> alData;
    AdapterCivilianList adapterCivilianList;
    Button btnCivilianEnter;

    String selectCivilianID;
    ArrayList<GuarderVO> dateList;

    private final static String DATA_CIVILIAN_NAME = "civilianName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_civilian_list);

        //피지킴이 목록 추가
        showCivilianList();

        adapterCivilianList = new AdapterCivilianList(alData);

        lvCivilianList = (ListView)findViewById(R.id.lvCivilianList);
        btnCivilianEnter = (Button)findViewById(R.id.btnCivilianEnter);

        lvCivilianList.setAdapter(adapterCivilianList);

        lvCivilianList.setOnItemClickListener(this);
        btnCivilianEnter.setOnClickListener(this);
    }

    private void showCivilianList() {

        // DB에서 피지킴이 목록을 가져옴
        //피지킴이iD 는 이름으로 치환하여 가져옴
        Intent intent = getIntent();
        dateList = intent.getParcelableArrayListExtra("CivilianList");

        alData = new ArrayList<GuarderVO>();

        for(GuarderVO vo : dateList)
        {
            Log.d("showCivilianList",vo.getGmcid());
            alData.add(new GuarderVO(vo.getGmcname(),vo.getGmcphone(),vo.getGmcid(),vo.getGstate()));
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        switch (alData.get(i).getGstate())
        {
            case 0:
                //지킴이 선택 x
                selectCivilianID = null;
                break;
            case 1:
                //지킴이 선택 o
                selectCivilianID = alData.get(i).getGmcid();
                break;
        }
    }

    @Override
    public void onClick(View view) {

        if(null != selectCivilianID)
        {
            returnCivilianListData(selectCivilianID);
        }
    }

    public void returnCivilianListData(String date)
    {
        //date로 sql에서 해당 날짜 리스트를 가져옴
        Intent intentData = new Intent();
        intentData.putExtra(DATA_CIVILIAN_NAME, date);
        setResult(RESULT_OK, intentData);
        finish();
    }

}
