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
    private String transCol[] = {"tseq", "tlseq", "tid", "ttype", "tmemo", "tday"};

    public TransDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, int table) {
        super(context, name, factory, version, table);
        Log.d("디비헬퍼확인", Integer.toString(table));
    }

    public TransDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler, int table) {
        super(context, name, factory, version, errorHandler, table);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {

        //기존 디비가 있으면 onOpen으로 접근함

        super.onOpen(db);

        this.db = db;

        Log.d("transDBHelper", "onOpen");

        ///////////////////////오픈에 써봄///////////////////
        Log.d("DBhelper", "oncreate1111");
        db = getWritableDatabase();
        Log.d("sql시작", "시작");
        String sql = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                transCol[0] + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                transCol[1] + " INTEGER DEFAULT 0," +
                transCol[2] + " TEXT," +
                transCol[3] + " TEXT," +
                transCol[4] + " TEXT," +
                transCol[5] + " TEXT)";

        Log.d("sql문", sql);

        db.execSQL(sql);

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /*Log.d("DBhelper", "oncreate1111");
        db = getWritableDatabase();
        Log.d("sql시작", "시작");
        String sql = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                transCol[0] + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                transCol[1] + " INTEGER DEFAULT 0," +
                transCol[2] + " TEXT," +
                transCol[3] + " TEXT," +
                transCol[4] + " TEXT," +
                transCol[5] + " TEXT)";

        Log.d("sql문", sql);

        db.execSQL(sql);*/

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

        Log.d("transdbhelper", "dbhelperinsert");

        db = getWritableDatabase();

        ///컨텐트벨류에 모든 값이 세팅이 되어 있어야 함!!!!!
        //{"tseq", "tlseq", "tid", "ttype", "tmemo", "tday"};
        int check = (int)db.insert(TABLE_NAME, "tseq, tlseq, tid, ttype, tmemo, tday", contentValues);

        Log.d("transdbhelper", "dbhelperinsert22");

        return check;
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
    public int remove(ContentValues contentValues) { return 0;
    }

    public void removeTable(){
        Log.d("리무브", "리무브실행");
        db = getWritableDatabase();
        String sql = "DROP TABLE if exists " + TABLE_NAME;
        db.execSQL(sql);

        //onCreate(db);

    }

}
