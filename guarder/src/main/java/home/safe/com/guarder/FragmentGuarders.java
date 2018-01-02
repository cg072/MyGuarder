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

    final static String TAG = "MEMBER";

    private ListViewAdapterGuarders lvAdapterGuarders;
    private ListView lvGuarders;
    private ArrayList<ListViewItemGuarders> alGuarders;
    private ViewGroup rootView;
    private ListViewItemGuarders lvItemGuarders;
    private String nowGuarderName;
    private String nowGuarderPhone;
    private int shareNumber = 0;
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
        Log.v("지킴이 크레이트뷰","진입");
        String title = "크레이트 뷰";
        guarderAdapterUpdate(title);
    }

    // 지킴이 목록의 어댑터 갱신
    private void guarderAdapterUpdate(String title) {
        Log.v("지킴이 어댑터 업뎃", title);
        // Adapter 생성 (implements ListViewAdapterSearch.ListBtnClickListener를 하였기때문에, 마지막에 this해도 오류 안남)
        Log.v("지킴이", "어댑터 갱신 진입");
        if(rootView == null) {
            Log.v("지킴이 어댑터", "루트뷰 == null");
        } else if(rootView.getContext() == null) {
            Log.v("지킴이 어댑터", "겟콘 == null");
        } else if (alGuarders == null){
            Log.v("지킴이 어댑터", "리스트 == null");
        }
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
        sendCV.put("gmcname", name);
        sendCV.put("gmcphone", phone);
        sendCV.put("gstate", 0);

        check = guaderController.insert(sendCV);

        alGuarders.add(lvItemGuarders);

        Collections.sort(alGuarders, new NameDescCompareGuarders());

        String title = "지킴이 추가";
        guarderAdapterUpdate(title);
        /*if (check == 0) {
            alGuarders.add(lvItemGuarders);

            Collections.sort(alGuarders, new NameDescCompareGuarders());

            guarderAdapterUpdate();
        }*/

        Log.v("지킴이 추가","체크값 : " + String.valueOf(check));
        // 역방향 시 Collections.reverse 로 해주면 된다
    }

    public void setList(ArrayList<ListViewItemGuarders> list) {
        String title = "지킴이 셋";
        Log.v("지킴이 셋","진입");
        alGuarders = list;
        if (list != null) {
            Log.v("지킴이 셋", "널아님");
            guarderAdapterUpdate(title);
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

                listViewItemGuarders.setTvName(contentValues.get("gmcname").toString());
                listViewItemGuarders.setTvPhone(contentValues.get("gmcphone").toString());
                if (contentValues.get("gstate").toString().equals("1")) {
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
