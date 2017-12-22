package home.safe.com.myguarder;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.safe.home.pgchanger.*;

import java.util.ArrayList;
import java.util.List;


//액티비티 -> 관리객체(VO 생성) -> changer생성(context는 파라미터) -> changer에서 필요한 메서드 접근
// -> changer에서 controller를 통해 db 접근 -> 데이터 질의결과 받음 -> 결과 응용(액티비티 나 관리객체에서 수행)
public class ActivityTest extends ProGuardian {
    TextView tvTest1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tvTest1 = (TextView)findViewById(R.id.test_content_tvTest);
        tvTest1.setMovementMethod(new ScrollingMovementMethod());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabTest);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                showProgressDialog("검색 중", 1000);
                testRemoveTable();
                testInsert();
                //testSearch();
                testListSearch();

            }
        });
    }

    private void testInsert() {
        PreTest preTest = new PreTest(getApplicationContext());
        List<PreTestVO> list = new ArrayList<PreTestVO>();
        list = preTest.testListSearch();
        if(list.size() < 1) {
            int res = preTest.testInsert();
            Log.d("ActivityTest", "insert result " + String.valueOf(res));
            tvTest1.setText(String.valueOf(res));
        }
    }

    private void testSearch() {
        PreTest preTest = new PreTest(getApplicationContext());
        PreTestVO vo = new PreTestVO();
        vo = (PreTestVO) preTest.testSearch();
        Log.d("ActivityTest","search result " + vo.getTest1());
        tvTest1.setText(vo.getDetails());

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
            tvTest1.append(vo.getString());
        }

    }

    private void testRemoveTable() {
        PreTest preTest = new PreTest(getApplicationContext());
        preTest.testRemoveTable();    //테이블삭제
    }

    private void showProgressDialog() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("검색 중");
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 2000);
    }

    private void showProgressDialog(String msg, int timeDelay) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(msg);
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, timeDelay);
    }

}
