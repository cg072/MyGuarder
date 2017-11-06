package home.safe.com.trans;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

    /*2017. 11. 03
    author : 박준규
    흐름 : 이동수단 설정 -> 메인 -> 이동수단 내역 보기 click
    실행되는 동작 : 1. 이동수단 내역 보여주기
                    2. 확인(transListFinish) 버튼을 누르면 종료
    */


public class ActivityTransListPopup extends AppCompatActivity implements View.OnClickListener{

    Button transListFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_list_popup);

        transListFinish = (Button) findViewById(R.id.transListFinish);
        transListFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == transListFinish){
            finish();
        }

    }
}
