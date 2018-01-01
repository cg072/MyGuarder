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

    public GuarderDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, int table) {
        super(context, name, factory, version, table);
        Log.v("DB", "슈퍼");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        super.onCreate(sqLiteDatabase);

        this.sqLiteDB = sqLiteDatabase;

        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
                "gseq INTEGER PRIMARY KEY AUTOINCREMENT," +
                "gmcname TEXT," +
                "gmcphone TEXT," +
                "gstate INTEGER );";

        try {
            sqLiteDB.execSQL(sqlCreate);
        } catch (Exception e){
            Log.v("DB", "생성완료");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        super.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public int insert(ContentValues contentValues) {

        int check = 0;

        sqLiteDB = getWritableDatabase();

        try {
            // DB에 입력한 값으로 행 추가
            sqLiteDB.execSQL("INSERT INTO " + TABLE_NAME + " VALUES( null, '" +
                    contentValues.get("gmcname").toString() + "', " +
                    contentValues.get("gmcphone").toString() + ", '" +
                    Integer.parseInt(contentValues.get("gstate").toString()) + "');");
            sqLiteDB.close();

        } catch (Exception e) {
            check = 1;
        }

        return check;
    }

    @Override
    public List<ContentValues> search(ContentValues contentValues) {
        Log.v("DB","서치");
        sqLiteDB = getWritableDatabase();

        List<ContentValues> list = new ArrayList<>();

        ContentValues cv;

        Log.v("DB","진입");

        switch (contentValues.get("type").toString()) {

            case "all":
                Log.v("DB", "ALL확인");
                String sqlSearch = "SELECT * FROM " + TABLE_NAME + ";";
                Cursor cursor = null;
                try{
                    cursor = sqLiteDB.rawQuery(sqlSearch, null);
                    cursor.moveToFirst();
                } catch (Exception e) {
                    Log.v("오류", e.getMessage());
                }
                //int count = cursor.getCount();
                //Log.v("DB", String.valueOf(count));
                try {
                    while (cursor.moveToNext()) {
                        cv = new ContentValues();

                        cv.put("gseq", cursor.getInt(0));
                        cv.put("gmcname", cursor.getString(1));
                        cv.put("gmcphone", cursor.getString(2));
                        cv.put("gstate", cursor.getInt(3));

                        list.add(cv);
                    }
                } catch (Exception e) {
                    Log.v("오류", e.getMessage());
                    list = null;
                }
        }

        return list;
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
