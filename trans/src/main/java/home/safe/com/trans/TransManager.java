package home.safe.com.trans;

import android.content.ContentValues;
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

    TransIntegratedVO integratedVO = new TransIntegratedVO();


    public TransManager(Context context){
        this.context = context;

        createDB();
        createController();
    }

    public void createController(){
        controller = new TransController();
        controller.setDBHelper(dbHelper);
    }

    public void createDB(){
        dbHelper = new TransDBHelper(context, ProGuardianDBHelper.DB_NAME, null, ProGuardianDBHelper.DB_VERSION, ProGuardianDBHelper.PG_TRANS);
        db = dbHelper.getWritableDatabase();
    }

    public void closeDB(){
        dbHelper.close();
    }

    public int insert(TransIntegratedVO integratedVO){
        int check = 0;

        //dbHelper.removeTable();

        this.integratedVO = integratedVO;

        ContentValues values = new ContentValues();
        values.put("tlseq", integratedVO.getTmemo());
        values.put("tid", integratedVO.getTtype());
        values.put("ttype", integratedVO.getTmemo());
        values.put("tday", integratedVO.getTtype());

        check = controller.insert(values);

        return check;

    }

}
