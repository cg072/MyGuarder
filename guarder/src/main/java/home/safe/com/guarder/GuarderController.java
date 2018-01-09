package home.safe.com.guarder;

import android.content.ContentValues;

import com.safe.home.pgchanger.IProGuardianController;
import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hotki on 2017-12-26.
 */

public class GuarderController implements IProGuardianController {

    private ProGuardianDBHelper proGuardianDBHelper;

    @Override
    public int insert(ContentValues contentValues) {
        return proGuardianDBHelper.insert(contentValues);
    }

    @Override
    public int update(ContentValues contentValues) {
        return proGuardianDBHelper.update(contentValues);
    }

    @Override
    public int remove(ContentValues contentValues) {
        return proGuardianDBHelper.remove(contentValues);
    }

    @Override
    public List<ContentValues> search(ContentValues contentValues) {
        List<ContentValues> list = new ArrayList<>();

        list = proGuardianDBHelper.search(contentValues);
        return list;
    }

    @Override
    public int insertServer(ContentValues contentValues) {
        int check = 0;
        return check;
    }

    @Override
    public int updateServer(ContentValues contentValues) {
        int check = 0;
        return check;
    }

    @Override
    public int removeServer(ContentValues contentValues) {
        int check = 0;
        return check;
    }

    @Override
    public List<ContentValues> searchServer(ContentValues contentValues) {
        List<ContentValues> list = new ArrayList<ContentValues>();
        return list;
    }

    @Override
    public void setDBHelper(ProGuardianDBHelper proGuardianDBHelper) {
        this.proGuardianDBHelper = proGuardianDBHelper;
    }

    @Override
    public ProGuardianDBHelper getDBHelper() {
        return this.proGuardianDBHelper;
    }
}
