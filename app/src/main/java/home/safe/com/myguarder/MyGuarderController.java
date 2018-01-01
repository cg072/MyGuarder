package home.safe.com.myguarder;

import android.content.ContentValues;
import android.util.Log;

import com.safe.home.pgchanger.IProGuardianController;
import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.List;

/**
 * Created by JINNY_ on 2017-12-26.
 */

public class MyGuarderController implements IProGuardianController{

    private ProGuardianDBHelper proGuardianDBHelper;
    private List<ContentValues> list;
    private int result;

    @Override
    public int insert(ContentValues contentValues) {
        Log.d("MyGuarderController", "insert");
        result = proGuardianDBHelper.insert(contentValues);
        return result;
    }

    @Override
    public int update(ContentValues contentValues) {
        Log.d("MyGuarderController", "update");
        result = proGuardianDBHelper.update(contentValues);
        return result;
    }

    @Override
    public int remove(ContentValues contentValues) {
        Log.d("MyGuarderController", "remove");
        result = proGuardianDBHelper.remove(contentValues);
        return result;
    }

    @Override
    public List<ContentValues> search(ContentValues contentValues) {
        Log.d("MyGuarderController", "search");
        list = proGuardianDBHelper.search(contentValues);
        return list;
    }

    @Override
    public int insertServer(ContentValues contentValues) {
        Log.d("MyGuarderController", "insertServer");
        return 0;
    }

    @Override
    public int updateServer(ContentValues contentValues) {
        Log.d("MyGuarderController", "updateServer");
        return 0;
    }

    @Override
    public int removeServer(ContentValues contentValues) {
        Log.d("MyGuarderController", "removeServer");
        return 0;
    }

    @Override
    public List<ContentValues> searchServer(ContentValues contentValues) {
        Log.d("MyGuarderController", "searchServer");
        return null;
    }

    @Override
    public void setDBHelper(ProGuardianDBHelper proGuardianDBHelper) {
        Log.d("MyGuarderController","setDBHelper");
        this.proGuardianDBHelper = proGuardianDBHelper;
    }

    @Override
    public ProGuardianDBHelper getDBHelper() {
        return proGuardianDBHelper;
    }
}
