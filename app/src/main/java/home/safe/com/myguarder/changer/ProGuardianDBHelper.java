package home.safe.com.myguarder.changer;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by 김진복 on 2017-11-21.
 * DBHelper 부모 객체
 * 상속받기
 * git remote set-url origin https://github.com/cg072/MyGuarder.git
 */

public class ProGuardianDBHelper extends SQLiteOpenHelper {
    final static public String DB_NAME = "safehome";
    final static public int DB_VERSION = 1;
    final static public int TABLE_MEMBER = 101;
    final static public int TABLE_GUARDER = 201;
    final static public int TABLE_LOCATION = 301;
    final static public int TABLE_NOTICE = 401;
    final static public int TABLE_TRANS = 501;

    final private String T_MEMBER = "member";
    final private String T_GUARDER = "guarder";
    final private String T_LOCATION = "location";
    final private String T_NOTICE = "notice";
    final private String T_TRANS = "transportation";

    private String table_name;
    private SQLiteDatabase db;

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
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int a, int b) {

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
                table_name = null;
                break;
        }

    }

    public int insert(ContentValues contentValues) {
        db = getWritableDatabase();
        //테이블명, 컬럼명, 컬럼값
        db.insert(table_name, null, contentValues);
        return 0;
    }

    public List<ContentValues> search(ContentValues contentValues) {
        db = getReadableDatabase();
        String[] conditions = {"컬럼1=값1", "컬럼2=값2"};
        db.rawQuery("select * from " + table_name, conditions);

        return null;
    }

    public int update(ContentValues contentValues) {
        db = getWritableDatabase();
        String[] conditions = {"값1", "값2", "값3"};   //물음표 위치에 0번째 값부터 조건으로 들어감
        db.update(table_name, contentValues, "조건1=? and 조건2=? and 조건3=?", conditions);
        return 0;
    }

    public int remove(ContentValues contentValues) {
        db = getWritableDatabase();
        String[] conditions = {"contentValues값1", "contentValues값2", "contentValues값3"};   //물음표 위치에 0번째 값부터 조건으로 들어감
        db.delete(table_name, "조건1=? and 조건2=? and 조건3=?", conditions);
        return 0;
    }

}
