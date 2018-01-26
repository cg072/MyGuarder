package home.safe.com.guarder;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class ActivityGuarder extends AppCompatActivity {

    final static private String TAB_FIRST = "회원 검색";
    final static private String TAB_SECOND = "지킴이 등록/해제";

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private boolean checkPermission = false;

    ListView lvGuarders;
    GuarderVO guarderVO;

    ArrayList<GuarderVO> phoneList = null;
    FragmentAdapter fragmentAdapter;

    final private String TAG = "가드";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guarder);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        tabLayout.addTab(tabLayout.newTab().setText(TAB_FIRST), 0, true);
        tabLayout.addTab(tabLayout.newTab().setText(TAB_SECOND), 1);
        tabLayout.addOnTabSelectedListener(tabSelectedListener);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        lvGuarders = (ListView) findViewById(R.id.lvGuarders);

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.setActivity(this);
        fragmentAdapter.setGuarderListInFmSearch(fragmentAdapter.getGuarderListInFmGuarder());
        viewPager.setAdapter(fragmentAdapter);

        //checkPermission();

    }

    // 키보드 숨기기
    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

            EditText etSearch = (EditText) findViewById(R.id.etSearch);

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

            viewPager.setCurrentItem(tab.getPosition());

            if(tab.getPosition() == 0) {
                fragmentAdapter.resetAddGuarderVOInFmSearch();
                fragmentAdapter.setGuarderListInFmSearch(fragmentAdapter.getGuarderListInFmGuarder());
            } else if (tab.getPosition() == 1){
                fragmentAdapter.updateGuarderListInFmGuarder();
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };
}


//import home.safe.com.guarder.R;
