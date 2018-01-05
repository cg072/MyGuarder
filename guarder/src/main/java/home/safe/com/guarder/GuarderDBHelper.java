package home.safe.com.guarder;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by hotki on 2017-12-26.
 */

public class GuarderDBHelper extends ProGuardianDBHelper {

    SQLiteDatabase sqLiteDB;
    final static private String TABLE_NAME = "guarderlist";
    final static private String NAME = "gmcname";
    final static private String PHONE = "gmcphone";
    final static private String USE = "gstate";

    public GuarderDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, int table) {
        super(context, name, factory, version, table);
        Log.v("디비", "생성");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        super.onCreate(sqLiteDatabase);

        this.sqLiteDB = sqLiteDatabase;
        Log.v("디비", "크레이트");
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
                NAME + " TEXT," +
                PHONE + " TEXT," +
                USE + " INTEGER );";
        try {
            sqLiteDB.execSQL(sqlCreate);
        } catch (Exception e) {
            Log.v("디비", "크레이트 Error");
            Log.v("디비", e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        super.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
        Log.v("디비", "업그레이드");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        this.sqLiteDB = db;
        Log.v("디비", "오픈");
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
                NAME + " TEXT," +
                PHONE + " TEXT," +
                USE + " INTEGER );";

        try {
            sqLiteDB.execSQL(sqlCreate);
        } catch (Exception e) {
            Log.v("디비", "오픈 Error");
            Log.v("디비", e.getMessage());
        }
    }

    @Override
    public int insert(ContentValues contentValues) {

        sqLiteDB = getWritableDatabase();

        // DB에 입력한 값으로 행 추가
        int check = (int)sqLiteDB.insert(TABLE_NAME, null, contentValues);

        return check;
    }

    @Override
    public List<ContentValues> search(ContentValues contentValues) {
        Log.v("DB","Search 진입");
        sqLiteDB = getWritableDatabase();

        List<ContentValues> list = new ArrayList<>();
        ContentValues cv;

        String sqlSearch = "SELECT * FROM " + TABLE_NAME ;

        switch (contentValues.get("type").toString()) {

            case "all":
                Log.v("DB", "Search all 진입");
                break;
            case "part":
                Log.v("DB", "Search part 진입");
                // 아직 미구현 내용(부분 검색)
                sqlSearch = "SELECT * FROM " + TABLE_NAME + " WHERE " + USE + " = " + (int)(contentValues.get(USE));
                break;
        }

        Cursor cursor = sqLiteDB.rawQuery(sqlSearch, null);
        while (cursor.moveToNext()) {
            cv = new ContentValues();

            cv.put(NAME, cursor.getString(0));
            cv.put(PHONE, cursor.getString(1));
            cv.put(USE, cursor.getInt(2));

            list.add(cv);
        }

        return list;
    }

    @Override
    public int update(ContentValues contentValues) {
        Log.v("DB","Update 진입");
        sqLiteDB = getWritableDatabase();
        Log.v("DB","Update 진입");
        String name = String.valueOf(contentValues.get(NAME));
        String phone = String.valueOf(contentValues.get(PHONE));

        int check = sqLiteDB.update(TABLE_NAME, contentValues, "gmcname = ?" , new String[]{name});

        return check;
    }

    @Override
    public int remove(ContentValues contentValues) {
        Log.v("DB","Update 진입");
        sqLiteDB = getWritableDatabase();

        String name = String.valueOf(contentValues.get(NAME));
        String phone = String.valueOf(contentValues.get(PHONE));

        int check = sqLiteDB.delete(TABLE_NAME, "gmcname = ? && gmcphone = ?", new String[]{name, phone});

        return check;
    }
}
