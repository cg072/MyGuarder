package home.safe.com.myguarder;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.util.ArrayList;

/*
 * @author 경창현
 * @version 1.0.0
 * @text  추가해야할일
 *        1. 맵이 제대로 안뜸
 *        2. 뷰 부분 부드럽게 수정 30%
 *        3. 지난위치보기 내부 구현
 *        4. 전송값 확인
 *        5. 설정 버튼
 *        6. 설정 엑티비티
 * @since 2017-11-07 오후 5:23
 */



public class MainActivity extends ProGuardian implements IProGuardian {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * @author 경창현
         * @version 1.0.0
         * @text ActivityGuarder 띄움
         * @since 2017-11-06 오후 11:47
         */
//        Intent intent = new Intent(this, ActivityGuarder.class);
//        startActivity(intent);

        /*
         * @author 경창현
         * @version 1.0.0
         * @text ActivityCivilian 띄움
         * @since 2017-11-07 오후 3:42
         */
        Intent intent2 = new Intent(this, ActivityCivilian.class);
        startActivity(intent2);
    }

    @Override
    public ArrayList searchList(ContentValues contentValues) {
        return null;
    }

    @Override
    public int modify(ContentValues contentValues) {
        return 0;
    }

    @Override
    public int insert(ContentValues contentValues) {
        return 0;
    }

    @Override
    public int remove(ContentValues contentValues) {
        return 0;
    }

}

