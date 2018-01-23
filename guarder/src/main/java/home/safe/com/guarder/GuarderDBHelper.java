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
    String data[] = new String[4];

    public GuarderDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, int table) {
        super(context, name, factory, version, table);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        super.onCreate(sqLiteDatabase);
        createTableGuarder(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        super.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);         // 기존의 테이블 삭제 후
        onCreate(sqLiteDatabase);                                              // 새 디비를 만들어준다.
        // 데이터 백업은 따로 짜야하는듯?
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        createTableGuarder(db);
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
        sqLiteDB = getWritableDatabase();

        List<ContentValues> list = new ArrayList<>();
        ContentValues cv;

        String sqlSearch = "SELECT * FROM " + TABLE_NAME ;

        if (contentValues.get("type").toString().equals("con")) {
            sqlSearch += " WHERE " + USE + " = " + (int)(contentValues.get(USE));
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
        sqLiteDB = getWritableDatabase();

        String name = contentValues.getAsString(NAME);
        String phone = contentValues.getAsString(PHONE);
        String use = contentValues.getAsString(USE);
        int check = sqLiteDB.update(
                TABLE_NAME,
                contentValues,
                NAME + " = ? and " + PHONE +" = ? " ,
                new String[]{name, phone});
        return check;
    }

    @Override
    public int remove(ContentValues contentValues) {
        sqLiteDB = getWritableDatabase();

        String name = contentValues.getAsString(NAME);
        String phone = contentValues.getAsString(PHONE);

        int check = sqLiteDB.delete(TABLE_NAME, "gmcname = ?", new String[]{name});

        return check;
    }

    private void createTableGuarder(SQLiteDatabase db) {
        this.sqLiteDB = db;

        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
                SEQ+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME + " TEXT," +
                PHONE + " TEXT," +
                USE + " INTEGER );";

        sqLiteDB.execSQL(sqlCreate);
    }

    private void setString(ContentValues contentValues) {

    }
}
