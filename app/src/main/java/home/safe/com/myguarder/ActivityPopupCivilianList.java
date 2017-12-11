package home.safe.com.myguarder;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class ActivityPopupCivilianList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_civilian_list);
    }

    /**
     *
     * @author 경창현
     * @version 1.0.0
     * @text
     * http://www.masterqna.com/android/72664/%EB%A6%AC%EC%8A%A4%ED%8A%B8%EB%B7%B0-%EC%95%84%EC%9D%B4%ED%85%9C%EC%9D%84-%ED%81%B4%EB%A6%AD%ED%95%98%EB%A9%B4-%EC%83%89%EC%9D%B4-%EB%B3%80%ED%95%98%EB%8A%94-%EA%B8%B0%EB%8A%A5%EC%9D%84-%EC%A7%88%EB%AC%B8%ED%95%98%EA%B3%A0-%EC%8B%B6%EC%8A%B5%EB%8B%88%EB%8B%A4
     * @since 2017-12-11 오후 9:30
    **/
}
