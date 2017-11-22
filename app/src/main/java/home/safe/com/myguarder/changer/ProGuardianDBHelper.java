package home.safe.com.myguarder.changer;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by 김진복 on 2017-11-21.
 * DBHelper 객체 부모
 * DBHelper 객체 만들때 상속
 */

abstract public class ProGuardianDBHelper extends SQLiteOpenHelper {
    final static public String DB_NAME = "safehome";
    final static public int DB_VERSION = 1;
    final static public int TABLE_MEMBER = 101;
    final static public int TABLE_GUARDER = 201;
    final static public int TABLE_LOCATION = 301;
    final static public int TABLE_NOTICE = 401;
    final static public int TABLE_TRANS = 501;
    final static public int TABLE_TEST = 1001;


    final private String T_MEMBER = "member";
    final private String T_GUARDER = "guarder";
    final private String T_LOCATION = "location";
    final private String T_NOTICE = "notice";
    final private String T_TRANS = "transportation";
    final private String T_TEST = "test";

    private String table_name;

/*
    public ProGuardianDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
*/

    public ProGuardianDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, int table) {
        super(context, name, factory, version);
        tableSelector(table);
    }

    public ProGuardianDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler, int table) {
        super(context, name, factory, version, errorHandler);
        tableSelector(table);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //oldVersion 이면 newVersion으로 업그레이드
    }

    public String getTableName() {
        return table_name;
    }

    private void setTableName(String tablename) {
        this.table_name = tablename;
    }

    private void tableSelector(int table) {
        switch (table)
        {
            case TABLE_MEMBER:
                table_name = T_MEMBER;
                break;
            case TABLE_GUARDER:
                table_name = T_GUARDER;
                break;
            case TABLE_LOCATION:
                table_name = T_LOCATION;
                break;
            case TABLE_NOTICE:
                table_name = T_NOTICE;
                break;
            case TABLE_TRANS:
                table_name = T_TRANS;
                break;
            default:
                table_name = T_TEST;
                break;
        }

    }

    abstract public int insert(ContentValues contentValues);

    abstract public List<ContentValues> search(ContentValues contentValues);

    abstract public int update(ContentValues contentValues);

    abstract public int remove(ContentValues contentValues);

}
