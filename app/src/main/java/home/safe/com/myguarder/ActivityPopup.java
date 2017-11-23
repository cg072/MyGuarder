package home.safe.com.myguarder;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/*
 * @author 경창현
 * @version 1.0.0
 * @text 지난위치보기 Popup
 * @since 2017-11-07 오후 5:18
 */

public class ActivityPopup extends Activity implements View.OnClickListener{

    Button btnPopupOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        btnPopupOk = findViewById(R.id.btnPopupOk);

        btnPopupOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getResources() == btnPopupOk.getResources())
            finish();
    }
}
