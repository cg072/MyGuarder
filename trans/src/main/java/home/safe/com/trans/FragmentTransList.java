package home.safe.com.trans;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.BinderThread;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by plupin724 on 2017-11-30.
 * 이동수단 등록을 위한 프래그먼트 클래스 작성
 */

//지킴이가 다수의 피지킴이의 내역을 볼 때, 피지킴이 마다 리스트가 따로 설정 되어야 한다.
//아이디를 가지고, 이름을 표시해주기
//창현이와 협의를 하여야 함
//최초 실행시 서버에서 정보를 받아 안드로이드 디비에 저장
//아이디를 가져와서 화면에 표시해주는 것까지 완성 해야 한다

public class FragmentTransList extends Fragment{

    AdapterListTrans adapter = new AdapterListTrans();

    ExpandableListView lvtrans;

    TestListViewDTO listDto = new TestListViewDTO();
    ArrayList<TestListViewDTO> listArrDto = new ArrayList<>();

    TransIntegratedVO integratedVO = new TransIntegratedVO();
    ArrayList<TransIntegratedVO> arrayList = new ArrayList<>();

    TransManager transManager;

    public FragmentTransList(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_translist, container, false);


        lvtrans = (ExpandableListView) rootview.findViewById(R.id.lvtrans);



        //adapter = new AdapterListTrans();

        /*adapter.addItem(new TestListViewDTO("1", "택시", "2017.05.1", "jdkd", "대중교통"));
        adapter.addItem(new TestListViewDTO("3", "택시", "2017.05.1", "jdkd", "대중교통"));*/

        lvtrans.setAdapter(adapter);



       return rootview;

    }

    //이 메소드에서 서버에서 셀렉트를 해 와야함
    //생각 해 볼 것 : 플래그먼트에서 이동수단내역으로 스와이프 했을 때, 셀렉트를 새로 해 와야함
    //AdapterFragTabTrans.java 와 충돌이 일어 나는지 생각해 봐야 함
    public void toServTransList(TestListViewDTO recvDto){


    }

    ////////////////서버 없이 동작 시켜 보기///////////////////////////

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(listArrDto != null) {
            for (TestListViewDTO data : listArrDto) {
                setAddAdapter(data);
            }
        }

        Log.v("어태치", "어태치");
    }*/



 /*   @Override
    public void onStart() {
        super.onStart();

        if(listArrDto != null) {
            for (TestListViewDTO data : listArrDto) {
                setAddAdapter(data);
            }
        }

    }*/


    //리스트 애드시에 숫자 불어나는 문제
    //코드 정리하기

    /*public void setFragList(TestListViewDTO dto){
        this.listDto = dto;

    }*/

    public void setArryDtoFragList(ArrayList<TestListViewDTO> recvArrDto){

        this.listArrDto = recvArrDto;
        Log.v("regArrDto" , "확인7");
        for(TestListViewDTO data : listArrDto){
            setAddAdapter(data);
        }

        listArrDto.clear();

    }

    public void setAddAdapter(TestListViewDTO recvDto){
        if(adapter == null) {
            return;
        }
        if(recvDto == null) {
            return;
        }
        //adapter.addItem(recvDto);
        adapter.notifyDataSetChanged();
    }


    /*public void setDtoFragList(TestListViewDTO test){

        listDto = test;

        this.listArrDto.add(listDto);

        for(TestListViewDTO data : listArrDto){
            setAddAdapter(data);
        }

        listArrDto.clear();

    }*/



    ///////////디비를 통해 세팅해보기//////////////////

    public void setTransManager(TransManager transManager){
        this.transManager = transManager;
    }


    public void setArrList(ArrayList<TransIntegratedVO> list){

        this.arrayList = list;
        adapter.clear();

        for(TransIntegratedVO data : arrayList){
            setListAdapter(data);
        }

        arrayList.clear();

    }

    public void setListAdapter(TransIntegratedVO data){
        if(adapter == null) {
            return;
        }
        if(data == null) {
            return;
        }
        adapter.addItem(data);
        adapter.notifyDataSetChanged();
    }

}


