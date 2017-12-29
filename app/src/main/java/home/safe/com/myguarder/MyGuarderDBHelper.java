package home.safe.com.myguarder;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.List;

/**
 * Created by JINNY_ on 2017-12-26.
 */

public class MyGuarderDBHelper extends ProGuardianDBHelper{

    final private String TABLE_NAME = getTableName();   //changer 생성시 테이블 관련 파라미터 참고
    private SQLiteDatabase db;
    private String myGuarderCol[] = {"lseq","llat","llong","lday","ltime","lid"};

    public MyGuarderDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, int table) {
        super(context, name, factory, version, table);
        Log.d("MyGuarderDBHelper", "생성자");
    }

    public MyGuarderDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler, int table) {
        super(context, name, factory, version, errorHandler, table);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("MyGuarderDBHelper", "테이블 생성");
        //DB 생성
        String sql = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                myGuarderCol[0] +" INTEGER PRIMARY KEY DEFAULT 0," +
                myGuarderCol[1] +" TEXT DEFAULT 000.0000000," +
                myGuarderCol[2] +" TEXT DEFAULT 000.0000000," +
                myGuarderCol[3] +" TEXT DEFAULT 000," +
                myGuarderCol[4] +" TEXT DEFAULT 00000," +
                myGuarderCol[5] +" TEXT NOT NULL)";
        //datetime, now() 없음

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        super.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }

    @Override
    public int insert(ContentValues contentValues) {
        return 0;
    }

    @Override
    public List<ContentValues> search(ContentValues contentValues) {
        return null;
    }

    @Override
    public int update(ContentValues contentValues) {
        return 0;
    }

    @Override
    public int remove(ContentValues contentValues) {
        return 0;
    }
}
