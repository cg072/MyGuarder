package home.safe.com.myguarder;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.util.ArrayList;

public class MainActivity extends ProGuardian implements IProGuardian {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public ArrayList searchList(ContentValues contentValues) {
        return null;
    }

    @Override
    public int modify(ContentValues contentValues) {
        return 0;
    }

    @Override
    public int insert(ContentValues contentValues) {
        return 0;
    }

    @Override
    public int remove(ContentValues contentValues) {
        return 0;
    }
    // 나는야 종하찡
}

