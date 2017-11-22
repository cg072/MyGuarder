package home.safe.com.myguarder.changer;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 김진복 on 2017-11-20.
 * 액티비티에서 접근성 및 통합관리용 class
 * db 필요시 사용
 * db 접근시 해당 객체만 알면 접근 가능하도록 만든 객체
 */

public class ProGuardianChanger {
    private Context context;
    private IProGuardianController customController;
    private ProGuardianDBHelper proGuardianDBHelper;
    private int tableN;
    private ContentValues contentValues;

    private ProGuardianChanger(){}

    public ProGuardianChanger(Context context) {
        this.context = context;
    }

    public ProGuardianChanger(Context context, int table) {
        this.context = context;
        this.tableN = table;
        dbSelector();
    }

    //예제용 메서드
    public void connectDB(int table) {
        //this.proGuardianDBHelper = new ProGuardianDBHelper(context, ProGuardianDBHelper.DB_NAME, null, ProGuardianDBHelper.DB_VERSION, tableN);
        dbSelector();
    }

    private void dbSelector() {
        switch (tableN)
        {
            case ProGuardianDBHelper.TABLE_MEMBER:
                this.proGuardianDBHelper = null;//멤버DBHelper//new ProGuardianDBHelper(context, ProGuardianDBHelper.DB_NAME, null, ProGuardianDBHelper.DB_VERSION, tableN);
                this.customController = null;
                    break;
            case ProGuardianDBHelper.TABLE_GUARDER:
                this.proGuardianDBHelper = null;//지킴이DBHelper//new ProGuardianDBHelper(context, ProGuardianDBHelper.DB_NAME, null, ProGuardianDBHelper.DB_VERSION, tableN);
                this.customController = null;
                    break;
            case ProGuardianDBHelper.TABLE_LOCATION:
                this.proGuardianDBHelper = null;//위치DBHelper//new ProGuardianDBHelper(context, ProGuardianDBHelper.DB_NAME, null, ProGuardianDBHelper.DB_VERSION, tableN);
                this.customController = null;
                    break;
            case ProGuardianDBHelper.TABLE_NOTICE:
                this.proGuardianDBHelper = null;//공지DBHelper//new ProGuardianDBHelper(context, ProGuardianDBHelper.DB_NAME, null, ProGuardianDBHelper.DB_VERSION, tableN);
                this.customController = null;
                    break;
            case ProGuardianDBHelper.TABLE_TRANS:
                this.proGuardianDBHelper = null;//이동수단DBHelper//new ProGuardianDBHelper(context, ProGuardianDBHelper.DB_NAME, null, ProGuardianDBHelper.DB_VERSION, tableN);
                this.customController = null;
                    break;
            case ProGuardianDBHelper.TABLE_TEST:
                this.proGuardianDBHelper = new PreTestDBHelper(context, ProGuardianDBHelper.DB_NAME, null, ProGuardianDBHelper.DB_VERSION, tableN);
                this.customController = new PreTestController(proGuardianDBHelper);
                    break;
            default:
                this.proGuardianDBHelper = null;
                this.customController = null;
                break;
        }

    }

    public void removeTable() {
        ((PreTestDBHelper)proGuardianDBHelper).removeTable();
    }

    public int insertData(ContentValues contentValues) {
        return customController.insert(contentValues);
    }

    public List<ProGuardianVO> searchData(ContentValues contentValues) {
        int listsize = 0;
        List<ContentValues> list = new ArrayList<ContentValues>();
        list = customController.search(contentValues);

        List<ProGuardianVO> returnList = new ArrayList<ProGuardianVO>();
        ProGuardianVO returnVO = null;

        if(list != null) {
            listsize = list.size();

            if (listsize > 0) {
                for (int idx = 0; idx < listsize; idx++) {
                    returnVO = new PreTestVO();
                    returnVO.convertContentValuesToData(list.get(idx));

                    returnList.add(returnVO);
                }
            }
        }

        return returnList;
    }

    public int modifyData(ContentValues contentValues) {
        return customController.update(contentValues);
    }

    public int removeData(ContentValues contentValues) {
        return customController.remove(contentValues);
    }


}
