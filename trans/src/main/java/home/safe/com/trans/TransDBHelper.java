package home.safe.com.trans;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by plupin724 on 2017-12-27.
 */

public class TransDBHelper extends ProGuardianDBHelper{

    private SQLiteDatabase db;
    final static private String TABLE_NAME = "transportation";
    private String transCol[] = {"tseq", "tlseq", "tid", "ttype", "tmemo", "tday"};



    public TransDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, int table) {
        super(context, name, factory, version, table);
        Log.d("디비헬퍼확인", Integer.toString(table));
    }

    public TransDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler, int table) {
        super(context, name, factory, version, errorHandler, table);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /*Log.d("DBhelper", "oncreate1111");

        Log.d("sql시작", "시작");

        String sql = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                transCol[0] + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                transCol[1] + " INTEGER DEFAULT 0," +
                transCol[2] + " TEXT," +
                transCol[3] + " TEXT," +
                transCol[4] + " TEXT," +
                transCol[5] + " TEXT)";

        Log.d("sql문", sql);

        db.execSQL(sql);*/

        ////////////////////////////////////////////////////

        super.onCreate(db);
        this.db = sqLiteDatabase;

        String sql = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                transCol[0] + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                transCol[1] + " INTEGER DEFAULT 0," +
                transCol[2] + " TEXT," +
                transCol[3] + " TEXT," +
                transCol[4] + " TEXT," +
                transCol[5] + " TEXT)";

        db.execSQL(sql);

    }

    @Override
    public void onOpen(SQLiteDatabase db) {

        //기존 디비가 있으면 onOpen으로 접근함

        super.onOpen(db);

        this.db = db;

        String sql = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                transCol[0] + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                transCol[1] + " INTEGER DEFAULT 0," +
                transCol[2] + " TEXT," +
                transCol[3] + " TEXT," +
                transCol[4] + " TEXT," +
                transCol[5] + " TEXT)";

        db.execSQL(sql);

        Log.d("transDBHelper", "onOpen");

    }




    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        super.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }


    @Override
    public String getTableName() {
        return super.getTableName();
    }

    @Override
    public int insert(ContentValues contentValues) {

        Log.d("transdbhelper", "dbhelperinsert");

        db = getWritableDatabase();

        ///컨텐트벨류에 모든 값이 세팅이 되어 있어야 함!!!!!
        int check = (int)db.insert(TABLE_NAME, null, contentValues);

        Log.d("transdbhelper", "dbhelperinsert22");

        return check;
    }

    @Override
    public List<ContentValues> search(ContentValues contentValues) {

        List<ContentValues> conList = new ArrayList<>();

        db = getReadableDatabase();

        SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
        sqb.setTables(TABLE_NAME);

        Cursor cursor = sqb.query(db, null, null, null, null, null, null);

        if(cursor != null){
            //movetoNext를 실행하는 동시에 그 자리로 이동한다고 생각하라
            while (cursor.moveToNext()){
                ContentValues values = new ContentValues();
                for(int i = 0; i < transCol.length; i++){
                    int index = cursor.getColumnIndex(transCol[i]);
                    String getVal = cursor.getString(index);
                    values.put(transCol[i], getVal);
                }
                conList.add(values);
            }

        }
        return conList;
    }

    @Override
    public int update(ContentValues contentValues) {
        int check = 0;
        db = getWritableDatabase();

        int tseq = contentValues.getAsInteger(transCol[0]);
        String ttype = contentValues.getAsString(transCol[3]);
        String tmemo = contentValues.getAsString(transCol[4]);

        Log.d("업데이트디비헬퍼1", ttype);
        Log.d("업데이트디비헬퍼2", tmemo);

        check = db.update(TABLE_NAME, contentValues,
                transCol[0] + " = ? ",
                new String[]{Integer.toString(tseq)});

        //update trans set ttype = "기타" and tmemo = "ㅋㅋㅋ" where index = 1 ;



        //////////////////////////////////

        search(contentValues);

        List<ContentValues> upval = search(contentValues);

        for(ContentValues data : upval){
            Log.d("들어옵니까?", data.getAsString("ttype") + "   " + data.getAsString("tmemo"));
        }

        return check;
    }

    @Override
    public int remove(ContentValues contentValues) { return 0; }



    ////////////////////////////////임시로 테이블을 컨트롤/////////////////////////////////////

    //임시로 테이브를 만드는 메소드
    public void createTable(){

        Log.d("DBhelper", "oncreate1111");
        db = getWritableDatabase();
        Log.d("sql시작", "시작");
        String sql = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                transCol[0] + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                transCol[1] + " INTEGER DEFAULT 0," +
                transCol[2] + " TEXT," +
                transCol[3] + " TEXT," +
                transCol[4] + " TEXT," +
                transCol[5] + " TEXT)";

        Log.d("sql문", sql);

        db.execSQL(sql);
    }

    //임시로 테이블을 지우는 메소드
    public void removeTable(){
        Log.d("리무브", "리무브실행");
        db = getWritableDatabase();
        String sql = "DROP TABLE if exists " + TABLE_NAME;
        db.execSQL(sql);

        //onCreate(db);

    }

}
