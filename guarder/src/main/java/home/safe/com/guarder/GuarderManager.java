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

    // 테스트
    GuarderServerHelper serverHelper;
    SQLiteDatabase server;
    // 테스트

    Context context;
    GuarderDBHelper dbHelper;
    SQLiteDatabase db;
    GuarderController controller;

    public GuarderManager(Context context) {
        this.context = context;

        createDB();
        createController();
    }

    public void createController() {
        controller = new GuarderController();
        //controller.setDBHelper(dbHelper);
        controller.setHelper(dbHelper, serverHelper);
    }

    public void createDB() {
        dbHelper =
                new GuarderDBHelper(context, ProGuardianDBHelper.DB_NAME,null,ProGuardianDBHelper.DB_VERSION,GuarderDBHelper.PG_GUARDER);
        db = dbHelper.getWritableDatabase();

        // 테스트
        serverHelper =
                new GuarderServerHelper(context, ProGuardianDBHelper.DB_NAME, null, ProGuardianDBHelper.DB_VERSION, GuarderDBHelper.PG_GUARDER);
        server = serverHelper.getWritableDatabase();
        // 테스트
    }

    public void closeDB()
    {
        dbHelper.close();
    }

    public int insert(String target, GuarderVO guarderVO) {
        int check = 0;

        ContentValues sendCV;

        switch (target) {
            case GuarderShareWord.TARGET_DB :
                sendCV = guarderVO.convertDataToContentValuesSendDB();
                check = controller.insert(sendCV);
                break;
            case GuarderShareWord.TARGET_SERVER:
                sendCV = guarderVO.convertDataToContentValuesSendServer();
                check = controller.insertServer(sendCV);
                break;
        }

        Log.v("검색 결과" , "삽입 "+ check);

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

        Log.v("서버","0");
        ContentValues contentValues = new ContentValues();
        if(data != null) {
            contentValues = data.convertDataToContentValues();
        }
        contentValues.put(GuarderShareWord.SELECT_TYPE, type);

        List<ContentValues> resultList = null;

        ArrayList<GuarderVO> guaderList = new ArrayList<GuarderVO>();
        GuarderVO guarderVO;

        switch (target) {
            case GuarderShareWord.TARGET_DB :
                resultList = controller.search(contentValues);
                break;
            case GuarderShareWord.TARGET_SERVER:
                Log.v("서버","1");
                resultList = controller.searchServer(contentValues);
                break;
        }

        for (ContentValues cv : resultList) {
            guarderVO = new GuarderVO();
            guarderVO.convertContentValuesToData(cv);
            guaderList.add(guarderVO);
            Log.v("검색 결과",guarderVO.getGmcname()+ " " + guarderVO.getGmcphone() + " " + guarderVO.getGstate());
        }

        for(GuarderVO g : guaderList) {
            Log.v("검색 결과2",g.getGmcname()+ " " + g.getGmcphone() + " " + g.getGstate());
        }
        //Collections.sort(guaderList, new sortOrder());

        return guaderList;
}

    public GuarderVO selectOneItem(String target, String type, GuarderVO data) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(GuarderShareWord.SELECT_TYPE, type);
        if(type.equals(GuarderShareWord.TYPE_SELECT_CON)) {
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

        ArrayList<GuarderVO> guaderList = new ArrayList<GuarderVO>();

        if(guaderList.size() == 1) {
            return guaderList.get(0);
        }

        return null;
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
