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
    final static String SELECT_TYPE = "type";

    public GuarderManager(Context context) {
        this.context = context;

        createDB();
        createController();
    }

    public void createController()
    {
        controller = new GuarderController();
        controller.setDBHelper(dbHelper);
    }

    public void createDB()
    {
        dbHelper =
                new GuarderDBHelper(context, ProGuardianDBHelper.DB_NAME,null,ProGuardianDBHelper.DB_VERSION,GuarderDBHelper.PG_GUARDER);
        db = dbHelper.getWritableDatabase();

    }

    public void closeDB()
    {
        dbHelper.close();
    }

    public int insert(GuarderVO guarderVO) {
        int check = 0;
        ContentValues sendCV = new ContentValues();
        sendCV.put(COL_NAME, guarderVO.getGmcname());
        sendCV.put(COL_PHONE, guarderVO.getGmcphone());
        sendCV.put(COL_USE, guarderVO.getGstate());

        check = controller.insert(sendCV);

        return check;
    }

    public int delete(GuarderVO guarderVO) {
        int check = 0;
        return check;
    }

    public int update(GuarderVO guarderVO) {
        int check = 0;

        ContentValues sendCV = new ContentValues();
        sendCV.put(COL_NAME, guarderVO.getGmcname());
        sendCV.put(COL_PHONE, guarderVO.getGmcphone());
        sendCV.put(COL_USE, guarderVO.getGstate());

        check = controller.update(sendCV);

        return check;
    }

    public ArrayList<GuarderVO> select(String type, GuarderVO data) {
        Log.v("가더","디비 보내기 진입");
        List<ContentValues> resultList = new ArrayList<ContentValues>();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SELECT_TYPE, type);
        contentValues.put("gstate", data.getGstate());
        resultList = controller.search(contentValues);

        GuarderVO guarderVO;
        ArrayList<GuarderVO> guaderlist = new ArrayList<GuarderVO>();

        for (ContentValues cv : resultList) {

            guarderVO = new GuarderVO();

            guarderVO.setGmcname(cv.get(COL_NAME).toString());
            guarderVO.setGmcphone(cv.get(COL_PHONE).toString());
            guarderVO.setGstate((int)(cv.get(COL_USE)));

            guaderlist.add(guarderVO);
        }

        Collections.sort(guaderlist, new NameDescCompareGuarders());

        return guaderlist;
    }

    /*
    *  date     : 2017.11.22
    *  author   : Kim Jong-ha
    *  title    : NameDescCompareGuarders, NameDescCompareSearch 메소드 생성
    *  comment  : 이름순 정렬
    * */
    private class NameDescCompareGuarders implements Comparator<GuarderVO> {
        @Override
        public int compare(GuarderVO arg0, GuarderVO arg1) {
            return  arg0.getGmcname().compareTo(arg1.getGmcname());
        }
    }
}
