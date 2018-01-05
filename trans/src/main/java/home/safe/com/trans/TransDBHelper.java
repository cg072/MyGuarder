package home.safe.com.trans;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.List;

/**
 * Created by plupin724 on 2017-12-27.
 */

public class TransDBHelper extends ProGuardianDBHelper{

    private SQLiteDatabase db;
    final static private String TABLE_NAME = "transportation";
    private String transCol[] = {"rtseq", "rlseq", "rmemo", "rday", "rmid"};

    public TransDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, int table) {
        super(context, name, factory, version, table);
    }

    public TransDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler, int table) {
        super(context, name, factory, version, errorHandler, table);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.d("transDBHelper", "onOpen");
        this.db = db;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("DBhelper", "oncreat");
        db = sqLiteDatabase;
        String sql = "CREATE TABLE IF NOT EXISTS" +
                TABLE_NAME + "(" +
                transCol[0] + "INTEGER DEFAULT 0," +
                transCol[1] + "INTEGET DEFAULT 0," +
                transCol[2] + "TEXT DEFAULT 20," +
                transCol[3] + "DATE DEFAULT 20," +
                transCol[4] + "TEXT DEFAULT 20)";

        db.execSQL(sql);

    }

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
