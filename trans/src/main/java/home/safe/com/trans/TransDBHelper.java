package home.safe.com.trans;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.List;

/**
 * Created by plupin724 on 2017-12-27.
 */

public class TransDBHelper extends ProGuardianDBHelper{

    final private String TABLE_NAME = getTableName();
    private SQLiteDatabase db;
    //private String transCol[] = {"a", "b", "c"};


    public TransDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, int table) {
        super(context, name, factory, version, table);
    }

    public TransDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler, int table) {
        super(context, name, factory, version, errorHandler, table);
    }

    /*@Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql =  "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +


    }*/

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        super.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }


    @Override
    public String getTableName() {
        return super.getTableName();
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
