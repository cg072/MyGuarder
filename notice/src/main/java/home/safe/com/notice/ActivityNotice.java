package home.safe.com.notice;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/*
* 2017. 11. 23
* author : 박준규
* 게시판을 위한 확장형 리스트뷰 메인 액티비티
* */

/*
* 해야할일 :
* 1. 디자인 변경!!!
* 2. 디비에서 정보 받아오기!!!
* 3. text라인 막아야함!!!
* 4. 공지사항 하나를 클릭하면 다른 공지사항들은 접혀야 함!!
* 5. 작성자를 데이트로 바꾸어서 저장해야 함!! */


public class ActivityNotice extends AppCompatActivity implements View.OnClickListener{

    TextView tvSearSelect;
    EditText etSearText;
    ImageButton btnSearButton;
    ExpandableListView elvNotice;
    int stat;
    ArrayList<TestDtoNotice> plusItem = new ArrayList<>();
    ArrayList<TestDtoNotice> resultitem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notice);

        //검색 분류를 위한 버튼을 로딩
        tvSearSelect = (TextView)findViewById(R.id.tvSearSelect);

        //검색어를 입력하는 EditText를 로딩
        etSearText = (EditText)findViewById(R.id.etSearText);

        //검색을 위한 이미지버튼을 로딩
        btnSearButton = (ImageButton)findViewById(R.id.btnSearButton);

        //클릭이벤트리스너
        tvSearSelect.setOnClickListener(this);
        btnSearButton.setOnClickListener(this);

        //리스트뷰를 보여줄 영역의 아이디를 호출
        elvNotice = (ExpandableListView)findViewById(R.id.elvNotice);

        //어댑터 생성
        AdapterNotice adapter = new AdapterNotice();

        //어댑터에 데이터를 넣어줌
        Log.v("됩니까", "돼요?");
        adapter.addItem(new TestDtoNotice("111", "aaa", "bbb"));
        adapter.addItem(new TestDtoNotice("222", "ccc", "ddd"));
        adapter.addItem(new TestDtoNotice("333", "eee", "fff"));
        adapter.addItem(new TestDtoNotice("444", "ggg", "hhh"));
        adapter.addItem(new TestDtoNotice("555", "iii", "jjj"));


        //어댑터 전송
        elvNotice.setAdapter(adapter);

    }



    @Override
    public void onClick(View view) {

        //검색종류 클릭시
        if(view == tvSearSelect){
            PopupMenu popupSearSelect = new PopupMenu(this, view);
            MenuInflater menuInflater = popupSearSelect.getMenuInflater();
            Menu menu = popupSearSelect.getMenu();
            menuInflater.inflate(R.menu.menu_notice_select, menu);
            popupSearSelect.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if(item.getItemId() == R.id.itemNoticeAll){
                        tvSearSelect.setText("전체");
                        stat = 0;
                    }
                    else if(item.getItemId() == R.id.itemNoticeTitle){
                        tvSearSelect.setText("제목");
                        stat = 1;
                    }
                    else if(item.getItemId() == R.id.itemNoticeCont){
                        tvSearSelect.setText("내용");
                        stat = 2;
                    }

                    return false;
                }
            });

            popupSearSelect.show();
        }


        //검색아이콘 클릭시
        if(view == btnSearButton){


            Toast.makeText(getApplicationContext(), "클릭됨요", Toast.LENGTH_LONG).show();

        }
    }


    /*
    * 2017. 11. 23
    * author : 박준규
    * 어뎁터를 처리하고 리스트뷰를 생성하는 내부 어댑터 클래스
    * */


    public class AdapterNotice extends BaseExpandableListAdapter {

        ArrayList<TestDtoNotice> items = new ArrayList<TestDtoNotice>();
        HashMap<TestDtoNotice, String> hash = new HashMap<>();

        //메인액티비티에서 보낸 정보를 어레이리스트에 추가
        public void addItem(TestDtoNotice test){

            Log.v("ㅇㅇ?", "ㅇㅇ?");

            items.add(test);

            hash.put(test, test.getTestcontents());

        }


        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return 1;
        }

        @Override
        public Object getGroup(int i) {
            return items.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {

            return items.get(i).getTestcontents();
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

            ListViewItemTitle itemTitle = new ListViewItemTitle(getApplicationContext());

            TestDtoNotice dto = items.get(i);

            itemTitle.setTitle(dto.getTesttitle(), dto.getTestauthor());

            return itemTitle;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

            ListViewItemContents itemContents = new ListViewItemContents(getApplicationContext());

            TestDtoNotice dto = items.get(i);

            itemContents.setContents(dto.getTestcontents());


            return itemContents;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }
    }


}
