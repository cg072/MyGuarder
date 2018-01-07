package home.safe.com.myguarder;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.List;

/**
 * Created by JINNY_ on 2017-12-27.
 */

public class LocationManage extends ProGuardian{
//모듈명매니져 LocationManage
    Context context;
    MyGuarderDBHelper dbHelper;
    SQLiteDatabase db;
    MyGuarderController controller;

    public LocationManage(Context context) {
        this.context = context;

        createDB();
        createController();
    }

    public void createController()
    {
        controller = new MyGuarderController();
        controller.setDBHelper(dbHelper);

    }

    public void createDB()
    {
        dbHelper =
                new MyGuarderDBHelper(context, ProGuardianDBHelper.DB_NAME,null,ProGuardianDBHelper.DB_VERSION,MyGuarderDBHelper.PG_LOCATION);
        db = dbHelper.getWritableDatabase();

    }

    public void closeDB()
    {
        dbHelper.close();
    }

//    public int insert(ContentValues contentValues) {
//        Log.d("MyGuarderController", "insert");
//        result = proGuardianDBHelper.insert(contentValues);
//        return result;
//    }
//
//    public int update(ContentValues contentValues) {
//        Log.d("MyGuarderController", "update");
//        result = proGuardianDBHelper.update(contentValues);
//        return result;
//    }
//
//    public int remove(ContentValues contentValues) {
//        Log.d("MyGuarderController", "remove");
//        result = proGuardianDBHelper.remove(contentValues);
//        return result;
//    }

    public List<ContentValues> search(ContentValues contentValues) {
        Log.d("MyGuarderController", "search");
        //SELECT ALL
        List<ContentValues> list;

        list = controller.search(contentValues);
        Log.d("MainActivity", "controller.search - "+list.size());

        return list;
    }


}
