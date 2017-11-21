package home.safe.com.myguarder.changer;

import android.content.ContentValues;
import android.content.Context;

import java.util.List;

/**
 * Created by 김진복 on 2017-11-20.
 * 공통으로 쓰이는 중간 다리 역할
 * db 접근 필요시 새로 생성해서 사용
 */

public class ProGuardianChanger {
    private Context context;
    private IProGuardianController customController;
    private ProGuardianDBHelper proGuardianDBHelper;
    private int tableN;
    private ContentValues contentValues;

    private ProGuardianChanger(){}

    /*public ProGuardianChanger(Context context) {
        this.context = context;
    }*/

    public ProGuardianChanger(Context context, int table) {
        this.context = context;
        this.tableN = table;
        dbSelector();
    }

/*
    //abstract 클래스에서는 몸체가 없음
    abstract public int insertData(ContentValues contentValues) ;
    abstract public int modifyData(ContentValues contentValues);
    abstract public int removeData(ContentValues contentValues);
    abstract public List<ContentValues> searchData(ContentValues contentValues);
    abstract public ContentValues convertDataToContentValues();
*/

    //예제용 메서드
    public void connectDB() {
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
            default:
                this.proGuardianDBHelper = new ProGuardianDBHelper(context, ProGuardianDBHelper.DB_NAME, null, ProGuardianDBHelper.DB_VERSION, tableN);
                this.customController = new PreTestController(proGuardianDBHelper);
                break;
        }

    }


    public int insertData(ContentValues contentValues) {
        return customController.insert(contentValues);
    }

    public List<ContentValues> searchData(ContentValues contentValues) {
        return customController.search(contentValues);
    }

    public int modifyData(ContentValues contentValues) {
        return customController.update(contentValues);
    }

    public int removeData(ContentValues contentValues) {
        return customController.remove(contentValues);
    }


}
