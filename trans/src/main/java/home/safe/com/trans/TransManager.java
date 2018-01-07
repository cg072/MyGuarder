package home.safe.com.trans;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.safe.home.pgchanger.ProGuardianDBHelper;

/**
 * Created by plupin724 on 2018-01-07.
 */

public class TransManager {
    Context context;
    TransDBHelper dbHelper;
    SQLiteDatabase db;
    TransController controller;

    public TransManager(Context context){
        this.context = context;

        createController();
        createDB();
    }

    public void createController(){
        controller = new TransController();
        controller.setDBHelper(dbHelper);
    }

    public void createDB(){
        dbHelper = new TransDBHelper(context, ProGuardianDBHelper.DB_NAME, null, ProGuardianDBHelper.DB_VERSION, ProGuardianDBHelper.PG_TRANS);
    }

    public void closeDB(){
        dbHelper.close();
    }






}
