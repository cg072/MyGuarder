package home.safe.com.myguarder;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityGuarder extends AppCompatActivity implements View.OnClickListener{

    Button btnGuarderLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guarder);

        btnGuarderLog = (Button)findViewById(R.id.btnGuarderLog);


        btnGuarderLog.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        //지난위치보기 팝업
        if(view.getResources() == btnGuarderLog.getResources())
        {
            Intent intent = new Intent(this,ActivityPopup.class);
            startActivity(intent);
        }
    }
}
