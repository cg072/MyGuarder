package home.safe.com.myguarder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
*
* @author 경창현
* @version 1.0.0
* @text 전송주기 Popup
* @since 2017-11-30 오후 4:59
**/
public class ActivityPopupCycle extends Activity implements AdapterView.OnItemSelectedListener,View.OnClickListener{

    Spinner spinnerCycle;
    Button btnCycle;

    private final static String DATA_NAME = "cycle";
    int cycleNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_cycle);

        spinnerCycle = (Spinner)findViewById(R.id.spinnerCycle);
        btnCycle = (Button)findViewById(R.id.btnCycle);



        spinnerCycle.setOnItemSelectedListener(this);
        btnCycle.setOnClickListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        cycleNum = Integer.parseInt(String.valueOf(adapterView.getItemAtPosition(position)));
        Toast.makeText(this,"onItemSelected : "+cycleNum,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        Toast.makeText(this,"onClick",Toast.LENGTH_SHORT).show();

        Intent intentData = new Intent();
        intentData.putExtra(DATA_NAME, cycleNum);
        setResult(RESULT_OK, intentData);
        finish();
    }
}
