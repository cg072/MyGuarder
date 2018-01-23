package home.safe.com.guarder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hotki on 2018-01-16.
 */

public class GuarderServerHelper extends ProGuardianDBHelper {

    final static String TAG = "ServerHelpler";

    SQLiteDatabase sqLiteDB;
    final static private String TABLE_NAME = "testServer";

    final static private String GUARDER_COL[] = {"gseq", "gmid", "gmcid", "gstate", "gregday"};

    String data[] = new String[5];

    public GuarderServerHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, int table) {
        super(context, name, factory, version, table);
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
        sqLiteDB = getWritableDatabase();

        List<ContentValues> list = new ArrayList<>();
        ContentValues cv;

        String sqlSearch = "SELECT * FROM " + TABLE_NAME ;

        switch (contentValues.get(GuarderShareWord.SELECT_TYPE).toString()) {

            case GuarderShareWord.TYPE_SELECT_ALL:
                break;
            case GuarderShareWord.TYPE_SELECT_CON:
                sqlSearch = sqlSearch +  " WHERE ";

                setString(contentValues);

                for(int i = 1 ; i < GUARDER_COL.length ; i ++) {
                    if(data[i] != null) {
                        sqlSearch = sqlSearch + GUARDER_COL[i] + " = '" + data[i] + "' ";

                        if(i != GUARDER_COL.length - 1) {
                            for (int j = i + 1; j < GUARDER_COL.length; j++) {
                                if (data[j] != null) {
                                    sqlSearch += " and ";
                                    break;
                                }
                            }
                        }
                    }
                }

                break;
        }

        Cursor cursor = sqLiteDB.rawQuery(sqlSearch, null);
        while (cursor.moveToNext()) {
            cv = new ContentValues();

            for(int i = 0 ; i < GUARDER_COL.length ; i++){
                cv.put(GUARDER_COL[i], cursor.getInt(i));
            }
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
                GUARDER_COL[0] +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                GUARDER_COL[1] + " TEXT," +
                GUARDER_COL[2] + " TEXT," +
                GUARDER_COL[3] + " INTEGER, " +
                GUARDER_COL[4] + " TEXT);";

        sqLiteDB.execSQL(SQL_CREATE);
    }

    private void setString(ContentValues contentValues) {
        for(int i = 0 ; i < GUARDER_COL.length ; i++) {
            data[i] = null;
            if(contentValues.getAsString(GUARDER_COL[i]) != null) {
                data[i] = contentValues.getAsString(GUARDER_COL[i]);
            }
        }
    }
}
