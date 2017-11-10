package home.safe.com.myguarder;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ActivityCivilian extends ProGuardian implements View.OnClickListener{

    Button btnCivilianLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_civilian);

        btnCivilianLog = (Button)findViewById(R.id.btnCivilianLog);

        initFragment();
        startMyLocation();

        btnCivilianLog.setOnClickListener(this);
    }

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
        if(view.getResources() == btnCivilianLog.getResources())
        {
            Intent intent = new Intent(this,ActivityPopup.class);
            startActivity(intent);
        }

    }



}
