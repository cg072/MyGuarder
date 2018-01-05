package home.safe.com.myguarder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.safe.home.pgchanger.ProGuardianDBHelper;

/**
 * Created by JINNY_ on 2017-12-27.
 */

public class CouplerMVC extends ProGuardian{
//모듈명매니져 LocationMa~~
    Context context;
    MyGuarderDBHelper dbHelper;
    SQLiteDatabase db;
    MyGuarderController controller;

    public CouplerMVC(Context context) {
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


}
