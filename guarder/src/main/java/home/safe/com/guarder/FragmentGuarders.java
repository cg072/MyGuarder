package home.safe.com.guarder;

import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragmentGuarders extends Fragment implements ListViewAdapterGuarders.GuardersListBtnClickListener {

    // 지킴이로 설정되어있다면 use = 1
    // 지킴이로 설정되어있지 않다면 use = 0
    final static String TAG = "MEMBER";

    private ListViewAdapterGuarders lvAdapterGuarders;
    private ListView lvGuarders;
    private ArrayList<ListViewItemGuarders> alGuarders;
    private ViewGroup rootView;
    private ListViewItemGuarders lvItemGuarders;
    private int shareNumber = 0;
    final static String COL_NAME = "gmcname";
    final static String COL_PHONE = "gmcphine";
    final static String COL_USE = "update";
    int a = 0;

    GuaderController guaderController = new GuaderController();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_guarders, container, false);

        lvGuarders = (ListView) rootView.findViewById(R.id.lvGuarders);
        alGuarders = new ArrayList<ListViewItemGuarders>();
        Log.v("지킴이", "최초 어댑터1");
        if(loadDB() != null) {
            Log.v("셋리스트", "시작");
            setList(loadDB());
            Log.v("셋리스트", "끝");
        }

        lvAdapterGuarders = new ListViewAdapterGuarders(rootView.getContext(), R.layout.listview_item_search, alGuarders, this);
        lvGuarders.setAdapter(lvAdapterGuarders);

        return rootView;
    }


    // 지킴이 리스트에서 슬라이딩 버튼을 눌렀을 시
    /*
    *  date     : 2017.11.20
    *  author   : Kim Jong-ha
    *  title    : onGuardersListBtnClick(int position, int count) 메소드 생성
    *  comment  : 지킴이 목록에 있는 SlidingButton을 눌렀을 경우의 작동 코딩
    *             지킴이는 1명만 지정이 가능하기 때문에, 지킴이로 지정된 ListView의 item을 제외하고는 모두 Button 상태가 false가 되어야한다.
    */
    @Override
    public void onGuardersListBtnClick(int position, int count) {

        String preGuarderName = "";
        String preGuarderPhone = "";
        boolean preGuarderUse = false;
        int preGuarderNumber;

        String postGuarderName = alGuarders.get(position).getTvName();
        String postGuarderPhone = alGuarders.get(position).getTvPhone();
        boolean postGuarderUse = alGuarders.get(position).getUse();

        int preCheck = 0;
        int postCheck = 0;

        // count는 지킴이 목록의 숫자를 의미
        for( int i = 0 ; i < count ; i++ ) {

            if ((i != position) && (alGuarders.get(i).getUse() == true)) {
                // 해당 position이 아닌데 true라면 button을 비활성화 시킨다
                alGuarders.get(i).setUse(false);

                preGuarderName = alGuarders.get(i).getTvName();
                preGuarderPhone = alGuarders.get(i).getTvPhone();
                preGuarderUse = alGuarders.get(i).getUse();

                preCheck = sendToDB(preGuarderName, preGuarderPhone, preGuarderUse);

                // update가 제대로 안되었다면
                if (preCheck == 0) {
                    alGuarders.get(i).setUse(true);
                }
                break;
            }
        }

        if(preCheck != 0) {
            // Button Off 상태 -> On
            if (postGuarderUse == false) {
                // 해당 position이라면 button을 활성화 시키고, name, phone을 가져온다.
                alGuarders.get(position).setUse(true);
                Toast.makeText(rootView.getContext(), postGuarderName + " 님이 지킴이로 설정되었습니다.", Toast.LENGTH_SHORT).show();

            // Button On 상태 -> Off
            } else {
                alGuarders.get(position).setUse(false);
                Toast.makeText(rootView.getContext(), "현재 지킴이가 없습니다.", Toast.LENGTH_SHORT).show();
            }
            postCheck = sendToDB(postGuarderName, postGuarderPhone, postGuarderUse);
        }

        if ( postCheck == 0 ){
            // 이전 지킴이 설정에 오류가 있거나, 이후 지킴이 설정에 오류가 있을때~
            alGuarders.get(position).setUse(postGuarderUse);
            Toast.makeText(rootView.getContext(), "지킴이 설정에 오류가 있습니다. Error: " + String.valueOf(preCheck) + " Code : " + String.valueOf(postCheck), Toast.LENGTH_SHORT).show();
        }

        // button을 누름으로써, 변경된 내역을 기반으로 지킴이 목록 갱신
        guarderAdapterUpdate();
    }

    private boolean sendToServer(String name, String phone) {
        boolean check = true;

        return check;
    }

    private int sendToDB(String name, String phone, boolean use) {

        int useInteger = 0;

        if(use == true) {
            useInteger = 1;
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_NAME, name);
        contentValues.put(COL_PHONE, phone);
        contentValues.put(COL_USE, useInteger);

        return guaderController.update(contentValues);
    }

    // 지킴이 목록의 어댑터 갱신
    private void guarderAdapterUpdate() {
        Log.v("지킴이", "어댑터 갱신 진입");
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
    public void guarderAdd(String name, String phone) {
        Log.v("지킴이 추가","진입");
        int check = 0;

        lvItemGuarders = new ListViewItemGuarders();
        lvItemGuarders.setTvName(name);
        lvItemGuarders.setTvPhone(phone);
        lvItemGuarders.setUse(false);


        ContentValues sendCV = new ContentValues();
        sendCV.put(COL_NAME, name);
        sendCV.put(COL_PHONE, phone);
        sendCV.put(COL_USE, 0);

        check = guaderController.insert(sendCV);

        if(check == 1) {
            alGuarders.add(lvItemGuarders);
            Collections.sort(alGuarders, new NameDescCompareGuarders());
            String title = "지킴이 추가";
            guarderAdapterUpdate();
        }
    }

    public void setList(ArrayList<ListViewItemGuarders> list) {
        String title = "지킴이 셋";
        Log.v("지킴이 셋","진입");
        alGuarders = list;
        if (list != null) {
            Log.v("지킴이 셋", "널아님");
            guarderAdapterUpdate();
        } else {
            Log.v("지킴이 셋", "널");
        }
    }

    public ArrayList<ListViewItemGuarders> getList() {
        return alGuarders;
    }

    private ArrayList<ListViewItemGuarders> loadDB() {
        ContentValues sendCV = new ContentValues();
        sendCV.put("type", "all");

        List<ContentValues> resultList = new ArrayList<>();
        resultList = guaderController.search(sendCV);

        ArrayList<ListViewItemGuarders> guaderlist = new ArrayList<ListViewItemGuarders>();
        ListViewItemGuarders listViewItemGuarders = new ListViewItemGuarders();

        if( resultList != null) {
            for (ContentValues contentValues : resultList) {

                listViewItemGuarders = new ListViewItemGuarders();

                listViewItemGuarders.setTvName(contentValues.get(COL_NAME).toString());
                listViewItemGuarders.setTvPhone(contentValues.get(COL_PHONE).toString());
                if (contentValues.get(COL_USE).toString().equals("1")) {
                    listViewItemGuarders.setUse(true);
                } else {
                    listViewItemGuarders.setUse(false);
                }

                guaderlist.add(listViewItemGuarders);
            }
        } else {
            guaderlist = null;
        }
        return guaderlist;
    }

    // 1. ArrayList에 있는 것을 저장.
    // 2. ArrayList을 받은 것을 셋팅.
}
