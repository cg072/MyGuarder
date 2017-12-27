package home.safe.com.myguarder;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.List;

/**
 * Created by JINNY_ on 2017-12-26.
 */

public class MyGuarderDBHelper extends ProGuardianDBHelper{

    public MyGuarderDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, int table) {
        super(context, name, factory, version, table);
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
        //DB 생성
//        sqLiteDatabase.execSQL("CREATE TABLE location(" +
//                "lseq INTEGER PRIMARY KEY DEFAULT 0," +
//                "llat CHAR(12) DEFAULT 000.0000000," +
//                "llong CHAR(12) DEFAULT 000.0000000," +
//                "lday DATE," +
//                "ltime CHAR(6) DEFAULT 00000," +
//                "lmid VARCHAR(15) NOT NULL)");
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
