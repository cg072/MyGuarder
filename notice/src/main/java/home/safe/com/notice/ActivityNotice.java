package home.safe.com.notice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/*
* 2017. 11. 23
* author : 박준규
* 게시판을 위한 확장형 리스트뷰 메인 액티비티
* */


public class ActivityNotice extends AppCompatActivity implements View.OnClickListener{

    TextView tvSearSelect;
    EditText edSearText;
    ImageButton btnSearButton;
    ExpandableListView elvNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notice);

        //검색 분류를 위한 버튼을 로딩
        tvSearSelect = (TextView)findViewById(R.id.tvSearSelect);

        //클릭이벤트리스너
        tvSearSelect.setOnClickListener(this);

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


        //어댑터 세팅
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
                    }
                    else if(item.getItemId() == R.id.itemNoticeTitle){
                        tvSearSelect.setText("제목");
                    }
                    else if(item.getItemId() == R.id.itemNoticeCont){
                        tvSearSelect.setText("내용");
                    }

                    return false;
                }
            });

            popupSearSelect.show();
        }
    }


}
