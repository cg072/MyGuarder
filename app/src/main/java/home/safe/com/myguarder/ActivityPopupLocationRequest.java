package home.safe.com.myguarder;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class ActivityPopupLocationRequest extends Activity implements View.OnClickListener{

    Button btnLocationReqOk;
    Button btnLocationReqNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_location_request);

        btnLocationReqOk = (Button)findViewById(R.id.btnLocationReqOk);
        btnLocationReqNo = (Button)findViewById(R.id.btnLocationReqNo);

        btnLocationReqOk.setOnClickListener(this);
        btnLocationReqNo.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == btnLocationReqOk.getId())
        {
            finish();
        }
        else if(view.getId() == btnLocationReqNo.getId())
        {
            finish();
        }

    }
}
