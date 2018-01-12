package home.safe.com.guarder;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hotki on 2017-12-26.
 */

public class GuarderDBHelper extends ProGuardianDBHelper {

    final static String TAG = "DBHelpler";

    SQLiteDatabase sqLiteDB;
    final static private String TABLE_NAME = "guarderlist";
    final static private String SEQ = "gseq";
    final static private String NAME = "gmcname";
    final static private String PHONE = "gmcphone";
    final static private String USE = "gstate";
    final static private String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
            SEQ+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            NAME + " TEXT," +
            PHONE + " TEXT," +
            USE + " INTEGER );";

    public GuarderDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, int table) {
        super(context, name, factory, version, table);
        Log.v("DBHelpler", "생성");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        super.onCreate(sqLiteDatabase);
        Log.v("DBHelpler", "온크레이트");
        this.sqLiteDB = sqLiteDatabase;
        sqLiteDB.execSQL(SQL_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        super.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
        Log.v("DBHelpler", "업그레이드");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);         // 기존의 테이블 삭제 후
        onCreate(sqLiteDatabase);                                              // 새 디비를 만들어준다.
        // 데이터 백업은 따로 짜야하는듯?
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        this.sqLiteDB = db;
        Log.v("DBHelpler", "오픈");

        try {
            sqLiteDB.execSQL(SQL_CREATE);
        } catch (Exception e) {
            Log.v("DBHelpler", "오픈 Error");
            Log.v("DBHelpler", e.getMessage());
        }
    }

    @Override
    public int insert(ContentValues contentValues) {

        sqLiteDB = getWritableDatabase();

        // DB에 입력한 값으로 행 추가
        int check = (int)sqLiteDB.insert(TABLE_NAME, NAME + "," + PHONE + "," + USE, contentValues);

        return check;
    }

    @Override
    public List<ContentValues> search(ContentValues contentValues) {
        Log.v("DBHelpler","Search 진입");
        sqLiteDB = getWritableDatabase();

        List<ContentValues> list = new ArrayList<>();
        ContentValues cv;

        String sqlSearch = "SELECT * FROM " + TABLE_NAME ;

        switch (contentValues.get("type").toString()) {

            case "all":
                Log.v("DBHelpler", "Search all 진입");
                break;
            case "part":
                Log.v("DBHelpler", "Search part 진입");
                // 아직 미구현 내용(부분 검색)
                sqlSearch += " WHERE " + USE + " = " + (int)(contentValues.get(USE));
                break;
        }

        Cursor cursor = sqLiteDB.rawQuery(sqlSearch, null);
        while (cursor.moveToNext()) {
            cv = new ContentValues();

            cv.put(SEQ, cursor.getInt(0));
            cv.put(NAME, cursor.getString(1));
            cv.put(PHONE, cursor.getString(2));
            cv.put(USE, cursor.getInt(3));

            list.add(cv);
        }

        return list;
    }

    @Override
    public int update(ContentValues contentValues) {
        Log.v("DBHelpler","Update 진입");
        sqLiteDB = getWritableDatabase();
        Log.v("DBHelpler","Update 진입");

        String name = String.valueOf(contentValues.get(NAME));
        String phone = String.valueOf(contentValues.get(PHONE));
        String use = String.valueOf(contentValues.get(USE));
        Log.v("DBHelpler",name);
        Log.v("DBHelpler",phone);
        Log.v("DBHelpler",String.valueOf(use));
        int check = sqLiteDB.update(
                TABLE_NAME,
                contentValues,
                NAME + " = ? and " + PHONE +" = ? " ,
                new String[]{name, phone});
        Log.v("DBHelpler",String.valueOf(check));
        return check;
    }

    @Override
    public int remove(ContentValues contentValues) {
        Log.v("DBHelpler","Remove 진입");
        sqLiteDB = getWritableDatabase();

        String name = String.valueOf(contentValues.get(NAME));
        String phone = String.valueOf(contentValues.get(PHONE));

        int check = sqLiteDB.delete(TABLE_NAME, "gmcname = ? and gmcphone = ?", new String[]{name, phone});

        return check;
    }
}
