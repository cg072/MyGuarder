package home.safe.com.notice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityNotice extends AppCompatActivity implements View.OnClickListener{

    TextView tvSearSelect;
    EditText edSearText;
    ImageButton btnSearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notice);

        tvSearSelect = (TextView)findViewById(R.id.tvSearSelect);

        tvSearSelect.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

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
