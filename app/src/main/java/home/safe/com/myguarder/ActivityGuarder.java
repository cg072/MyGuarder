package home.safe.com.myguarder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ActivityGuarder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guarder);

        initFragment();

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
}
