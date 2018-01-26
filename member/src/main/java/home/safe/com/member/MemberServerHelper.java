package home.safe.com.member;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.safe.home.pgchanger.PreTestDBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by hotki on 2017-12-26.
 */

public class MemberServerHelper extends PreTestDBHelper {

    SQLiteDatabase sqLiteDB;
    final static private String TABLE_NAME = "testServerMember";

    int mseq;
    String mname;
    String mphone;
    String mid;
    String mpwd;
    String mcertday;
    String mbirth;
    String memail;
    String mgender;
    String msns;
    String msnsid;
    String mregday;

    String data[] = new String[12];

    final static String[] MEMBER_COL = {   "mseq"     , "mname" , "mphone", "mid"     , "mpwd",
            "mcertday", "mbirth", "memail", "mgender" , "msns",
            "msnsid"    ,"mregday" };

    public MemberServerHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, int table) {
        super(context, name, factory, version, table);
    }

    // 있으면 오픈
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        createTableMember(db);
    }

    // DB 생성
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        super.onCreate(sqLiteDatabase);
        createTableMember(sqLiteDatabase);
    }

    // 버젼 바뀔땐 업그레이드
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        super.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);         // 기존의 테이블 삭제 후
        onCreate(sqLiteDatabase);
    }

    @Override
    public int insert(ContentValues contentValues) {
        sqLiteDB = getWritableDatabase();

        int check = (int)sqLiteDB.insert(TABLE_NAME, null, contentValues);

        return check;
    }

    @Override
    public List<ContentValues> search(ContentValues contentValues) {
        sqLiteDB = getWritableDatabase();

        List<ContentValues> list = new ArrayList<>();
        ContentValues cv;

        String sqlSearch = "SELECT * FROM " + TABLE_NAME ;
        switch (contentValues.get(MemberShareWord.SELECT_TYPE).toString()) {

            case MemberShareWord.TYPE_SELECT_ALL:
                break;
            case MemberShareWord.TYPE_SELECT_CON:
                sqlSearch = sqlSearch +  " WHERE ";

                setString(contentValues);

                for(int i = 1 ; i < MEMBER_COL.length ; i ++) {
                    if(data[i] != null) {
                        sqlSearch = sqlSearch + MEMBER_COL[i] + " = '" + data[i] + "' ";

                        if(i != MEMBER_COL.length - 1) {
                            for (int j = i + 1; j < MEMBER_COL.length; j++) {
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

        Log.v("서버", sqlSearch);

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

        setString(contentValues);

        String id = String.valueOf(contentValues.get(MEMBER_COL[3]));
        int check = sqLiteDB.update(
                TABLE_NAME,
                contentValues,
                MEMBER_COL[3] + " = ?",
                new String[]{id});
        return check;
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

    private void createTableMember(SQLiteDatabase db) {
        this.sqLiteDB = db;
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                MEMBER_COL[0] + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MEMBER_COL[1] + " TEXT," +
                MEMBER_COL[2] + " TEXT," +
                MEMBER_COL[3] + " TEXT," +
                MEMBER_COL[4] + " TEXT," +
                MEMBER_COL[5] + " TEXT," +
                MEMBER_COL[6] + " TEXT," +
                MEMBER_COL[7] + " TEXT," +
                MEMBER_COL[8] + " TEXT," +
                MEMBER_COL[9] + " TEXT," +
                MEMBER_COL[10] + " TEXT," +
                MEMBER_COL[11] + " TEXT);";

            sqLiteDB.execSQL(sqlCreate);

    }

    private void setString(ContentValues contentValues){

        for(int i = 1 ; i < MEMBER_COL.length ; i++) {
            data[i] = null;
            if(contentValues.getAsString(MEMBER_COL[i]) != null) {
                data[i] = contentValues.getAsString(MEMBER_COL[i]);
            }
        }
/*        mseq = contentValues.getAsInteger(MEMBER_COL[0]);
        mname = contentValues.getAsString(MEMBER_COL[1]);
        mphone = contentValues.getAsString(MEMBER_COL[2]);
        mid = contentValues.getAsString(MEMBER_COL[3]);
        mpwd = contentValues.getAsString(MEMBER_COL[4]);
        mcertday = contentValues.getAsString(MEMBER_COL[5]);
        mbirth = contentValues.getAsString(MEMBER_COL[6]);
        memail = contentValues.getAsString(MEMBER_COL[7]);
        mgender = contentValues.getAsString(MEMBER_COL[8]);
        msns = contentValues.getAsString(MEMBER_COL[9]);
        msnsid = contentValues.getAsString(MEMBER_COL[10]);
        mregday = contentValues.getAsString(MEMBER_COL[11]);*/

    }
}
