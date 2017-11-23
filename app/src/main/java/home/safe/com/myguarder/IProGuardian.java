package home.safe.com.myguarder;

import android.content.ContentValues;

import java.util.ArrayList;

/**
 * Created by hotki on 2017-11-01.
 */

interface IProGuardian {
    ArrayList searchList(ContentValues contentValues);
    int modify(ContentValues contentValues);
    int insert(ContentValues contentValues);
    int remove(ContentValues contentValues);
    //public int getDataCount();
    //public ContentValues getData(int row);
}
