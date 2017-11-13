package home.safe.com.myguarder;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.View;
import android.widget.Button;



import com.google.android.gms.maps.MapView;

/*
 * @author 경창현
 * @version 1.0.0
 * @text 구글 api로 바꿈
 * @since 2017-11-14 오전 12:14
 */

public class ActivityCivilian extends ProGuardian implements View.OnClickListener{

    MapView mapView;
    Button btnCivilianLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_civilian);

        btnCivilianLog = (Button)findViewById(R.id.btnCivilianLog);


        //fragment inflater
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mapView = (MapView)layoutInflater.inflate(R.layout.fragment_map,null).findViewById(R.id.map);

        //initFragment
        initFragment();

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
