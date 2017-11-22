package home.safe.com.myguarder;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import home.safe.com.myguarder.changer.PreTest;
import home.safe.com.myguarder.changer.PreTestVO;
import home.safe.com.myguarder.changer.ProGuardianVO;

//액티비티 -> 관리객체(VO 생성) -> changer생성(context는 파라미터) -> changer에서 필요한 메서드 접근
// -> changer에서 controller를 통해 db 접근 -> 데이터 질의결과 받음 -> 결과 응용(액티비티 나 관리객체에서 수행)
public class ActivityTest extends ProGuardian {
    TextView tvTest1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvTest1 = (TextView)findViewById(R.id.test_content_tvTest);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabTest);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                //testRemoveTable();
                //testInsert();
                //testSearch();
                testListSearch();
            }
        });
    }

    private void testInsert() {
        PreTest preTest = new PreTest(getApplicationContext());
        int res = preTest.testInsert();
        Log.d("ActivityTest","insert result " + String.valueOf(res));
        tvTest1.setText(String.valueOf(res));
    }

    private void testSearch() {
        PreTest preTest = new PreTest(getApplicationContext());
        PreTestVO vo = new PreTestVO();
        vo = (PreTestVO) preTest.testSearch();
        Log.d("ActivityTest","search result " + vo.getTest1());
        tvTest1.setText(vo.getDetail());

    }

    private void testListSearch() {
        PreTest preTest = new PreTest(getApplicationContext());

        List<PreTestVO> list = new ArrayList<PreTestVO>();
        list = preTest.testListSearch();

        tvTest1.setText("");
        PreTestVO vo = new PreTestVO();
        for(int a=0; a < list.size(); a++)
        {
            vo = list.get(a);
            tvTest1.append(vo.getDetails());
        }

    }

    private void testRemoveTable() {
        PreTest preTest = new PreTest(getApplicationContext());
        preTest.testRemoveTable();    //테이블삭제
    }

}
