package home.safe.com.myguarder.changer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 김진복 on 2017-11-21.
 * 예제용
 * PreTest 관련 db접근
 */

public class PreTestDBHelper extends ProGuardianDBHelper {
    final private String TABLE_NAME = getTableName();   //changer 생성시 테이블 관련 파라미터 참고
    private SQLiteDatabase db;
    private String _id = "idx";
    final static public String COL1 = "tcol1";
    final static public String COL2 = "tcol2";
    final static public String COL3 = "tcol3";
    String KEY_TEST = "test";
    String TAG = "test_tag";
    String NAME = "ProGuardianDBHelper";
    String CLASS = NAME + "@" + Integer.toHexString(hashCode());

    public PreTestDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, int table) {
        super(context, name, factory, version, table);
    }

    public PreTestDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler, int table) {
        super(context, name, factory, version, errorHandler, table);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                _id + " Integer PRIMARY KEY AUTOINCREMENT," +
                COL1 + " Text NOT NULL,"+
                COL2 + " Integer DEFAULT 0,"+
                COL3 + " Text NOT NULL"+
                ");";

        db = sqLiteDatabase;
        execUserQuery(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        db = sqLiteDatabase;
        switch (oldVersion) {
            case 1 :
                try {
                    db.beginTransaction();
                    db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + KEY_TEST+ " Integer DEFAULT 0");
                    db.setTransactionSuccessful();
                    Log.d(TAG, CLASS + " ===== upgrade성공" );

                } catch (IllegalStateException e) {
                    Log.e(TAG, CLASS, e);

                } finally {
                    db.endTransaction();

                }
                break;
        }
    }

    public void removeTable() {
        db = getWritableDatabase();
        String sql = "drop table " + TABLE_NAME;
        execUserQuery(sql);
        onCreate(db);
    }

    private void execUserQuery(String sql) {
        try {
            db.beginTransaction();
            db.execSQL(sql);
            db.setTransactionSuccessful();
            Log.d(TAG, CLASS + " ===== sql 실행 성공" );
        } catch (IllegalStateException e) {
            Log.e(TAG, CLASS, e);

        } finally {
            db.endTransaction();

        }
    }

    @Override
    public int insert(ContentValues contentValues) {
        db = getWritableDatabase();
        String col = COL1 + ", " + COL2 + ", " + COL3;
        int returnValue = 0;

        //테이블명, 컬럼명, 컬럼값
        long res = db.insert(TABLE_NAME, col, contentValues);
        if(res > 0) {
            Log.d(TAG, CLASS + " ===== insert성공");
            returnValue = 1;
        }

        return returnValue;
    }

    @Override
    public List<ContentValues> search(ContentValues contentValues) {
        db = getReadableDatabase();

        String colValue1 = contentValues.getAsString(COL1);
        String colValue2 = contentValues.getAsString(COL2);
        String colValue3 = contentValues.getAsString(COL3);

        String[] cols = {COL1, COL2, COL3};
        String[] conditionValues = {colValue1, colValue2, colValue3};
        String conditions = COL1 + "=?" + " and " + COL2 + "=?" + " and " + COL3 + "=?" ;
        String sort = COL2 + " desc";

        //검색
        //쿼리 방법1 - SQLiteDatabase  rawQuery 사용
        //Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null); // conditions);

        //쿼리 방법2 - SQLiteQueryBuilder 사용
        SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
        sqb.setTables(TABLE_NAME);
        //db, 보여질컬럼, 조건컬럼collection, 조건값collection, 그룹컬럼collection, 그룹조건collection, 정렬
        //sqb.query(SQLiteDatabase, String[], String[], String[], String[], String[], String);
        //2-1 조건없이 검색, 정렬만.
        Cursor cursor = sqb.query(db, cols, null, null, null, null, sort);
        //2-2 조건 검색
        //Cursor cursor = sqb.query(db, cols, conditions, conditionValues, null, null, sort);

        List<ContentValues> returnList = new ArrayList<ContentValues>();

        if(cursor != null) {
            Log.d(TAG, CLASS + " ===== search cnt= " + String.valueOf(cursor.getCount()));
            String val = "";
            int columnIndex = 0;

            while (cursor.moveToNext()) {
                Log.d(TAG, CLASS + " ===== search while in" );
                ContentValues values = new ContentValues();
                for (int a = 0; a < cols.length; a++) {
                    columnIndex = cursor.getColumnIndex(cols[a]);
                    val = cursor.getString(columnIndex);
                    Log.d(TAG, CLASS + " ===== search val= " + val);
                    values.put(cols[a], val);
                }

                returnList.add(values);
            }
        }

        return returnList;
    }

    @Override
    public int update(ContentValues contentValues) {
        db = getWritableDatabase();
        String[] conditions = {"값1", "값2", "값3"};   //물음표 위치에 0번째 값부터 조건으로 들어감
        db.update(TABLE_NAME, contentValues, "조건1=? and 조건2=? and 조건3=?", conditions);
        return 0;
    }

    @Override
    public int remove(ContentValues contentValues) {
        db = getWritableDatabase();
        String[] conditions = {"contentValues값1", "contentValues값2", "contentValues값3"};   //물음표 위치에 0번째 값부터 조건으로 들어감
        db.delete(TABLE_NAME, "조건1=? and 조건2=? and 조건3=?", conditions);
        return 0;
    }

}
