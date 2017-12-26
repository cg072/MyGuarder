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
