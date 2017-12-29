package home.safe.com.member;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.safe.home.pgchanger.PreTestDBHelper;

import java.util.List;

/**
 * Created by hotki on 2017-12-26.
 */

public class MemberDBHelper extends PreTestDBHelper {

    private String[] memberCol = {   "mseq"     , "mname" , "mphone", "mid"     , "mpwd",
                                       "mcertday", "mbirth", "memail", "mgender" , "msns",
                                       "msnid"    , "mregday"};

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        super.onCreate(sqLiteDatabase);

        String sqlCreate = "create table member("
                + "mseq 		int 		auto_increment 	primary key,"
                + "mname 		varchar(12)	not null,"
                + "mphone 	    varchar(11)	not null,"
                + "mid 		    varchar(15)	not null,"
                + "mpwd 		varchar(255)	not null,"
                + "mcertday 	datetime 		default '2000-01-01',"
                + "mbirth 		char(8),"
                + "memail 		varchar(50),"
                + "mgender 	char(1) ,"
                + "msns 		varchar(50),"
                + "msnid 		varchar(50),"
                + "mregday 	datetime 		default '2000-01-01');";
        sqLiteDatabase.execSQL(sqlCreate);
    }

    // 버젼 바뀔땐 업그레이드
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        super.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }

    // 있으면 오픈, 없으면 크레이트
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public MemberDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, int table) {
        super(context, name, factory, version, table);

    }

    @Override
    public int insert(ContentValues contentValues) {

        return 0;
    }

    @Override
    public List<ContentValues> search(ContentValues contentValues) {
        return null;
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
