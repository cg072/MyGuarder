package home.safe.com.myguarder;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.List;

/**
 * Created by JINNY_ on 2017-12-26.
 */

public class MyGuarderDBHelper extends ProGuardianDBHelper{

    public MyGuarderDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, int table) {
        super(context, name, factory, version, table);
    }

    public MyGuarderDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler, int table) {
        super(context, name, factory, version, errorHandler, table);
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
