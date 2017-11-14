package home.safe.com.trans;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

    /*2017. 11. 03
    author : 박준규
    흐름 : 이동수단 설정 -> 메인 -> 이동수단 내역 보기 click
    실행되는 동작 : 1. 이동수단 내역 보여주기
                   2. 확인(transListFinish) 버튼을 누르면 종료
                   3. 이동수단 관리 페이지라는 명확한 표시 해주기!!!!!!!
    */


public class ActivityTransListPopup extends AppCompatActivity implements View.OnClickListener{

    ListView transListView;
    Button transListFinish;

    ArrayList<TestListViewDTO> redto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_list_popup);

        transListView = (ListView) findViewById(R.id.transListView);
        transListFinish = (Button) findViewById(R.id.transListFinish);

        redto = (ArrayList<TestListViewDTO>)getIntent().getSerializableExtra("dtotest");

        //내부클래스로 만들어준 어댑터 인스턴스를 생성
        TransListAdapter adapter = new TransListAdapter();


        //어댑터에 메인엑티비티에서 인텐트로 받은 dto정보를 넣어줌
        for(TestListViewDTO test : redto){
            adapter.addItem(test);
        }


      /*  adapter.addItem(new TestListViewDTO("a", "b", "c"));*/

        //리스트뷰에 어댑터를 추가
        transListView.setAdapter(adapter);


        //이동수단내역의 확인버튼 이벤트
        transListFinish.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if(view == transListFinish){
            finish();
        }

    }

   /* 2017.11.12
    author 박준규
    *교통수단정보 리스트를 받을 어댑터 내부 클래스 생성  */

    class TransListAdapter extends BaseAdapter{

        //메인액티비티에서 받을 정보를 저장할 배열리스트
        ArrayList<TestListViewDTO> items = new ArrayList<TestListViewDTO>();


        public void addItem(TestListViewDTO dto){
            items.add(dto);

        }

        //ArrayList의 아이템 갯수를 확인하는 메소드
        @Override
        public int getCount() {
            return items.size();
        }

        //ArrayList의 아이템을 꺼내오는 메소드
        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        //getCount의 숫자만큼, getItem으로 아이템을 리턴하여 아이템에 해당하는 뷰를 얻어옴
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            TransItemView itemView = new TransItemView(getApplicationContext());


            TestListViewDTO dto = items.get(i);

            itemView.setSeq(dto.getNum());
            itemView.setType(dto.getType());
            itemView.setName(dto.getName());

            return  itemView;
        }

        @Override
        public CharSequence[] getAutofillOptions() {
            return new CharSequence[0];
        }
    }
}
