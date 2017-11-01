package home.safe.com.myguarder;

import android.content.ContentValues;

import java.util.ArrayList;

/**
 * Created by hotki on 2017-11-01.
 */

interface IProGuardian {
    public ArrayList searchList(ContentValues contentValues);
    public int modify(ContentValues contentValues);
    public int insert(ContentValues contentValues);
    public int remove(ContentValues contentValues);
    //public int getDataCount();
    //public ContentValues getData(int row);
}
