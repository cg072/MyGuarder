package home.safe.com.trans;

import android.content.ContentValues;
import android.util.Log;

import com.safe.home.pgchanger.IProGuardianController;
import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.List;

/**
 * Created by plupin724 on 2017-12-27.
 */

public class TransController implements IProGuardianController{

    private ProGuardianDBHelper proGuardianDBHelper;
    int result;

    @Override
    public int insert(ContentValues contentValues) {

        Log.d("controllerInsert", "insert");
        result = proGuardianDBHelper.insert(contentValues);
        return result;
    }

    @Override
    public int update(ContentValues contentValues) {
        return 0;
    }

    @Override
    public int remove(ContentValues contentValues) {
        return 0;
    }

    @Override
    public List<ContentValues> search(ContentValues contentValues) {
        return null;
    }

    @Override
    public int insertServer(ContentValues contentValues) {
        return 0;
    }

    @Override
    public int updateServer(ContentValues contentValues) {
        return 0;
    }

    @Override
    public int removeServer(ContentValues contentValues) {
        return 0;
    }

    @Override
    public List<ContentValues> searchServer(ContentValues contentValues) {
        return null;
    }

    @Override
    public void setDBHelper(ProGuardianDBHelper proGuardianDBHelper) {

        this.proGuardianDBHelper = proGuardianDBHelper;

    }

    @Override
    public ProGuardianDBHelper getDBHelper() {
        return proGuardianDBHelper;
    }
}
