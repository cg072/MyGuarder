package home.safe.com.trans;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by plupin724 on 2017-11-30.
 * 이동수단 등록을 위한 프래그먼트 클래스 작성
 */

public class FragmentTransList extends Fragment {

    AdapterListTrans adapter;

    ListView lvtrans;

    ArrayList<TestListViewDTO> recvdto;

    TestListViewDTO aaa;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_translist, container, false);


        lvtrans = (ListView)rootview.findViewById(R.id.lvtrans);

        adapter = new AdapterListTrans();

        adapter.addItem(new TestListViewDTO("1", "택시", "개인"));
        adapter.addItem(new TestListViewDTO("2", "버스", "시외버스"));
        adapter.addItem(new TestListViewDTO("3", "대리", "대리"));


        lvtrans.setAdapter(adapter);

       return rootview;

    }

    //이 메소드에서 서버에서 셀렉트를 해 와야함
    //생각 해 볼 것 : 플래그먼트에서 이동수단내역으로 스와이프 했을 때, 셀렉트를 새로 해 와야함
    //AdapterFragTabTrans.java 와 충돌이 일어 나는지 생각해 봐야 함
    public void addItem(TestListViewDTO recvDto){


    }
}
