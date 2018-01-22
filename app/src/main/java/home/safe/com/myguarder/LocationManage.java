package home.safe.com.myguarder;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.ArrayList;
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

    int result = 0;

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

    public int insert(ContentValues contentValues) {
        Log.d("MyGuarderController", "insert");
        result = controller.insert(contentValues);
        return result;
    }

    public int update(ContentValues contentValues) {
        Log.d("MyGuarderController", "update");
        result = controller.update(contentValues);
        return result;
    }

    public int remove(ContentValues contentValues) {
        Log.d("MyGuarderController", "remove");
        result = controller.remove(contentValues);
        return result;
    }

    public ArrayList<MyGuarderVO> search(ContentValues contentValues) {
        Log.d("MyGuarderController", "search");
        //SELECT ALL
        List<ContentValues> list;

        list = controller.search(contentValues);
        Log.d("MainActivity", "controller.search - "+list.size());

        ArrayList<MyGuarderVO> alMyGuarderVOList = new ArrayList<>();
        MyGuarderVO vo;

        for(ContentValues ls : list)
        {
            vo = new MyGuarderVO();
            vo.convertContentValuesToData(ls);
            alMyGuarderVOList.add(vo);

        }

        return alMyGuarderVOList;
    }

    public int insertServer(ContentValues contentValues) {
        result = controller.insertServer(contentValues);
        return result;
    }

    public int updateServer(ContentValues contentValues) {
        result = controller.updateServer(contentValues);
        return result;
    }

    public int removeServer(ContentValues contentValues) {
        result = controller.removeServer(contentValues);
        return result;
    }

    public ArrayList<MyGuarderVO> searchServer(ContentValues contentValues) {
        //SELECT ALL
        List<ContentValues> list;

        String str = "qwe";


        list = controller.searchServer(contentValues);
        return null;
    }

}
