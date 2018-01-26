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
 * Created by hotki on 2018-01-16.
 */

public class GuarderServerHelper extends ProGuardianDBHelper {

    final static String TAG = "ServerHelpler";

    SQLiteDatabase sqLiteDB;
    final static private String TABLE_NAME = "testServerGuarder";

    final static private String GUARDER_COL_ALL[] = {"gseq", "gmid", "gmcid", "gregday", "gmcname", "gmcphone",  "gstate"};
    final static private String GUARDER_COL_TEXT[] = { "gmid", "gmcid", "gregday", "gmcname", "gmcphone"};
    final static private String GUARDER_COL_INTEGER[] = {"gseq", "gstate"};


    String stringData[] = new String[GUARDER_COL_TEXT.length];
    int intData[] = new int[GUARDER_COL_INTEGER.length];

    public GuarderServerHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, int table) {
        super(context, name, factory, version, table);
    }

    @Override
    public int insert(ContentValues contentValues) {
        Log.v("서버","인서트");
        sqLiteDB = getWritableDatabase();

        // DB에 입력한 값으로 행 추가
        int check = (int)sqLiteDB.insert(TABLE_NAME, null, contentValues);

        Log.v("아이디", contentValues.getAsString(GUARDER_COL_ALL[1]));

        if(check == 0 ) {
            Log.v("테스트", "삽입실패");
        } else {
            Log.v("테스트", ""+check);
        }
        Log.v("서버", "인서트" + check);
        return check;
    }

    @Override
    public List<ContentValues> search(ContentValues contentValues) {
        Log.v("서버","3");
        sqLiteDB = getWritableDatabase();

        List<ContentValues> list = new ArrayList<>();
        ContentValues cv;

        String sqlSearch = "SELECT * FROM " + TABLE_NAME ;

        switch (contentValues.get(GuarderShareWord.SELECT_TYPE).toString()) {

            case GuarderShareWord.TYPE_SELECT_ALL:
                break;
            case GuarderShareWord.TYPE_SELECT_CON:
                sqlSearch = sqlSearch +  " WHERE ";

                setData(contentValues);

                int check = 0;

                for(int i = 0 ; i < GUARDER_COL_TEXT.length ; i ++) {
                    if(stringData[i] != null) {
                        sqlSearch = sqlSearch + GUARDER_COL_TEXT[i] + " = '" + stringData[i] + "' ";

                        check++;

                        if(i != GUARDER_COL_TEXT.length - 1) {
                            for (int j = i + 1; j < GUARDER_COL_TEXT.length; j++) {
                                if (stringData[j] != null) {
                                    sqlSearch += " and ";
                                    break;
                                }
                            }
                        }
                    }
                }
                if(check > 1 && intData[1] != -1) {
                    sqlSearch = sqlSearch + " and " + GUARDER_COL_INTEGER[1] + " = " + intData[1] + " ";
                }

                break;
        }

        Log.v("쿼리", sqlSearch);

        Cursor cursor = sqLiteDB.rawQuery(sqlSearch, null);
        while (cursor.moveToNext()) {
            cv = new ContentValues();

            cv.put(GUARDER_COL_ALL[0], cursor.getInt(0));
            cv.put(GUARDER_COL_ALL[1], cursor.getString(1));
            cv.put(GUARDER_COL_ALL[2], cursor.getString(2));
            cv.put(GUARDER_COL_ALL[3], cursor.getString(3));
            cv.put(GUARDER_COL_ALL[4], cursor.getString(4));
            cv.put(GUARDER_COL_ALL[5], cursor.getString(5));
            cv.put(GUARDER_COL_ALL[6], cursor.getInt(6));

            list.add(cv);
        }

        return list;
    }

    @Override
    public int update(ContentValues contentValues) {
        sqLiteDB = getWritableDatabase();
        int check = 0;
/*        int check = sqLiteDB.update(
                TABLE_NAME,
                contentValues,
                NAME + " = ? and " + PHONE +" = ? " ,
                new String[]{name, phone});*/
        return check;
    }

    @Override
    public int remove(ContentValues contentValues) {
        sqLiteDB = getWritableDatabase();

        int check = 0;
/*        String name = String.valueOf(contentValues.get(NAME));
        String phone = String.valueOf(contentValues.get(PHONE));

        int check = sqLiteDB.delete(TABLE_NAME, "gmcname = ?", new String[]{name});*/

        return check;
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

    private void createTableGuarder(SQLiteDatabase db) {
        this.sqLiteDB = db;

        String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
                GUARDER_COL_ALL[0] +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                GUARDER_COL_ALL[1] + " TEXT," +
                GUARDER_COL_ALL[2] + " TEXT," +
                GUARDER_COL_ALL[3] + " TEXT," +
                GUARDER_COL_ALL[4] + " TEXT," +
                GUARDER_COL_ALL[5] + " TEXT," +
                GUARDER_COL_ALL[6] + " INTEGER); ";


        sqLiteDB.execSQL(SQL_CREATE);
    }

    private void setData(ContentValues contentValues) {
        for(int i = 0 ; i < GUARDER_COL_TEXT.length ; i++) {
            stringData[i] = null;
            if(contentValues.getAsString(GUARDER_COL_TEXT[i]) != null) {
                stringData[i] = contentValues.getAsString(GUARDER_COL_TEXT[i]);
            }
        }
        for(int i = 0 ; i < GUARDER_COL_INTEGER.length ; i++) {
            intData[i] = -1;
            if(contentValues.getAsInteger(GUARDER_COL_INTEGER[i]) != -1) {
                intData[i] = contentValues.getAsInteger(GUARDER_COL_INTEGER[i]);
            }
        }
    }
}
