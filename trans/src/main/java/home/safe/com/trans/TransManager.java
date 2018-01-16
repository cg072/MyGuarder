package home.safe.com.trans;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.ArrayList;

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

        Log.d("managerInsert", "insert");
       // dbHelper.removeTable();

        this.integratedVO = integratedVO;


        ContentValues values = integratedVO.convertDataToContentValues();

        //{"tseq", "tlseq", "tid", "ttype", "tmemo", "tday"};

        /*ContentValues values = new ContentValues();
        // values.put("tseq", 1);
        values.put("tlseq", integratedVO.getTlseq());
        values.put("tid", integratedVO.getTid());
        values.put("ttype", integratedVO.getTtype());
        values.put("tmemo", integratedVO.getTmemo());
        values.put("tday", integratedVO.getTday());*/

        check = controller.insert(values);

        return check;

    }

    /*public ArrayList<TransIntegratedVO> selectAll(ArrayList<TransIntegratedVO> transIntegratedVOS){


    }*/


}
