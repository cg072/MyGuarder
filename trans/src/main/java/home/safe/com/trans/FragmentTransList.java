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

import java.util.ArrayList;

/**
 * Created by plupin724 on 2017-11-30.
 * 이동수단 등록을 위한 프래그먼트 클래스 작성
 */

public class FragmentTransList extends Fragment {

    ListAdapterTrans adapter;

    ListView lvtrans;

    ArrayList<TestListViewDTO> recvdto;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_translist, container, false);


        lvtrans = (ListView)rootview.findViewById(R.id.lvtrans);

        adapter = new ListAdapterTrans();

        adapter.addItem(new TestListViewDTO("1", "택시", "개인"));
        adapter.addItem(new TestListViewDTO("2", "버스", "시외버스"));
        adapter.addItem(new TestListViewDTO("3", "대리", "대리"));


        lvtrans.setAdapter(adapter);

       return rootview;

    }

    public void addItem(String num, String type, String name){



    }
}
