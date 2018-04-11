package home.safe.com.myguarder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by JINNY_ on 2017-12-26.
 */

public class MyGuarderDBHelper extends ProGuardianDBHelper{

    final private String TABLE_NAME = getTableName();   //changer 생성시 테이블 관련 파라미터 참고
    private SQLiteDatabase db;
    private String myGuarderCol[] = {"lseq","llat","llong","lday","ltime","lid"};
    private int result = 0;

    public MyGuarderDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, int table) {
        super(context, name, factory, version, table);
        Log.d("MyGuarderDBHelper", "생성자");
    }

    public MyGuarderDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler, int table) {
        super(context, name, factory, version, errorHandler, table);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.d("MyGuarderDBHelper", "onOpen");
        this.db = db;

        //없어져야할 부분 ****
        String sql = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                myGuarderCol[0] +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                myGuarderCol[1] +" TEXT DEFAULT 000.0000000," +
                myGuarderCol[2] +" TEXT DEFAULT 000.0000000," +
                myGuarderCol[3] +" TEXT DEFAULT 000," +
                myGuarderCol[4] +" TEXT DEFAULT 00000," +
                myGuarderCol[5] +" TEXT NOT NULL)";
        //datetime, now() 없음

        db.execSQL(sql);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("MyGuarderDBHelper", "onCreate");
        //DB 생성
        String sql = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                myGuarderCol[0] +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                myGuarderCol[1] +" TEXT DEFAULT 000.0000000," +
                myGuarderCol[2] +" TEXT DEFAULT 000.0000000," +
                myGuarderCol[3] +" TEXT DEFAULT 000," +
                myGuarderCol[4] +" TEXT DEFAULT 00000," +
                myGuarderCol[5] +" TEXT NOT NULL)";
        //datetime, now() 없음

        db = sqLiteDatabase;

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        super.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
        Log.d("MyGuarderDBHelper", "onUpgrade");
    }

    @Override
    public int insert(ContentValues contentValues) {
        Log.d("MyGuarderDBHelper", "insert");
        Log.d("MyGuarderDBHelper",contentValues.getAsString(myGuarderCol[1]));

        result = (int)db.insert(TABLE_NAME,myGuarderCol[1]+","+myGuarderCol[2]+","+myGuarderCol[3]+","+myGuarderCol[4]+","+myGuarderCol[5],contentValues);

        return result;
    }

    @Override
    public List<ContentValues> search(ContentValues contentValues) {
        Log.d("MyGuarderDBHelper", "search");
        List<ContentValues> list = new ArrayList<>();
        Log.d("MyGuarderDBHelper", "contentValues.size - "+contentValues.size());
        //contentValues.size() 로 SELECT문 WHERE 갯수 판단
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            Log.d("Cursor",""+cursor.getString(0));
            Log.d("Cursor",""+cursor.getString(1));
            Log.d("Cursor",""+cursor.getString(2));
            Log.d("Cursor",""+cursor.getString(3));
            Log.d("Cursor",""+cursor.getString(4));
            Log.d("Cursor",""+cursor.getString(5));
            Log.d("list","-----------");

            ContentValues values = new ContentValues();
            DatabaseUtils.cursorRowToContentValues(cursor , values);
            list.add(values);

            cursor.moveToNext();
        }

        Log.d("Cursor",""+cursor.getCount());
        Log.d("list","-----------");
        cursor.close();

        for(int a = 0; a < list.size();a++)
        {
            Log.d("list",myGuarderCol[0]+" - "+list.get(a).getAsString(myGuarderCol[0]));
            Log.d("list",myGuarderCol[1]+" - "+list.get(a).getAsString(myGuarderCol[1]));
            Log.d("list",myGuarderCol[2]+" - "+list.get(a).getAsString(myGuarderCol[2]));
            Log.d("list",myGuarderCol[3]+" - "+list.get(a).getAsString(myGuarderCol[3]));
            Log.d("list",myGuarderCol[4]+" - "+list.get(a).getAsString(myGuarderCol[4]));
            Log.d("list",myGuarderCol[5]+" - "+list.get(a).getAsString(myGuarderCol[5]));
            Log.d("list","-----------");

        }
        Log.d("list",""+list.size());

        return list;
    }

    @Override
    public int update(ContentValues contentValues) {
        Log.d("MyGuarderDBHelper", "update");
        result = db.update(TABLE_NAME,contentValues,myGuarderCol[0]+" = ?" ,new String[]{contentValues.getAsString(myGuarderCol[0])});
        //db.update(TABLE_NAME,contentValues,"lseq = ?" ,new String[]{lseq의 지울 번호 -> ?에 들어감});
        return result;
    }

    /**
     *
     * @author 경창현
     * @version 1.0.0
     * @text DB DELETE 중에서 지난 위치 삭제가 유일한것으로 판단됨
     * @since 2018-01-04 오후 8:09
    **/
    @Override
    public int remove(ContentValues contentValues) {
        Log.d("MyGuarderDBHelper", "remove");

        Log.d("remove", contentValues.getAsString(myGuarderCol[3]));
        String thisDate = contentValues.getAsString(myGuarderCol[3]);
        String lastDate = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(contentValues.getAsString(myGuarderCol[3]));

            Calendar calendar = new GregorianCalendar();

            calendar.add(Calendar.DATE, -1);
            lastDate = dateFormat.format(calendar.getTime());
            Log.d("remove", lastDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        result = db.delete(TABLE_NAME,myGuarderCol[3]+" <> ? "+" and "+myGuarderCol[3]+" <> ? ",new String[]{thisDate,lastDate});

        return result;
    }


    public int serverInsert(ContentValues contentValues) {
        Log.d("MyGuarderDBHelper", "serverInsert");
        Log.d("MyGuarderDBHelper",contentValues.getAsString(myGuarderCol[1]));

        result = (int)db.insert(TABLE_NAME,myGuarderCol[1]+","+myGuarderCol[2]+","+myGuarderCol[3]+","+myGuarderCol[4]+","+myGuarderCol[5],contentValues);
        return result;
    }

    public String plusColums(String... nameasd)
    {
        String list = "";
        for(String a: nameasd)
        {
            list += a +",";
            a.length();
        }

        Log.d("MyGuarderDBHelper","plusColums - "+list);
        return list;
    }
}
