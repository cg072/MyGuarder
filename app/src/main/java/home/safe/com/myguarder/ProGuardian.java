package home.safe.com.myguarder;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by hotki on 2017-11-01.
 */

public class ProGuardian extends AppCompatActivity {
    private SQLiteOpenHelper sqLiteOpenHelper;
    public static boolean loginCheck;

    public SQLiteOpenHelper getSqLiteOpenHelper() {
        return  sqLiteOpenHelper;
    }

    public void sendMessage(ContentValues contentValues) {
    }

    public ContentValues recvMessage() {
        ContentValues contentValues = new ContentValues();
        return contentValues;
    }

    public void sendEmergency(ContentValues contentValues) {

    }

    public boolean getCert(ContentValues contentValues) {
        boolean check = false;

        return check;
    }

    /**
     *
     * @author 경창현
     * @version 1.0.0
     * @text Menu 올리기 - action_bar 추가
     * @since 2017-11-08 오후 5:16
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);

        return true;
    }

    /**
     *
     * @author 경창현
     * @version 1.0.0
     * @text Menu 클릭시 처리 부분
     * @since 2017-11-08 오후 5:17
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        String txt = null;

        switch (id)
        {
            case R.id.action_setting:
                txt = "action_setting";
                break;
        }

        Toast.makeText(this,txt,Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }
}
