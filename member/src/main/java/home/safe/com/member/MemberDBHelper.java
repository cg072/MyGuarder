package home.safe.com.member;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.safe.home.pgchanger.PreTestDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hotki on 2017-12-26.
 */

public class MemberDBHelper extends PreTestDBHelper {

    SQLiteDatabase sqLiteDB;
    final static private String TABLE_NAME = "guarderlist";

    final static String[] MEMBER_COL = {   "mseq"     , "mname" , "mphone", "mid"     , "mpwd",
            "mcertday", "mbirth", "memail", "mgender" , "msns",
            "msnid"    , "mregday"};
    final String sqlCreate = "create table member(" +
            MEMBER_COL[0] + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MEMBER_COL[1] + " TEXT NOT NULL, " +
            MEMBER_COL[2] + " TEXT NOT NULL, " +
            MEMBER_COL[3] + " TEXT NOT NULL, " +
            MEMBER_COL[4] + " TEXT NOT NULL, " +
            MEMBER_COL[5] + " TEXT" +
            MEMBER_COL[6] + " TEXT" +
            MEMBER_COL[7] + " TEXT" +
            MEMBER_COL[8] + " TEXT" +
            MEMBER_COL[9] + " TEXT" +
            MEMBER_COL[10]+ " TEXT" +
            MEMBER_COL[11]+ " TEXT);";

    public MemberDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, int table) {
        super(context, name, factory, version, table);
    }

    // DB 생성
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        super.onCreate(sqLiteDatabase);

        sqLiteDatabase.execSQL(sqlCreate);
    }

    // 버젼 바뀔땐 업그레이드
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        super.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);         // 기존의 테이블 삭제 후
        onCreate(sqLiteDatabase);
    }

    // 있으면 오픈, 없으면 크레이트
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public int insert(ContentValues contentValues) {
        sqLiteDB = getWritableDatabase();
        String sqlnsert = "";
        for(int i = 0 ; i < MEMBER_COL.length - 1; i ++) {
            sqlnsert += MEMBER_COL[i] + ", ";
        }
        sqlnsert += MEMBER_COL[MEMBER_COL.length - 1];

        int check = (int)sqLiteDB.insert(TABLE_NAME, sqlnsert, contentValues);

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
                sqlSearch +=  " WHERE "; // 이후 코드에서는 조건문을 써야한다.
                break;
        }

        Cursor cursor = sqLiteDB.rawQuery(sqlSearch, null);
        while (cursor.moveToNext()) {
            cv = new ContentValues();

            for(int i = 0 ; i < MEMBER_COL.length ; i++){
                cv.put(MEMBER_COL[i], cursor.getString(i));
            }

            list.add(cv);
        }

        return list;
    }

    @Override
    public int update(ContentValues contentValues) {
        sqLiteDB = getWritableDatabase();
        String name = String.valueOf(contentValues.get(MEMBER_COL[1]));
        String phone = String.valueOf(contentValues.get(MEMBER_COL[2]));
        int check = sqLiteDB.update(
                TABLE_NAME,
                contentValues,
                MEMBER_COL[1] + "= ? and " + MEMBER_COL[2] + " = ?",
                new String[]{name, phone});
        return 0;
    }

    @Override
    public int remove(ContentValues contentValues) {
        sqLiteDB = getWritableDatabase();
        String name = String.valueOf(contentValues.get(MEMBER_COL[1]));
        String phone = String.valueOf(contentValues.get(MEMBER_COL[2]));
        int check = sqLiteDB.delete(
                TABLE_NAME,
                MEMBER_COL[1] + "= ? and " + MEMBER_COL[2] + " = ?",
                new String[]{name, phone});
        return 0;
    }
}
