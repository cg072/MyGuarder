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

        initFragment();

        btnGuarderLog.setOnClickListener(this);
    }

    /*
     * @author 경창현
     * @version 1.0.0
     * @text NaverMap Fragment 등록
     * @since 2017-11-06 오후 11:45
     */
    public void initFragment()
    {
        Fragment fragment = new FragmentMap();
        fragment.setArguments(new Bundle());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragMapHere, fragment);
        fragmentTransaction.commit();
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
