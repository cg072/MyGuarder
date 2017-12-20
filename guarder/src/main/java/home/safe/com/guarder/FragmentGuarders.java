package home.safe.com.guarder;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FragmentGuarders extends Fragment implements ListViewAdapterGuarders.GuardersListBtnClickListener {

    final static String TAG = "MEMBER";

    private ListViewAdapterGuarders lvAdapterGuarders;
    private ListView lvGuarders;
    private ArrayList<ListViewItemGuarders> alGuarders;
    private ViewGroup rootView;
    private ListViewItemGuarders lvItemGuarders;
    private String nowGuarderName;
    private String nowGuarderPhone;
    private int shareNumber = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_guarders, container, false);

        lvGuarders = (ListView) rootView.findViewById(R.id.lvGuarders);
        alGuarders = new ArrayList<ListViewItemGuarders>();

        setAdd();

        lvAdapterGuarders = new ListViewAdapterGuarders(rootView.getContext(), R.layout.listview_item_search, alGuarders, this);
        lvGuarders.setAdapter(lvAdapterGuarders);

        return rootView;
    }

    @Override
    public void onGuardersListBtnClick(int position, int count) {
        // count는 지킴이 목록의 숫자를 의미
        for( int i = 0 ; i < count ; i++ ) {

            if( i != position) {
                // 해당 position이 아니라면 button을 비활성화 시킨다
                alGuarders.get(i).setUse(false);

            } else {
                // Button Off 상태
                if(alGuarders.get(i).getUse() == false) {
                    // 해당 position이라면 button을 활성화 시키고, name, phone을 가져온다.
                    alGuarders.get(i).setUse(true);
                    nowGuarderName = alGuarders.get(i).getTvName();
                    nowGuarderPhone = alGuarders.get(i).getTvPhone();
                    Toast.makeText(rootView.getContext(), nowGuarderName + " 님이 지킴이로 설정되었습니다.", Toast.LENGTH_SHORT).show();

                    // Button On 상태
                } else {
                    // 해당 position이라면 button을 활성화 시키고, name, phone을 가져온다.
                    alGuarders.get(i).setUse(false);
                    nowGuarderName = null;
                    nowGuarderPhone = null;
                    Toast.makeText(rootView.getContext(), "현재 지킴이가 없습니다.", Toast.LENGTH_SHORT).show();
                }

                //위의 nowGuarder부분을 다른 액티비티, 서버로 전송하여 준다.
            }
        }
        // button을 누름으로써, 변경된 내역을 기반으로 지킴이 목록 갱신
        guarderAdapterUpdate();
    }

    // 지킴이 목록의 어댑터 갱신
    private void guarderAdapterUpdate() {
        // Adapter 생성 (implements ListViewAdapterSearch.ListBtnClickListener를 하였기때문에, 마지막에 this해도 오류 안남)
        lvAdapterGuarders = new ListViewAdapterGuarders(rootView.getContext(), R.layout.listview_item_guarders, alGuarders, this);
        // 변경된 Adapter 적용
        lvAdapterGuarders.notifyDataSetChanged();
        // 리스트뷰 참조 및 Adapter 달기
        lvGuarders.setAdapter(lvAdapterGuarders);
    }

    /*
*  date     : 2017.11.22
*  author   : Kim Jong-ha
*  title    : NameDescCompareGuarders, NameDescCompareSearch 메소드 생성
*  comment  : 이름순 정렬
* */
    private class NameDescCompareGuarders implements Comparator<ListViewItemGuarders> {
        @Override
        public int compare(ListViewItemGuarders arg0, ListViewItemGuarders arg1) {
            return  arg0.getTvName().compareTo(arg1.getTvName());
        }
    }

    // ArrayList에 지킴이를 추가함 [서버와 연결 후 부터는 서버로부터 받아와야한다.]
    private void guarderAdd(String name, String phone) {
        lvItemGuarders = new ListViewItemGuarders();
        lvItemGuarders.setTvName(name);
        lvItemGuarders.setTvPhone(phone);
        lvItemGuarders.setUse(false);

        alGuarders.add(lvItemGuarders);

        Collections.sort(alGuarders ,new NameDescCompareGuarders());
        // 역방향 시 Collections.reverse 로 해주면 된다
    }

    public void setAdd () {
        // 예시용 목록 2 지킴이
        ListViewItemGuarders listViewItemGuarders = new ListViewItemGuarders();

        listViewItemGuarders.setTvName("스트라티스");
        listViewItemGuarders.setTvPhone("01030000300");
        listViewItemGuarders.setUse(true);
        alGuarders.add(listViewItemGuarders);

        listViewItemGuarders = new ListViewItemGuarders();
        listViewItemGuarders.setTvName("가즈아");
        listViewItemGuarders.setTvPhone("01011111111");
        listViewItemGuarders.setUse(false);
        alGuarders.add(listViewItemGuarders);

        listViewItemGuarders = new ListViewItemGuarders();
        listViewItemGuarders.setTvName("김현중");
        listViewItemGuarders.setTvPhone("01088888888");
        listViewItemGuarders.setUse(false);
        alGuarders.add(listViewItemGuarders);

        listViewItemGuarders = new ListViewItemGuarders();
        listViewItemGuarders.setTvName("허이섭");
        listViewItemGuarders.setTvPhone("01044444444");
        listViewItemGuarders.setUse(false);
        alGuarders.add(listViewItemGuarders);

        listViewItemGuarders = new ListViewItemGuarders();
        listViewItemGuarders.setTvName("놔낭");
        listViewItemGuarders.setTvPhone("01012389023");
        listViewItemGuarders.setUse(false);
        alGuarders.add(listViewItemGuarders);

        listViewItemGuarders = new ListViewItemGuarders();
        listViewItemGuarders.setTvName("뻑");
        listViewItemGuarders.setTvPhone("01098765678");
        listViewItemGuarders.setUse(false);
        alGuarders.add(listViewItemGuarders);

        listViewItemGuarders = new ListViewItemGuarders();
        listViewItemGuarders.setTvName("지리구요형");
        listViewItemGuarders.setTvPhone("01083832562");
        listViewItemGuarders.setUse(false);
        alGuarders.add(listViewItemGuarders);

        listViewItemGuarders = new ListViewItemGuarders();
        listViewItemGuarders.setTvName("지리구요동생");
        listViewItemGuarders.setTvPhone("01083832562");
        listViewItemGuarders.setUse(false);
        alGuarders.add(listViewItemGuarders);

        listViewItemGuarders = new ListViewItemGuarders();
        listViewItemGuarders.setTvName("지리구요친구");
        listViewItemGuarders.setTvPhone("01083832562");
        listViewItemGuarders.setUse(false);
        alGuarders.add(listViewItemGuarders);

        listViewItemGuarders = new ListViewItemGuarders();
        listViewItemGuarders.setTvName("지리구요");
        listViewItemGuarders.setTvPhone("01083832562");
        listViewItemGuarders.setUse(false);
        alGuarders.add(listViewItemGuarders);

        listViewItemGuarders = new ListViewItemGuarders();
        listViewItemGuarders.setTvName("오졌구요");
        listViewItemGuarders.setTvPhone("01099991111");
        listViewItemGuarders.setUse(false);
        alGuarders.add(listViewItemGuarders);
    }


    // 1. ArrayList에 있는 것을 저장.
    // 2. ArrayList을 받은 것을 셋팅.
}
