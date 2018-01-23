package home.safe.com.guarder;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by hotki on 2018-01-05.
 */

public class GuarderManager {

    Context context;
    GuarderDBHelper dbHelper;
    SQLiteDatabase db;
    GuarderController controller;

    final static String COL_NAME = "gmcname";
    final static String COL_PHONE = "gmcphone";
    final static String COL_USE = "gstate";
    final static String SELECT = "select";
    final static String UPDATE = "update";
    final static String INSERT = "insert";
    final static String DELETE = "delete";
    final static String QUERY_TYPE = "type";
    final static String TYPE = "type";
    final static String TYPE_SELECT_ALL = "all";
    final static String TYPE_SELECT_CON = "con";

    public GuarderManager(Context context) {
        this.context = context;

        createDB();
        createController();
    }

    public void createController() {
        controller = new GuarderController();
        controller.setDBHelper(dbHelper);
    }

    public void createDB() {
        dbHelper =
                new GuarderDBHelper(context, ProGuardianDBHelper.DB_NAME,null,ProGuardianDBHelper.DB_VERSION,GuarderDBHelper.PG_GUARDER);
        db = dbHelper.getWritableDatabase();
    }

    public void closeDB()
    {
        dbHelper.close();
    }

    public int insert(String target, GuarderVO guarderVO) {
        int check = 0;

        ContentValues sendCV = guarderVO.convertDataToContentValuesSendDB();

        switch (target) {
            case GuarderShareWord.TARGET_DB :
                check = controller.insert(sendCV);
                break;
            case GuarderShareWord.TARGET_SERVER:
                check = controller.insertServer(sendCV);
                break;
        }

        return check;
    }

    public int delete(String target, GuarderVO guarderVO) {
        int check = 0;

        ContentValues sendCV = guarderVO.convertDataToContentValuesSendDB();

        switch (target) {
            case GuarderShareWord.TARGET_DB :
                check = controller.remove(sendCV);
                break;
            case GuarderShareWord.TARGET_SERVER:
                check = controller.removeServer(sendCV);
                break;
        }

        return check;
    }

    public int update(String target, GuarderVO guarderVO) {
        int check = 0;

        ContentValues sendCV = guarderVO.convertDataToContentValuesSendDB();
        switch (target) {
            case GuarderShareWord.TARGET_DB :
                check = controller.update(sendCV);
                break;
            case GuarderShareWord.TARGET_SERVER:
                check = controller.updateServer(sendCV);
                break;
        }
        return check;
    }

    public ArrayList<GuarderVO> select(String target, String type, GuarderVO data) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(TYPE, type);
        if(type.equals(TYPE_SELECT_CON)) {
            contentValues.put("gstate", data.getGstate());
        }


        List<ContentValues> resultList = null;

        switch (target) {
            case GuarderShareWord.TARGET_DB :
                resultList = controller.search(contentValues);
                break;
            case GuarderShareWord.TARGET_SERVER:
                resultList = controller.searchServer(contentValues);
                break;
        }

        GuarderVO guarderVO;
        ArrayList<GuarderVO> guaderList = new ArrayList<GuarderVO>();

        for (ContentValues cv : resultList) {
            guarderVO = new GuarderVO();
            guarderVO.convertContentValuesToData(cv);
            guaderList.add(guarderVO);
        }

        Collections.sort(guaderList, new sortOrder());

        return guaderList;
    }

    /*
    *  date     : 2017.11.22
    *  author   : Kim Jong-ha
    *  title    : sortOrder, NameDescCompareSearch 메소드 생성
    *  comment  : 이름순 정렬
    * */
    private class sortOrder implements Comparator<GuarderVO> {
        @Override
        public int compare(GuarderVO arg0, GuarderVO arg1) {
            return  arg0.getGmcname().compareTo(arg1.getGmcname());
        }
    }
}
