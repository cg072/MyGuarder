package home.safe.com.trans;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.ArrayList;
import java.util.List;

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

        Log.d("매니저 값확인", integratedVO.getTtype());
        Log.d("매니저 값확인2", integratedVO.getTmemo());
        Log.d("매니저 값확인3", Integer.toString(integratedVO.getTseq()));

        return check;

    }

    public ArrayList<TransIntegratedVO> select(){

        List<ContentValues> resultList;
        ArrayList<TransIntegratedVO> retList = new ArrayList<>();

        ContentValues values = integratedVO.convertDataToContentValues();

        resultList = controller.search(values);

        for(int i = 0; i < resultList.size(); i++){
            TransIntegratedVO vo = new TransIntegratedVO();
            vo.convertContentValuesToData(resultList.get(i));
            retList.add(vo);
        }

        return retList;

    }

    public int update(TransIntegratedVO integratedVO){
        int check = 0;
        this.integratedVO = integratedVO;
        ContentValues values = integratedVO.convertDataToContentValues();

        check = controller.update(values);

        return check;
    }

}
