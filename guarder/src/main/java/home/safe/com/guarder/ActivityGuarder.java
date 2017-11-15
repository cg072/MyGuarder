package home.safe.com.guarder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityGuarder extends AppCompatActivity implements ListViewAdapterSearch.ListBtnClickListener, ListViewAdapterGuarders.ListBtnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guarder);

        ListView lvSearch;
        ListView lvGuarders;
        ListViewAdapterSearch lvAdapterSearch;
        ListViewAdapterGuarders lvAdapterGuarders;
        ArrayList<ListViewItemSearch> alSearch = new ArrayList<ListViewItemSearch>();
        ArrayList<ListViewItemGuarders> alGuarders = new ArrayList<ListViewItemGuarders>();

        // alSearch의 아이템들을 Load
        loadItemsFromDB2(alSearch);  // <- 검색버튼을 눌렀을 시에 적용이 되어야한다.
        loadItemsFromDB(alGuarders);

        // Adapter 생성 (implements ListViewAdapterSearch.ListBtnClickListener를 하였기때문에, 마지막에 this해도 오류 안남)
        lvAdapterSearch = new ListViewAdapterSearch(this, R.layout.listview_item_search, alSearch, this);
        lvAdapterGuarders = new ListViewAdapterGuarders(this, R.layout.listview_item_guarders, alGuarders, this);

        // 리스트뷰 참조 및 Adapter 달기
        lvSearch = (ListView) findViewById(R.id.lvSearch);
        lvSearch.setAdapter(lvAdapterSearch);
        lvGuarders = (ListView) findViewById(R.id.lvGuarders);
        lvGuarders.setAdapter(lvAdapterGuarders);

        // lvSearch에 클릭 이벤트 핸들러 정의
        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 아이템 클릭시 해야할 행동
            }
        });

        lvGuarders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 아이템 클릭시 해야할 행동
            }
        });
    }

    // ArrayList에 데이터를 로드하는 loadItemsFromDB() 함수를 추가
    // 현재 내용은 직접 DB를 다루진 않고 리스트에 값을 저장해서 리턴시킴킴
   public boolean loadItemsFromDB2(ArrayList<ListViewItemSearch> list) {
        ListViewItemSearch lvItemSearch;
        int desc ;

        if(list == null) {
            list = new ArrayList<ListViewItemSearch>();
        }

        // 순서를 위한 desc값을 1로 초기화.
        desc = 1;

        // 아이템 생성.
        lvItemSearch = new ListViewItemSearch();
        lvItemSearch.setTvName("김종하");
        lvItemSearch.setTvPhone("01072058516");
        list.add(lvItemSearch);
        desc++;

        // 아이템 생성.
        lvItemSearch = new ListViewItemSearch();
        lvItemSearch.setTvName("김진복");
        lvItemSearch.setTvPhone("010알아몰라");
        list.add(lvItemSearch);
        desc++;

        // 아이템 생성.
        lvItemSearch = new ListViewItemSearch();
        lvItemSearch.setTvName("박준규");
        lvItemSearch.setTvPhone("010몰라알아아");
        list.add(lvItemSearch);
        desc++;

        // 아이템 생성.
        lvItemSearch = new ListViewItemSearch();
        lvItemSearch.setTvName("경창현");
        lvItemSearch.setTvPhone("010몰라몰라");
        list.add(lvItemSearch);
        desc++;

        return true;
    }

    public boolean loadItemsFromDB(ArrayList<ListViewItemGuarders> list) {
        ListViewItemGuarders lvItemGuarders;
        int desc ;

        if(list == null) {
            list = new ArrayList<ListViewItemGuarders>();
        }

        // 순서를 위한 desc값을 1로 초기화.
        desc = 1;

        // 아이템 생성.
        lvItemGuarders = new ListViewItemGuarders();
        lvItemGuarders.setTvName("김종하");
        lvItemGuarders.setTvPhone("01072058516");
        list.add(lvItemGuarders);
        desc++;

        // 아이템 생성.
        lvItemGuarders = new ListViewItemGuarders();
        lvItemGuarders.setTvName("김진복");
        lvItemGuarders.setTvPhone("010알아몰라");
        list.add(lvItemGuarders);
        desc++;

        // 아이템 생성.
        lvItemGuarders = new ListViewItemGuarders();
        lvItemGuarders.setTvName("박준규");
        lvItemGuarders.setTvPhone("010몰라알아아");
        list.add(lvItemGuarders);
        desc++;

        // 아이템 생성.
        lvItemGuarders = new ListViewItemGuarders();
        lvItemGuarders.setTvName("경창현");
        lvItemGuarders.setTvPhone("010몰라몰라");
        list.add(lvItemGuarders);
        desc++;

        return true;
    }

    @Override
    public void onListBtnClick(int position) {
        Toast.makeText(this, Integer.toString(position+1) + "아이템이 선택되었습니다.", Toast.LENGTH_SHORT).show();
    }
}
